#include "budget.h"

#include "libgb_api.h"
//#define K _lib->kotlin.root.org.opengamestudio

BudgetComponent::BudgetComponent() {
    libgb_ExportedSymbols *_lib = libgb_symbols();
    _lib->kotlin.root.org.opengamestudio.CLDController.CLDController abc;
    /*_ctrl =*/// new K.CLDController(K.budgetContext());
}
