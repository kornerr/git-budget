#include <any>
#include <cstdio>
#include "budget.h"

#define KT_ANY = libgb_kref_kotlin_Any

void onDebugPrint() {
    printf("ИГР onDP TODO\n");
}

void printCB() {
    printf("ИГР printCB\n");
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

    KT.callCallbackC((void *)&printCB);
}

void BudgetComponent::doTest() {
    printf("ИГР BudgetC.doT\n");
}
