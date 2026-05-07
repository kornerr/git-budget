package org.opengamestudio

private typealias MC = MasterContext

object MasterComponent {
    init {
        val oneliners = arrayOf(
            F.didLaunch, { c: MC -> masterLaunchComponents() },
        )
        registerOneliners(masterCtrl(), oneliners)
    }

    fun setup() {
        masterSet(F.didSetup, true)
    }
}
