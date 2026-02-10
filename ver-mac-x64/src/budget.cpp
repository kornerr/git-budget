#include <any>
#include <cstdio>
#include "budget.h"

void onDebugPrint() {
    auto ctx = KT.budgetControllerContext();
    auto value = KT.CLDContext.fieldAny(ctx, "didLaunch");
    printf("ИГР ctx.didLaunch: '%s'\n", KT.anyToString(value));
}

BudgetComponent::BudgetComponent() {
    auto item = KT.Item.Item(9, 8, 7, 6);
    printf("ИГР x = '%d'\n", KT.Item.get_x(item));
    auto ctrl = KT.budgetController();

    KT.CLDController.registerFieldCallbackC(
        ctrl,
        "didLaunch",
        (void *)&onDebugPrint
    );

    
    KT.CLDController.set(ctrl, "didLaunch", KT.boolAsAny(true));
    KT.CLDController.set(ctrl, "didLaunch", KT.boolAsAny(false));
}

void BudgetComponent::doTest() {
    printf("ИГР BudgetC.doT\n");
}
