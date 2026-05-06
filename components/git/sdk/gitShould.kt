package org.opengamestudio

// Trigger the listing of files for the specified directory
//
// Conditions:
// 1. Component did launch
fun gitShouldResetFilesDir(c: GitContext): GitContext {
    if (c.recentField == F.didLaunch) {
        c.filesDir = "/todo/path/from/what/passed"
        c.recentField = F.filesDir
        return c
    }

    c.recentField = F.none
    return c
}
