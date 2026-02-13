#include <any>
#include <cstdio>
#include <vector>
#include "budget.h"

void onDebugPrint() {
    auto bctx = KT.budgetControllerContext();
    auto ctx = KT.budgetContextToCLD(bctx);
    auto key = KT.CLDContext.get_recentField(ctx);
    auto value = KT.CLDContext.fieldAny(ctx, key);
    auto strvalue = KT.anyToString(value);
    printf("ИГР BudgetC.ctrl k/v: '%s'/'%s'\n", key, strvalue);
    KT_LIB->DisposeString(key);
    KT_LIB->DisposeString(strvalue);
}

void onDidLaunch() {
    auto ctx = BudgetContext(KT.budgetControllerContext());
    auto value = ctx.didLaunch();
    auto key = "didLaunch";
    printf("ИГР BudgetC.ctrl onDL: '%s'/'%d'\n", key, value);
}

BudgetComponent::BudgetComponent() {
    auto item = KT.Item.Item(9, 8, 7, 6);
    printf("ИГР x = '%d'\n", KT.Item.get_x(item));
    //auto ctrl = KT.budgetController();
    libgb_kref_org_opengamestudio_CLDController ctrl = KT.budgetController();

    // Debug effect to print every change
    KT.CLDController.registerCallbackC(
        ctrl,
        (void *)&onDebugPrint
    );

    /**/printf("ИГР ctrl type: '%s'\n", typeid(ctrl).name());

    // Debug effect to print specific field changes
    KT.CLDController.registerFieldCallbackC(
        ctrl,
        "didLaunch",
        (void *)&onDidLaunch
    );

    std::vector<std::any> oneliners = {
        "didLaunch", std::make_any<std::function<void(void)>>([&]() { printf("ИГР lambda didL\n"); }),
        "inputDate", std::make_any<std::function<void(void)>>([&]() { printf("ИГР lambda inputD\n"); }),
    };

    auto funptr = std::any_cast<std::function<void(void)>>(oneliners[1]);
    funptr();

    
    budgetCtrlSet("didLaunch", true);
    budgetCtrlSet("didLaunch", false);
    budgetCtrlSet("inputDate", "123-00-00");
}

void BudgetComponent::doTest() {
    printf("ИГР BudgetC.doT\n");
}
