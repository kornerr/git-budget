#include <cstdio>
#include "budget.h"

BudgetComponent::BudgetComponent() {
    auto item = KT.Item.Item(9, 8, 7, 6);
    printf("ИГР x = '%d'\n", KT.Item.get_x(item));
    auto ctrl = KT.budgetController();
}

void BudgetComponent::doTest() {
    printf("ИГР BudgetC.doT\n");
}
