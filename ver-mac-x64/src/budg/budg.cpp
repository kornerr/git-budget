#include <any>
#include <vector>

#include "budg.h"
#include "budgAux.h"
#include "budgEffect.h"
#include "ignore.kd.h"
#include "VM.h"

#define CB(code) std::make_any<std::function<void(BudgetContext)>>([&](BudgetContext c) { code })
#define VM VM::singleton()

BudgComponent::BudgComponent() {
    std::vector<std::any> oneliners = {
        F.didClickCopy, CB( budgCopyResult(c.result()); ),
        F.didClickPasteMorningBalance, CB( budgPasteMorningBalance(); ),
        F.didClickPasteSpent, CB( budgPasteSpent(); ),
        F.morningBalance, CB( VM.setMorningBalance(c.morningBalance()); ),
        F.result, CB( VM.setResult(c.result()); ),
        F.spent, CB( VM.setSpent(c.spent()); ),
    };
    BudgEffectRegistry::registerOneliners(KT.budgCtrl(), oneliners);
}

void BudgComponent::setup() {
    // Defaults
    budgSet(F.reportedDate, budgReportedDate());
    budgSet(F.reportedWeekday, budgReportedWeekday());
    budgSet(F.didSetup, true);
}
