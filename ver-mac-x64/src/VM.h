#ifndef GB_VM_H
#define GB_VM_H

#include <QObject>

class VM: public QObject {
    Q_OBJECT

    Q_PROPERTY(QString inputMorningBalanceLabel READ inputMorningBalanceLabel WRITE setInputMorningBalanceLabel NOTIFY didChangeInputMorningBalanceLabel)

    public:
        VM()
        virtual ~VM() { }
      
        QString inputMorningBalanceLabel() const;

    public slots:
        void setInputMorningBalanceLabel(const QString &value);

    signals:
        void didChangeInputMorningBalanceLabel(const QString &value);

    private:
        QString _inputMorningBalanceLabel;
}

#endif // GB_VM_H
