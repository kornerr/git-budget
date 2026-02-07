#include <any>
#include <cstdio>
#include "budget.h"

#define KT_ANY = libgb_kref_kotlin_Any

void onDidLaunch(const libgb_kref_org_opengamestudio_CLDContext &c) {
    printf("ИГР onDL got did launch\n");
}

BudgetComponent::BudgetComponent() {
    auto item = KT.Item.Item(9, 8, 7, 6);
    printf("ИГР x = '%d'\n", KT.Item.get_x(item));
    auto ctrl = KT.budgetController();

    KT.CLDController.registerFieldCallback(
        ctrl,
        "didLaunch",
        onDidLaunch
        //[]() { printf("ИГР BudgetC.ctrl got did launch\n"); }
    );

    
    KT.CLDController.set(ctrl, "didLaunch", KT.boolAsAny(true));
}

void BudgetComponent::doTest() {
    printf("ИГР BudgetC.doT\n");
}
