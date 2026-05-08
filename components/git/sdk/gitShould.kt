package org.opengamestudio

// Clone repository
//
// Conditions:
// 1. Repository dir is absent
fun gitShouldClone(c: GitContext): GitContext {
    if (
        c.recentField == F.repoDirExists &&
        !c.repoDirExists
    ) {
        c.clone = true
        c.recentField = F.clone
        return c
    }

    c.recentField = F.none
    return c
}

// Set repository dir
//
// Conditions:
// 1. Root dir has been set
fun gitShouldResetRepoDir(c: GitContext): GitContext {
    if (c.recentField == F.rootDir) {
        c.repoDir = c.rootDir + "/" + GIT_REPO_DIR
        c.recentField = F.repoDir
        return c
    }

    c.recentField = F.none
    return c
}
