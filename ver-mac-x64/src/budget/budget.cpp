#include <any>
#include <vector>

#include "budget.h"
#include "ignore.kd.h"
#include "VM.h"

#define CB(code) std::make_any<std::function<void(BudgetContext)>>([&](BudgetContext c) { code })
#define VM VM::singleton()

BudgetComponent::BudgetComponent() {
    std::vector<std::any> oneliners = {
      /*
        F.didClickCopy, CB( budgetCopyResult(c.result()); ),
        F.didClickPasteMorningBalance, CB( budgetPasteMorningBalance(); ),
        F.didClickPasteSpent, CB( budgetPasteSpent(); ),
        F.morningBalance, CB( VM.setMorningBalance(c.morningBalance()); ),
        F.result, CB( VM.setResult(c.result()); ),
        */
        F.spent, CB( VM.setSpent(c.spent()); ),
    };
    BudgetEffectRegistry::registerOneliners(KT.budgetCtrl(), oneliners);

    /*
    std::string rd = budgetReportedDate().toStdString();
    budgetSet(std::string(F.reportedDate), rd.c_str());
    budgetSet(std::string(F.reportedWeekday), budgetReportedWeekday());
    */
}

void BudgetComponent::setup() {
    budgetSet(F.didSetup, true);
}

#undef VM
