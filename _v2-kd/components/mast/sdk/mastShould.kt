package org.opengamestudio

// Launch only once
//
// Purpose: Work around Android's activity restart
//
// Conditions:
// 1. UI has been created the first time
fun mastShouldLaunch(c: MastContext): MastContext {
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

// Reset Budget tab selection state
//
// Conditions:
// 1. Did launch
// 2. User did click Budget tab
// 3. User did click another tab
fun mastShouldResetBudgetTabSelection(c: MastContext): MastContext {
    /* 1 */ if (c.recentField == F.didLaunch) {
        c.isBudgetTabSelected = true
        c.recentField = F.isBudgetTabSelected
        return c
    }

    /* 2 */ if (c.recentField == F.didClickBudgetTab) {
        c.isBudgetTabSelected = true
        c.recentField = F.isBudgetTabSelected
        return c
    }

    /* 3 */ if (c.recentField == F.didClickSettingsTab) {
        c.isBudgetTabSelected = false
        c.recentField = F.isBudgetTabSelected
        return c
    }

    c.recentField = F.none
    return c
}

// Consolidate logs
//
// Conditions:
// 1. Budget context changed
// 2. Git context changed
// 3. Log context changed
fun mastShouldResetLogs(c: MastContext): MastContext {
    /* 1 */ if (c.recentField == F.logBudgetChange) {
        c.logs += c.logBudgetChange
        c.recentField = F.logs
        return c
    }

    /* 2 */ if (c.recentField == F.logGitChange) {
        c.logs += c.logGitChange
        c.recentField = F.logs
        return c
    }

    /* 3 */ if (c.recentField == F.logHistoryChange) {
        c.logs += c.logHistoryChange
        c.recentField = F.logs
        return c
    }

    c.recentField = F.none
    return c
}

// Reset Settings tab selection state
//
// Conditions:
// 1. User did click Settings tab
// 2. User did click another tab
fun mastShouldResetSettingsTabSelection(c: MastContext): MastContext {
    /* 2 */ if (c.recentField == F.didClickSettingsTab) {
        c.isSettingsTabSelected = true
        c.recentField = F.isSettingsTabSelected
        return c
    }

    /* 3 */ if (c.recentField == F.didClickBudgetTab) {
        c.isSettingsTabSelected = false
        c.recentField = F.isSettingsTabSelected
        return c
    }

    c.recentField = F.none
    return c
}
