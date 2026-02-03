#include "App.h"

App::App() {
    _vm = new VM();
}

App::~App() {
    delete _vm;
}

VM* App::vm() {
    return _vm;
}
