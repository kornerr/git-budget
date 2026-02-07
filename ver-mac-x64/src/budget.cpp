#include <any>
#include <cstdio>
#include "budget.h"

#define KT_ANY = libgb_kref_kotlin_Any

BudgetComponent::BudgetComponent() {
    auto item = KT.Item.Item(9, 8, 7, 6);
    printf("ИГР x = '%d'\n", KT.Item.get_x(item));
    auto ctrl = KT.budgetController();
    
    KT.CLDController.set(ctrl, "didLaunch", KT.boolAsAny(true));
}

void BudgetComponent::doTest() {
    printf("ИГР BudgetC.doT\n");
}
