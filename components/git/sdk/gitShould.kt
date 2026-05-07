package org.opengamestudio

// Trigger the listing of files for the specified directory
//
// Conditions:
// 1. Root dir has been set
fun gitShouldResetFilesDir(c: GitContext): GitContext {
    if (c.recentField == F.rootDir) {
        c.filesDir = c.rootDir + "/" + GIT_REPO_DIR
        c.recentField = F.filesDir
        return c
    }

    c.recentField = F.none
    return c
}
