package org.opengamestudio

fun mastBindComponents() {
    // Budg -> Git
    budgCtrl().registerFieldCallback(F.didClickCommit, { cc: KDContext ->
        gitSet(F.commit, true)
    })
    budgCtrl().registerFieldCallback(F.didClickPush, { cc: KDContext ->
        gitSet(F.push, true)
    })

    // Git -> Hist
    gitCtrl().registerFieldCallback(F.didClone, { cc: KDContext ->
        histSet(F.didClone, true)
    })
    gitCtrl().registerFieldCallback(F.didPull, { cc: KDContext ->
        histSet(F.didPull, true)
    })
    gitCtrl().registerFieldCallback(F.repoDir, { cc: KDContext ->
        val c = cc as GitContext
        histSet(F.repoDir, c.repoDir)
    })
    
    // Hist -> Budg
    histCtrl().registerFieldCallback(F.items, { cc: KDContext ->
        val c = cc as HistContext
        budgSet(F.historyItems, c.items)
    })
}

fun mastFillLogs(
    items: Array<String>,
    vm: VM
) {
    vm.logs.clear()
    vm.logs.addAll(items)
}

fun mastLaunchComponents() {
    BudgComponent.launch()
    GitComponent.launch()
    HistComponent.launch()
}

fun mastLogComponents() {
    budgCtrl().registerCallback { c ->
        val dbg = formatContextChange(c, "Budg")
        mastSet(F.logBudgetChange, dbg)
    }
    gitCtrl().registerCallback { c ->
        val dbg = formatContextChange(c, "Git")
        mastSet(F.logGitChange, dbg)
    }
    histCtrl().registerCallback { c ->
        val dbg = formatContextChange(c, "Hist")
        mastSet(F.logHistoryChange, dbg)
    }
}
