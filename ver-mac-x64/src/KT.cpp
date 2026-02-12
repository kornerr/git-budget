#include "KT.h"

template<>
void budgetCtrlSet(
    const std::string &key,
    bool value
) {
    auto ctrl = KT.budgetController();
    KT.CLDController.set(ctrl, key.c_str(), KT.boolToAny(value));
}

template<>
void budgetCtrlSet(
    const std::string &key,
    const char *value
) {
    auto ctrl = KT.budgetController();
    KT.CLDController.set(ctrl, key.c_str(), KT.strAsAny(value));
}

BudgetContext::BudgetContext(
    libgb_kref_org_opengamestudio_BudgetContext ctx
):
  ctx(ctx) { }

bool BudgetContext::didLaunch() const {
    return KT.BudgetContext.get_didLaunch(ctx);
}
