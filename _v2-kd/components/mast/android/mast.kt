package org.opengamestudio

private typealias MC = MastContext

object MastComponent {
    init {
        val vm = VM
        val oneliners = arrayOf(
            F.didLaunch, { c: MC -> mastLaunchComponents() },
            F.didLaunch, { c: MC -> mastLogComponents() },
            F.isBudgetTabSelected, { c: MC -> vm.isBudgetTabSelected.value = c.isBudgetTabSelected },
            F.isSettingsTabSelected, { c: MC -> vm.isSettingsTabSelected.value = c.isSettingsTabSelected },
            F.logs, { c: MC -> mastFillLogs(c.logs, vm) },
        )
        registerOneliners(mastCtrl(), oneliners)
    }

    fun setup() {
        mastBindComponents()
        mastSet(F.didSetup, true)
    }
}
