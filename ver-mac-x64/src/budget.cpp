#include <cstdio>
#include "budget.h"

BudgetComponent::BudgetComponent() {
    //libgb_ExportedSymbols *_lib = libgb_symbols();
    auto item = KT.Item.Item(9, 8, 7, 6);
    printf("ИГР x = '%d'\n", KT.Item.get_x(item));
    //_lib->kotlin.root.org.opengamestudio.CLDController.CLDController abc;
    /*_ctrl =*/// new K.CLDController(K.budgetContext());
}

void BudgetComponent::doTest() {
    printf("ИГР BudgetC.doT\n");
}
