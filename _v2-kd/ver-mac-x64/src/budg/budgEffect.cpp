#include <QClipboard>
#include <QGuiApplication>

#include "budgEffect.h"
#include "ignore.kd.h"

void budgCopyResult(const QString &r) {
    QGuiApplication::clipboard()->setText(r);
}

void budgPasteMorningBalance() {
    QString txt = QGuiApplication::clipboard()->text();
    budgSet(F.pastedMorningBalance, txt.toStdString().c_str());
}

void budgPasteSpent() {
    QString txt = QGuiApplication::clipboard()->text();
    budgSet(F.pastedSpent, txt.toStdString().c_str());
}
