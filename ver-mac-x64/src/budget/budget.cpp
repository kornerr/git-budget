#include <any>
#include <cstdio>
#include <string>
#include <vector>
#include <functional>

#include <QClipboard>
#include <QDate>
#include <QGuiApplication>
#include <QString>

#include "budget.h"
#include "ignore.kd.h"
#include "KT.h"
#include "VM.h"

#include "libgb_api.h"

#define CB(code) std::make_any<std::function<void(BudgetContext)>>([&](BudgetContext c) { code })

#define VM VM::singleton()

namespace {

QString budgetReportedDate() {
    QDate yesterday = QDate::currentDate().addDays(-1);
    return QStringLiteral("%1.%2")
        .arg(yesterday.day(), 2, 10, QChar(u'0'))
        .arg(yesterday.month(), 2, 10, QChar(u'0'));
}

int budgetReportedWeekday() {
    QDate yesterday = QDate::currentDate().addDays(-1);
    return yesterday.dayOfWeek();
}

void budgetCopyResult(const QString &r) {
    QGuiApplication::clipboard()->setText(r);
}

void budgetPasteMorningBalance() {
    QString txt = QGuiApplication::clipboard()->text();
    if (txt.isEmpty()) {
        txt = QStringLiteral("N/A");
    }
    std::string s = txt.toStdString();
    budgetSet(std::string(F.pastedMorningBalance), s.c_str());
}

void budgetPasteSpent() {
    QString txt = QGuiApplication::clipboard()->text();
    if (txt.isEmpty()) {
        txt = QStringLiteral("N/A");
    }
    std::string s = txt.toStdString();
    budgetSet(std::string(F.pastedSpent), s.c_str());
}

} // namespace

// Int values for Kotlin KD (reportedWeekday); bool/string specializations live in ignore.kd.cpp.
template<> void budgetSet(const std::string &key, int value) {
    libgb_kref_kotlin_Int kint =
        libgb_symbols()->createNullableInt(static_cast<libgb_KInt>(value));
    libgb_kref_kotlin_Any asAny;
    asAny.pinned = kint.pinned;
    KT.KDController.set(KT.budgetCtrl(), key.c_str(), asAny);
}

BudgetComponent::BudgetComponent() {
    std::vector<std::any> oneliners = {
        std::make_any<const char *>(F.didClickCopy),
        CB(budgetCopyResult(c.result());),
        std::make_any<const char *>(F.didClickPasteMorningBalance),
        CB(budgetPasteMorningBalance();),
        std::make_any<const char *>(F.didClickPasteSpent),
        CB(budgetPasteSpent();),
        std::make_any<const char *>(F.morningBalance),
        CB(VM.setMorningBalance(c.morningBalance());),
        std::make_any<const char *>(F.spent),
        CB(VM.setSpent(c.spent());),
        std::make_any<const char *>(F.result),
        CB(VM.setResult(c.result());),
    };
    BudgetEffectRegistry::registerOneliners(KT.budgetCtrl(), oneliners);

    std::string rd = budgetReportedDate().toStdString();
    budgetSet(std::string(F.reportedDate), rd.c_str());
    budgetSet(std::string(F.reportedWeekday), budgetReportedWeekday());
}

void BudgetComponent::setup() {
    budgetSet(std::string(F.didSetup), true);
}

#undef VM
