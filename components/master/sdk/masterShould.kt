package org.opengamestudio

// Trigger the listing of files for the specified directory
//
// Conditions:
// 1. Component did launch
fun masterShouldResetFilesDir(c: MasterContext): MasterContext {
    if (c.recentField == "didLaunch") {
        c.dataDir = "/todo/path/from/what/passed"
        c.recentField = "dataDir"
        return c
    }

    c.recentField = F.none
    return c
}
