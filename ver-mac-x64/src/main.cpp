#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>

#include "App.h"

int main(int argc, char *argv[]) {
    auto myapp = new App();
    QGuiApplication app(argc, argv);
    QQmlApplicationEngine engine;
    QObject::connect(
        &engine,
        &QQmlApplicationEngine::objectCreationFailed,
        &app,
        []() { QCoreApplication::exit(-1) },
        Qt::QueuedConnection
    );
    engine.rootContext()->setContextProperty("myapp", myapp);
    engine.rootContext()->setContextProperty("vm", myapp->vm());
    engine.loadFromModule("gb", "AppView");

    return app.exec();
}
