#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>

#include "ignore.kd.h"
#include "budget.h"
#include "VM.h"

int main(int argc, char *argv[]) {
    // Create Qt application
    QGuiApplication app(argc, argv);
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
    BudgetComponent m;
    m.setup();

    // Configure and load QML
    engine.rootContext()->setContextProperty("api", &api);
    engine.rootContext()->setContextProperty("F", &fobj);
    engine.rootContext()->setContextProperty("vm", &VM::singleton());
    engine.loadFromModule("gitbudget", "MainView");
    engine.loadFromModule("gitbudget", "AppView");

    return app.exec();
}
