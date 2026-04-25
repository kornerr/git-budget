#include <QClipboard>
#include <QGuiApplication>

#include "budgetEffects.h"
#include "ignore.kd.h"

void budgetCopyResult(const QString &r) {
    QGuiApplication::clipboard()->setText(r);
}

void budgetPasteMorningBalance() {
    QString txt = QGuiApplication::clipboard()->text();
    budgetSet(F.pastedMorningBalance, txt.toStdString().c_str());
}

