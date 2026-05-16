package org.opengamestudio

fun masterBindComponents() {
    // Budget -> Git
    budgetCtrl().registerFieldCallback(F.didClickCommit, { cc: KDContext ->
        gitSet(F.commit, true)
    })
    budgetCtrl().registerFieldCallback(F.didClickPush, { cc: KDContext ->
        gitSet(F.push, true)
    })

    // Git -> Log
    gitCtrl().registerFieldCallback(F.didClone, { cc: KDContext ->
        logSet(F.didClone, true)
    })
    gitCtrl().registerFieldCallback(F.didPull, { cc: KDContext ->
        logSet(F.didPull, true)
    })
    gitCtrl().registerFieldCallback(F.repoDir, { cc: KDContext ->
        val c = cc as GitContext
        logSet(F.repoDir, c.repoDir)
    })
    
    // Log -> Budget
    logCtrl().registerFieldCallback(F.items, { cc: KDContext ->
        val c = cc as LogContext
        budgetSet(F.logItems, c.items)
    })
}

fun masterLaunchComponents() {
    BudgetComponent.launch()
    GitComponent.launch()
    LogComponent.launch()
}

fun masterLogComponents() {
    budgetCtrl().registerCallback { c ->
        val dbg = formatContextChange(c, "Budget")
        masterSet(F.logBudgetChange, dbg)
    }
    gitCtrl().registerCallback { c ->
        val dbg = formatContextChange(c, "Git")
        masterSet(F.logGitChange, dbg)
    }
    logCtrl().registerCallback { c ->
        val dbg = formatContextChange(c, "Log")
        masterSet(F.logLogChange, dbg)
    }
}
