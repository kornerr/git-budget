package org.opengamestudio

private typealias MC = MasterContext

object MasterComponent {
    init {
        val vm = VM
        val oneliners = arrayOf(
            F.didLaunch, { c: MC -> masterLaunchComponents() },
            F.didLaunch, { c: MC -> masterLogComponents() },
            F.isBudgetTabSelected, { c: MC -> vm.isBudgetTabSelected.value = c.isBudgetTabSelected },
            F.isSettingsTabSelected, { c: MC -> vm.isSettingsTabSelected.value = c.isSettingsTabSelected },
            F.logs, { c: MC -> masterFillLogs(c.logs, vm) },
        )
        registerOneliners(masterCtrl(), oneliners)
    }

    fun setup() {
        masterBindComponents()
        masterSet(F.didSetup, true)
    }
}
