#ifndef GB_VM_H
#define GB_VM_H

#include <QObject>
#include <QString>

class VM: public QObject {
    Q_OBJECT

    Q_PROPERTY(QString morningBalance READ morningBalance WRITE setMorningBalance NOTIFY morningBalanceChanged)
    Q_PROPERTY(QString result READ result WRITE setResult NOTIFY resultChanged)
    Q_PROPERTY(QString spent READ spent WRITE setSpent NOTIFY spentChanged)

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
        QString morningBalance() const;
        QString result() const;
        QString spent() const;

    public slots:
        void setMorningBalance(const QString &value);
        void setResult(const QString &value);
        void setSpent(const QString &value);

    signals:
        void morningBalanceChanged();
        void resultChanged();
        void spentChanged();

    private:
        QString _morningBalance;
        QString _result;
        QString _spent;
};

#endif // GB_VM_H
