#include <QApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>

#include "budget.h"
#include "ignore.kd.h"
#include "VM.h"

int main(int argc, char *argv[]) {
    // Create Qt application
    QApplication app(argc, argv);
    QQmlApplicationEngine engine;
    QObject::connect(
        &engine,
        &QQmlApplicationEngine::objectCreationFailed,
        &app,
        []() { QCoreApplication::exit(-1); },
        Qt::QueuedConnection
    );

    API api;
    FObj fobj;
    // Create and launch components
    BudgetComponent budget;
    budget.setup();

    // Configure and load QML
    engine.rootContext()->setContextProperty("api", &api);
    engine.rootContext()->setContextProperty("F", &fobj);
    engine.rootContext()->setContextProperty("vm", &VM::singleton());
    engine.loadFromModule("gitbudget", "AppView");

    return app.exec();
}
