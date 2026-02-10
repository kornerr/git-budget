#include <any>
#include <cstdio>
#include <iostream>
#include "budget.h"

#define KT_ANY = libgb_kref_kotlin_Any

void onDebugPrint() {
    printf("ИГР onDP-1\n");
    //auto ctrl = KT.budgetController();
    printf("ИГР onDP-2\n");
    //auto bctx = KT.budgetContext();
    //auto ctx = KT.budgetContextToCLD(bctx);
    auto ctx = KT.budgetControllerContext();
    auto value = KT.CLDContext.fieldAny(ctx, "didLaunch");
    std::cout << "ИГР onDP-3 ctx.didLaunch: '" << KT.anyToBool(value) << "'\n";

    //auto ctx = KT.CLDController.currentContext(ctrl);
    //printf("ИГР onDP-3 field/value: '%s'/'%s'\n");
}

BudgetComponent::BudgetComponent() {
    auto item = KT.Item.Item(9, 8, 7, 6);
    printf("ИГР x = '%d'\n", KT.Item.get_x(item));
    auto ctrl = KT.budgetController();

    KT.CLDController.registerFieldCallbackC(
        ctrl,
        "didLaunch",
        (void *)&onDebugPrint
        //[]() { printf("ИГР BudgetC.ctrl got did launch\n"); }
    );

    
    KT.CLDController.set(ctrl, "didLaunch", KT.boolAsAny(true));
    KT.CLDController.set(ctrl, "didLaunch", KT.boolAsAny(false));
}

void BudgetComponent::doTest() {
    printf("ИГР BudgetC.doT\n");
}
