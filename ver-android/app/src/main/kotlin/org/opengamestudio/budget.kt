package org.opengamestudio

//<!-- API -->

fun budgetCtrl(): CLDController {
    return BudgetComponent.ctrl
}

//<!-- Constants -->

//<!-- Component -->

private typealias BC = BudgetContext

object BudgetComponent {
    val ctrl: CLDController

    init {
        ctrl = CLDController(BudgetContext())
        // Debug
        ctrl.registerCallback { c ->
            var value = "${c.field(c.recentField) as Any}"
            println("ИГР BudgetC.init ctrl key/value: '${c.recentField}'/'$value'")
        }

        // Default values
        //ctrl.set("busId", uuidString())

        setupEffects()
        setupShoulds()
    }

    fun setupEffects() {
        val vm = VM
        /*
        val oneliners = arrayOf(
            "isPlaygroundVisible", { c: BC -> vm.playgroundIsVisible.value = c.isPlaygroundVisible },
        )
        registerOneliners(ctrl, oneliners)
        */
    }

    fun setupShoulds() {
        /*
        arrayOf(
          ::playShouldResetPlaygroundVisibility,
        ).forEach { f ->
          ctrl.registerFunction { c -> f(c as BudgetContext) }
        }
        */
    }
}

//<!-- Effects -->
 
