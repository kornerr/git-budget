#include <QClipboard>
#include <QGuiApplication>

#include "budgetEffects.h"

void budgetCopyResult(const QString &r) {
    QGuiApplication::clipboard()->setText(r);
}
