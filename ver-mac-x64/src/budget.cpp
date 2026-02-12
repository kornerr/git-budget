#include <any>
#include <cstdio>
#include "budget.h"

void onDebugPrint() {
    auto ctx = KT.budgetControllerContext();
    auto key = KT.CLDContext.get_recentField(ctx);
    auto value = KT.CLDContext.fieldAny(ctx, key);
    auto strvalue = KT.anyToString(value);
    printf("ИГР BudgetC.ctrl k/v: '%s'/'%s'\n", key, strvalue);
    KT_LIB->DisposeString(key);
    KT_LIB->DisposeString(strvalue);
}

void onDidLaunch() {
    auto ctx = KT.budgetControllerContext();
    auto key = KT.CLDContext.get_recentField(ctx);
    auto value = KT.CLDContext.fieldAny(ctx, key);
    auto strvalue = KT.anyToString(value);
    printf("ИГР BudgetC.ctrl onDL: '%s'/'%s'\n", key, strvalue);
    KT_LIB->DisposeString(key);
    KT_LIB->DisposeString(strvalue);
}

BudgetComponent::BudgetComponent() {
    auto item = KT.Item.Item(9, 8, 7, 6);
    printf("ИГР x = '%d'\n", KT.Item.get_x(item));
    auto ctrl = KT.budgetController();

    // Debug effect to print every change
    KT.CLDController.registerCallbackC(
        ctrl,
        (void *)&onDebugPrint
    );

    // Debug effect to print specific field changes
    KT.CLDController.registerFieldCallbackC(
        ctrl,
        "didLaunch",
        (void *)&onDidLaunch
    );
    
    budgetCtrlSet("didLaunch", true);
    budgetCtrlSet("didLaunch", false);
    //KT.CLDController.set(ctrl, "didLaunch", KT.boolAsAny(true));
    //KT.CLDController.set(ctrl, "didLaunch", KT.boolAsAny(false));
    KT.CLDController.set(ctrl, "inputDate", KT.strAsAny("123-00-00"));
}

void BudgetComponent::doTest() {
    printf("ИГР BudgetC.doT\n");
}
