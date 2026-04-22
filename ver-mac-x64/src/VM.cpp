#include "VM.h"

VM::VM() : QObject() {
    _result = QStringLiteral("TODO-Result");
}

QString VM::morningBalance() const {
    return _morningBalance;
}

QString VM::result() const {
    return _result;
}

QString VM::spent() const {
    return _spent;
}

void VM::setMorningBalance(const QString &value) {
    if (_morningBalance == value) {
        return;
    }
    _morningBalance = value;
    emit morningBalanceChanged();
}

void VM::setResult(const QString &value) {
    if (_result == value) {
        return;
    }
    _result = value;
    emit resultChanged();
}

void VM::setSpent(const QString &value) {
    if (_spent == value) {
        return;
    }
    _spent = value;
    emit spentChanged();
}
