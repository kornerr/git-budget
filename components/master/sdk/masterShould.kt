package org.opengamestudio

// Launch only once
//
// Purpose: Work around Android's activity restart
//
// Conditions:
// 1. UI has been created the first time
fun masterShouldLaunch(c: MasterContext): MasterContext {
    /* 1 */ if (
        c.recentField == F.didSetup &&
        !c.didLaunch
    ) {
        c.didLaunch = true
        c.recentField = F.didLaunch
        return c
    }

    c.recentField = F.none
    return c
}

