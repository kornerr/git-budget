#include "ignore.kd.h"
#include "KT.h"

template<> void budgetSet(
    const std::string &key,
    QString value
) {
    budgetSet(key, value.toStdString().c_str());
}

template<> void budgetSet(
    const std::string &key,
    int value
) {
    KT.KDController.set(KT.budgetCtrl(), key.c_str(), KT.intToAny(value));
}

