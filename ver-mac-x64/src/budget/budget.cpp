#include <any>
#include <vector>

#include "budget.h"
#include "budgetAux.h"
#include "budgetEffect.h"
#include "ignore.kd.h"
#include "VM.h"

#define CB(code) std::make_any<std::function<void(BudgetContext)>>([&](BudgetContext c) { code })
#define VM VM::singleton()

BudgetComponent::BudgetComponent() {
    std::vector<std::any> oneliners = {
        F.didClickCopy, CB( budgetCopyResult(c.result()); ),
        F.didClickPasteMorningBalance, CB( budgetPasteMorningBalance(); ),
        F.didClickPasteSpent, CB( budgetPasteSpent(); ),
        F.morningBalance, CB( VM.setMorningBalance(c.morningBalance()); ),
        F.result, CB( VM.setResult(c.result()); ),
        F.spent, CB( VM.setSpent(c.spent()); ),
    };
    BudgetEffectRegistry::registerOneliners(KT.budgetCtrl(), oneliners);
}

void BudgetComponent::setup() {
    // Defaults
    budgetSet(F.reportedDate, budgetReportedDate());
    budgetSet(F.reportedWeekday, budgetReportedWeekday());
    budgetSet(F.didSetup, true);
}
