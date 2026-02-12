#ifndef GB_KT_H
#define GB_KT_H

#include <string>

#include "libgb_api.h"
#define KT libgb_symbols()->kotlin.root.org.opengamestudio
#define KT_LIB libgb_symbols()

template<typename T>
void budgetCtrlSet(
    const std::string &key,
    T value
);

class BudgetContext {

    public:
        BudgetContext(libgb_kref_org_opengamestudio_BudgetContext ctx);

        bool didLaunch() const;

    private:
        libgb_kref_org_opengamestudio_BudgetContext ctx;
};

#endif // GB_KT_H
