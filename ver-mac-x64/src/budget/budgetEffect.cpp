#include <QClipboard>
#include <QGuiApplication>

#include "budgetEffect.h"
#include "ignore.kd.h"

void budgetCopyResult(const QString &r) {
    QGuiApplication::clipboard()->setText(r);
}

void budgetPasteMorningBalance() {
    QString txt = QGuiApplication::clipboard()->text();
    budgetSet(F.pastedMorningBalance, txt.toStdString().c_str());
}

void budgetPasteSpent() {
    QString txt = QGuiApplication::clipboard()->text();
    budgetSet(F.pastedSpent, txt.toStdString().c_str());
}
