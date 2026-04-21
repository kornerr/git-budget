#ifndef GB_VM_H
#define GB_VM_H

#include <QObject>

class VM: public QObject {
    Q_OBJECT

    Q_PROPERTY(
        bool mainIsVisible
        READ mainIsVisible
        WRITE mainSetIsVisible
        NOTIFY mainDidChangeIsVisible
    )

    private:
        VM();

    public:
        VM(VM const &) = delete;
        void operator=(VM const &) = delete;
        virtual ~VM() { }
        static VM &singleton() {
            static VM instance;
            return instance;
        }

    public:
        bool mainIsVisible() const;

    public slots:
        void mainSetIsVisible(bool value);

    signals:
        void mainDidChangeIsVisible(bool value);

    private:
        bool _mainIsVisible;
};

#endif // GB_VM_H
