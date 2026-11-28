// Prototype dependencies
#include <CoreFoundation/CoreFoundation.h>
#include <Security/Security.h>
#include <git2.h>
#include <uuid/uuid.h>

// Portable C
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

// POSIX
#include <fcntl.h>
#include <sys/stat.h>
#include <unistd.h>

#define MAIN_REF "refs/heads/main"

static const char repo_url[] = "https://192.168.1.153:3000/testuser/check-git-access.git";
static const char username[] = "testuser";

struct transport {
    SecCertificateRef ca;
    const char *token;
    unsigned int credential_attempts;
};

static void check_git(int result, const char *action)
{
    if (result < 0) {
        const git_error *error = git_error_last();
        fprintf(stderr, "%s: %s (%d)\n", action, error ? error->message : "libgit2 error", result);
        exit(EXIT_FAILURE);
    }
}

static int credentials(git_credential **out, const char *url, const char *user,
                       unsigned int allowed, void *payload)
{
    struct transport *transport = payload;
    (void)url;
    (void)user;

    if ((allowed & GIT_CREDENTIAL_USERPASS_PLAINTEXT) && transport->credential_attempts++ == 0) {
        return git_credential_userpass_plaintext_new(out, username, transport->token);
    }
    return GIT_PASSTHROUGH;
}

static int check_certificate(git_cert *certificate, int valid, const char *host, void *payload)
{
    const struct transport *transport = payload;
    (void)valid;
    if (certificate->cert_type != GIT_CERT_X509) {
        return GIT_ECERTIFICATE;
    }

    const git_cert_x509 *x509 = (const git_cert_x509 *)certificate;
    CFDataRef der = CFDataCreate(kCFAllocatorDefault, x509->data, (CFIndex)x509->len);
    SecCertificateRef leaf = SecCertificateCreateWithData(kCFAllocatorDefault, der);
    CFRelease(der);
    if (!leaf) {
        return GIT_ECERTIFICATE;
    }

    CFStringRef hostname = CFStringCreateWithCString(kCFAllocatorDefault, host, kCFStringEncodingUTF8);
    SecPolicyRef policy = SecPolicyCreateSSL(true, hostname);
    const void *anchors[] = {transport->ca};
    CFArrayRef ca = CFArrayCreate(kCFAllocatorDefault, anchors, 1, &kCFTypeArrayCallBacks);
    SecTrustRef trust = NULL;
    int trusted = SecTrustCreateWithCertificates(leaf, policy, &trust) == errSecSuccess
        && SecTrustSetAnchorCertificates(trust, ca) == errSecSuccess
        && SecTrustSetAnchorCertificatesOnly(trust, true) == errSecSuccess
        && SecTrustEvaluateWithError(trust, NULL);

    if (trust) {
        CFRelease(trust);
    }
    CFRelease(ca);
    CFRelease(policy);
    CFRelease(hostname);
    CFRelease(leaf);
    return trusted ? 0 : GIT_ECERTIFICATE;
}

static SecCertificateRef load_ca(const char *path)
{
    FILE *file = fopen(path, "rb");
    if (!file) {
        perror(path);
        exit(EXIT_FAILURE);
    }
    unsigned char bytes[4096];
    size_t size = fread(bytes, 1, sizeof(bytes), file);
    int complete = feof(file) && !ferror(file);
    fclose(file);

    CFDataRef data = CFDataCreate(kCFAllocatorDefault, bytes, (CFIndex)size);
    SecCertificateRef ca = SecCertificateCreateWithData(kCFAllocatorDefault, data);
    CFRelease(data);
    if (!complete || !ca) {
        fprintf(stderr, "Could not read the DER CA certificate: %s\n", path);
        exit(EXIT_FAILURE);
    }
    return ca;
}

static git_commit *prepare_main(git_repository *repo)
{
    git_object *base = NULL;
    int result = git_revparse_single(&base, repo, "refs/remotes/origin/main^{commit}");
    if (result == GIT_ENOTFOUND) {
        result = git_revparse_single(&base, repo, "HEAD^{commit}");
    }

    if (result != GIT_ENOTFOUND && result != GIT_EUNBORNBRANCH) {
        check_git(result, "Resolve the base commit");
        git_checkout_options checkout;
        check_git(git_checkout_options_init(&checkout, GIT_CHECKOUT_OPTIONS_VERSION), "Initialize checkout");
        checkout.checkout_strategy = GIT_CHECKOUT_SAFE;
        check_git(git_checkout_tree(repo, base, &checkout), "Check out the fetched commit");

        // Only this invocation's temporary clone is updated here.
        git_reference *main_ref = NULL;
        check_git(git_reference_create(&main_ref, repo, MAIN_REF, git_object_id(base), 1,
                                       "Prepare main from the fetched history"), "Update local main");
        git_reference_free(main_ref);
    }

    check_git(git_repository_set_head(repo, MAIN_REF), "Select main");
    return (git_commit *)base;
}

static void append_log(git_repository *repo)
{
    char path[PATH_MAX];
    if (snprintf(path, sizeof(path), "%slog.txt", git_repository_workdir(repo)) >= (int)sizeof(path)) {
        fprintf(stderr, "The log.txt path is too long.\n");
        exit(EXIT_FAILURE);
    }

    int fd = open(path, O_CREAT | O_NOFOLLOW | O_RDWR | O_NONBLOCK, 0644);
    if (fd < 0) {
        perror(path);
        exit(EXIT_FAILURE);
    }
    struct stat info;
    if (fstat(fd, &info) < 0 || !S_ISREG(info.st_mode)) {
        fprintf(stderr, "log.txt must be a regular file.\n");
        exit(EXIT_FAILURE);
    }
    FILE *log = fdopen(fd, "r+");
    if (!log) {
        perror("Open log.txt");
        exit(EXIT_FAILURE);
    }

    unsigned char buffer[4096];
    size_t length;
    int last = '\n';
    puts("Current log.txt:");
    while ((length = fread(buffer, 1, sizeof(buffer), log)) != 0) {
        fwrite(buffer, 1, length, stdout);
        last = buffer[length - 1];
    }
    if (ferror(log) || fseek(log, 0, SEEK_END) != 0) {
        perror("Read log.txt");
        exit(EXIT_FAILURE);
    }
    if (last != '\n') {
        putchar('\n');
    }

    time_t now = time(NULL);
    struct tm local;
    char timestamp[sizeof("2026-11-28 18:06:55")];
    if (!localtime_r(&now, &local) || !strftime(timestamp, sizeof(timestamp), "%Y-%m-%d %H:%M:%S", &local)) {
        fprintf(stderr, "Could not format the current time.\n");
        exit(EXIT_FAILURE);
    }
    uuid_t random_uuid;
    uuid_string_t uuid;
    uuid_generate_random(random_uuid);
    uuid_unparse_lower(random_uuid, uuid);

    if (fprintf(log, "%s%s uuid-%s\n", last == '\n' ? "" : "\n", timestamp, uuid) < 0 || fclose(log) != 0) {
        perror("Write log.txt");
        exit(EXIT_FAILURE);
    }
    printf("Appended %s uuid-%s\n", timestamp, uuid);
}

static git_oid commit_log(git_repository *repo, git_commit *parent)
{
    git_index *index = NULL;
    git_oid tree_id, commit_id;
    git_tree *tree = NULL;
    git_signature *signature = NULL;
    const git_commit *parents[] = {parent};

    check_git(git_repository_index(&index, repo), "Open the index");
    check_git(git_index_add_bypath(index, "log.txt"), "Stage log.txt");
    check_git(git_index_write_tree(&tree_id, index), "Write the commit tree");
    check_git(git_tree_lookup(&tree, repo, &tree_id), "Read the commit tree");
    check_git(git_signature_now(&signature, username, "testuser@localhost"), "Create the commit signature");
    check_git(git_commit_create(&commit_id, repo, "HEAD", signature, signature, NULL,
                                "Append log entry", tree, parent ? 1 : 0, parents), "Commit log.txt");
    git_signature_free(signature);
    git_tree_free(tree);
    git_index_free(index);
    return commit_id;
}

static int check_push(const char *ref, const char *status, void *payload)
{
    (void)payload;
    if (status) {
        fprintf(stderr, "Remote rejected %s: %s\n", ref, status);
        return -1;
    }
    return 0;
}

static int remote_message(const char *message, int length, void *payload)
{
    (void)payload;
    fwrite(message, 1, (size_t)length, stderr);
    return 0;
}

int main(int argc, char **argv)
{
    if (argc != 3) {
        fprintf(stderr, "Usage: %s CLONE_DIRECTORY CA_CERTIFICATE\n", argv[0]);
        return EXIT_FAILURE;
    }
    const char *token = getenv("GITEA_TOKEN");
    if (!token || !*token) {
        fprintf(stderr, "Set GITEA_TOKEN or start the program with run-libgit2.\n");
        return EXIT_FAILURE;
    }

    check_git(git_libgit2_init(), "Initialize libgit2");
    struct transport transport = {load_ca(argv[2]), token, 0};
    git_clone_options clone;
    check_git(git_clone_options_init(&clone, GIT_CLONE_OPTIONS_VERSION), "Initialize clone");
    clone.fetch_opts.callbacks.credentials = credentials;
    clone.fetch_opts.callbacks.certificate_check = check_certificate;
    clone.fetch_opts.callbacks.payload = &transport;
    clone.fetch_opts.follow_redirects = GIT_REMOTE_REDIRECT_NONE;

    git_repository *repo = NULL;
    git_remote *remote = NULL;
    check_git(git_clone(&repo, repo_url, argv[1], &clone), "Clone the test repository");
    check_git(git_remote_lookup(&remote, repo, "origin"), "Find origin");
    transport.credential_attempts = 0;
    check_git(git_remote_fetch(remote, NULL, &clone.fetch_opts, NULL), "Fetch the latest changes");

    git_commit *parent = prepare_main(repo);
    append_log(repo);
    git_oid commit = commit_log(repo, parent);

    git_push_options push;
    check_git(git_push_options_init(&push, GIT_PUSH_OPTIONS_VERSION), "Initialize push");
    push.callbacks = clone.fetch_opts.callbacks;
    push.callbacks.push_update_reference = check_push;
    push.callbacks.sideband_progress = remote_message;
    push.follow_redirects = GIT_REMOTE_REDIRECT_NONE;
    char main_refspec[] = MAIN_REF ":" MAIN_REF;
    char *refspecs[] = {main_refspec};
    git_strarray refs = {refspecs, 1};
    transport.credential_attempts = 0;
    check_git(git_remote_push(remote, &refs, &push), "Push main");
    printf("Pushed %s to main.\n", git_oid_tostr_s(&commit));

    git_commit_free(parent);
    git_remote_free(remote);
    git_repository_free(repo);
    CFRelease(transport.ca);
    git_libgit2_shutdown();
    return EXIT_SUCCESS;
}
