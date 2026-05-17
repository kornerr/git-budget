package org.opengamestudio

fun masterBindComponents() {
    // Budget -> Git
    budgetCtrl().registerFieldCallback(F.didClickCommit, { cc: KDContext ->
        gitSet(F.commit, true)
    })
    budgetCtrl().registerFieldCallback(F.didClickPush, { cc: KDContext ->
        gitSet(F.push, true)
    })

    // Git -> History
    gitCtrl().registerFieldCallback(F.didClone, { cc: KDContext ->
        historySet(F.didClone, true)
    })
    gitCtrl().registerFieldCallback(F.didPull, { cc: KDContext ->
        historySet(F.didPull, true)
    })
    gitCtrl().registerFieldCallback(F.repoDir, { cc: KDContext ->
        val c = cc as GitContext
        historySet(F.repoDir, c.repoDir)
    })
    
    // History -> Budget
    historyCtrl().registerFieldCallback(F.items, { cc: KDContext ->
        val c = cc as HistoryContext
        budgetSet(F.historyItems, c.items)
    })
}

fun masterFillLogs(
    items: Array<String>,
    vm: VM
) {
    vm.logs.clear()
    vm.logs.addAll(items)
}

fun masterLaunchComponents() {
    BudgetComponent.launch()
    GitComponent.launch()
    HistoryComponent.launch()
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
    historyCtrl().registerCallback { c ->
        val dbg = formatContextChange(c, "History")
        masterSet(F.logHistoryChange, dbg)
    }
}
