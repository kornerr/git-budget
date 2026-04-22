#include "VM.h"

VM::VM() : QObject() {
    _mainIsVisible = false;
    _result = QStringLiteral("TODO-Result");
}

bool VM::mainIsVisible() const {
    return _mainIsVisible;
}

QString VM::spent() const {
    return _spent;
}

QString VM::morningBalance() const {
    return _morningBalance;
}

QString VM::result() const {
    return _result;
}

void VM::mainSetIsVisible(bool value) {
    _mainIsVisible = value;
    emit mainDidChangeIsVisible(value);
}

void VM::setSpent(const QString &value) {
    if (_spent == value) {
        return;
    }
    _spent = value;
    emit spentChanged();
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
