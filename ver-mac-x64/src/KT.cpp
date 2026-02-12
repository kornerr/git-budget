#include "KT.h"

template<>
void budgetCtrlSet(
    const std::string &key,
    bool value
) {
    auto ctrl = KT.budgetController();
    KT.CLDController.set(ctrl, key.c_str(), KT.boolAsAny(value));
}

template<>
void budgetCtrlSet(
    const std::string &key,
    const char *value
) {
    auto ctrl = KT.budgetController();
    KT.CLDController.set(ctrl, key.c_str(), KT.strAsAny(value));
}
