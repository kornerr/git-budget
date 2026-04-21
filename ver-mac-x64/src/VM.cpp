#include "VM.h"

VM::VM() : QObject() {
    _mainIsVisible = false;
}

bool VM::mainIsVisible() const {
    return _mainIsVisible;
}

void VM::mainSetIsVisible(bool value) {
    _mainIsVisible = value;
    emit mainDidChangeIsVisible(value);
}
