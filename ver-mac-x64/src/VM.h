#ifndef GB_VM_H
#define GB_VM_H

#include <QObject>
#include <QString>

class VM: public QObject {
    Q_OBJECT

    Q_PROPERTY(
        bool mainIsVisible
        READ mainIsVisible
        WRITE mainSetIsVisible
        NOTIFY mainDidChangeIsVisible
    )
    Q_PROPERTY(QString spent READ spent WRITE setSpent NOTIFY spentChanged)
    Q_PROPERTY(
        QString morningBalance
        READ morningBalance
        WRITE setMorningBalance
        NOTIFY morningBalanceChanged
    )
    Q_PROPERTY(QString result READ result WRITE setResult NOTIFY resultChanged)

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
        QString spent() const;
        QString morningBalance() const;
        QString result() const;

    public slots:
        void mainSetIsVisible(bool value);
        void setSpent(const QString &value);
        void setMorningBalance(const QString &value);
        void setResult(const QString &value);

    signals:
        void mainDidChangeIsVisible(bool value);
        void spentChanged();
        void morningBalanceChanged();
        void resultChanged();

    private:
        bool _mainIsVisible;
        QString _spent;
        QString _morningBalance;
        QString _result;
};

#endif // GB_VM_H
