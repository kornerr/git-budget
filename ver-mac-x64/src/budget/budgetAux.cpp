#include <QDate>

#include "budgetAux.h"

QString budgetReportedDate() {
    QDate yesterday = QDate::currentDate().addDays(-1);
    return QString("%1.%2")
        .arg(yesterday.day(), 2, 10, QChar(u'0'))
        .arg(yesterday.month(), 2, 10, QChar(u'0'));
}

int budgetReportedWeekday() {
    QDate yesterday = QDate::currentDate().addDays(-1);
    return yesterday.dayOfWeek();
}

