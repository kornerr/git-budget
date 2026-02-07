#ifndef GB_APP_H
#define GB_APP_H

#include <QObject>

#include "budget.h"
#include "VM.h"

class App: public QObject {
    Q_OBJECT

    public:
        App();
        virtual ~App();

        VM* vm();

    private:
        VM *_vm;
};

#endif // GB_APP_H
