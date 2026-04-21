#include <any>
#include <cstdio>
#include <vector>

#include "budget.h"
#include "ignore.kd.h"

#define CB(code) std::make_any<std::function<void(BudgetContext)>>([&](BudgetContext c) { code })
#define VM VM::singleton()


BudgetComponent::BudgetComponent() {
    /*
    std::vector<std::any> oneliners = {
        F.greetingText, CB( VM.mainSetGreetingText(c.greetingText()); ),
        F.isVisible, CB( VM.mainSetIsVisible(c.isVisible()); ),
    };
    BudgetEffectRegistry::registerOneliners(KT.budgetCtrl(), oneliners);
    */
}

void BudgetComponent::setup() {
    budgetSet(F.didSetup, true);
}
