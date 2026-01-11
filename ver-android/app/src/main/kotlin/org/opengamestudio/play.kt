package org.opengamestudio

//<!-- API -->

fun playCtrl(): CLDController {
    return PlayComponent.ctrl
}

//<!-- Constants -->

//<!-- Component -->

private typealias PC = PlayContext

object PlayComponent {
    val ctrl: CLDController

    init {
        ctrl = CLDController(PlayContext())
        // Debug
        ctrl.registerCallback { c ->
            var value = "${c.field(c.recentField) as Any}"
            println("ИГР PlayC.init ctrl key/value: '${c.recentField}'/'$value'")
        }

        // Default values
        //ctrl.set("busId", uuidString())

        setupEffects()
        setupShoulds()
    }

    fun setupEffects() {
        val vm = VM
        val oneliners = arrayOf(
            "isPlaygroundVisible", { c: PC -> vm.playgroundIsVisible.value = c.isPlaygroundVisible },
            "url", { c: PC -> vm.webView?.loadUrl(c.url) },
        )
        registerOneliners(ctrl, oneliners)
    }

    fun setupShoulds() {
        arrayOf(
          ::playShouldResetPlaygroundVisibility,
          ::playShouldResetPlaygroundURL,
        ).forEach { f ->
          ctrl.registerFunction { c -> f(c as PlayContext) }
        }
    }
}

//<!-- Effects -->
 
