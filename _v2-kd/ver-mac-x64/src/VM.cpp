#include "VM.h"

VM::VM() : QObject() {
    _morningBalancePasteTitle = QStringLiteral("Paste");
    _morningBalancePlaceholder = QStringLiteral("Morning balance");
    _result = QStringLiteral("TODO-Result");
    _resultCopyTitle = QStringLiteral("Copy");
    _spentPasteTitle = QStringLiteral("Paste");
    _spentPlaceholder = QStringLiteral("Spent");
}

QString VM::morningBalance() const {
    return _morningBalance;
}

QString VM::morningBalancePasteTitle() const {
    return _morningBalancePasteTitle;
}

QString VM::morningBalancePlaceholder() const {
    return _morningBalancePlaceholder;
}

QString VM::result() const {
    return _result;
}

QString VM::resultCopyTitle() const {
    return _resultCopyTitle;
}

QString VM::spent() const {
    return _spent;
}

QString VM::spentPasteTitle() const {
    return _spentPasteTitle;
}

QString VM::spentPlaceholder() const {
    return _spentPlaceholder;
}

void VM::setMorningBalance(const QString &value) {
    if (_morningBalance == value) {
        return;
    }
    _morningBalance = value;
    emit morningBalanceChanged();
}

void VM::setMorningBalancePasteTitle(const QString &value) {
    if (_morningBalancePasteTitle == value) {
        return;
    }
    _morningBalancePasteTitle = value;
    emit morningBalancePasteTitleChanged();
}

void VM::setMorningBalancePlaceholder(const QString &value) {
    if (_morningBalancePlaceholder == value) {
        return;
    }
    _morningBalancePlaceholder = value;
    emit morningBalancePlaceholderChanged();
}

void VM::setResult(const QString &value) {
    if (_result == value) {
        return;
    }
    _result = value;
    emit resultChanged();
}

void VM::setResultCopyTitle(const QString &value) {
    if (_resultCopyTitle == value) {
        return;
    }
    _resultCopyTitle = value;
    emit resultCopyTitleChanged();
}

void VM::setSpent(const QString &value) {
    if (_spent == value) {
        return;
    }
    _spent = value;
    emit spentChanged();
}

void VM::setSpentPasteTitle(const QString &value) {
    if (_spentPasteTitle == value) {
        return;
    }
    _spentPasteTitle = value;
    emit spentPasteTitleChanged();
}

void VM::setSpentPlaceholder(const QString &value) {
    if (_spentPlaceholder == value) {
        return;
    }
    _spentPlaceholder = value;
    emit spentPlaceholderChanged();
}
