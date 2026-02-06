#ifndef GB_BUDGET_H
#define GB_BUDGET_H

#include <QObject>
#include "KT.h"

//<!-- API -->

//KT.CLDController *budgetCtrl();

//<!-- Компонент -->

class BudgetComponent: public QObject {
    Q_OBJECT

    public:
        static BudgetComponent& singleton() {
            static BudgetComponent instance;
            return instance;
        }

    private:
        BudgetComponent();
        BudgetComponent(BudgetComponent const&);
        void operator=(BudgetComponent const&);

        //CLDController *_ctrl;
};

#endif // GB_BUDGET_H
