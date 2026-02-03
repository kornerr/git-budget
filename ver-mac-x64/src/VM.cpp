#include "VM.h"

VM::VM() {
    _inputMorningBalanceLabel = "TODO-MorningBalanceLabel"
}

QString VM::inputMorningBalanceLabel() const {
    return _inputMorningBalanceLabel;
}

void VM::setInputMorningBalanceLabel(const QString &value) {
    _inputMorningBalanceLabel = value;
    emit didChangeInputMorningBalanceLabel(value);
}
