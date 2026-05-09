package org.opengamestudio

fun masterBindComponents() {
    budgetCtrl().registerFieldCallback(F.didClickCommit, { cc: KDContext ->
        gitSet(F.commit, true)
    })
    budgetCtrl().registerFieldCallback(F.didClickPush, { cc: KDContext ->
        gitSet(F.push, true)
    })
}

fun masterLaunchComponents() {
    BudgetComponent.launch()
    GitComponent.launch()
}
