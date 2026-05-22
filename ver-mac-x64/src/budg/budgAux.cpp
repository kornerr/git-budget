#include <QDate>

#include "budgAux.h"

QString budgReportedDate() {
    QDate yesterday = QDate::currentDate().addDays(-1);
    return QString("%1.%2")
        .arg(yesterday.day(), 2, 10, QChar(u'0'))
        .arg(yesterday.month(), 2, 10, QChar(u'0'));
}

int budgReportedWeekday() {
    QDate yesterday = QDate::currentDate().addDays(-1);
    return yesterday.dayOfWeek();
}

