#ifndef GB_VM_H
#define GB_VM_H

#include <QObject>
#include <QString>

class VM: public QObject {
    Q_OBJECT

    Q_PROPERTY(QString morningBalance READ morningBalance WRITE setMorningBalance NOTIFY morningBalanceChanged)
    Q_PROPERTY(QString morningBalancePasteTitle READ morningBalancePasteTitle WRITE setMorningBalancePasteTitle NOTIFY morningBalancePasteTitleChanged)
    Q_PROPERTY(QString morningBalancePlaceholder READ morningBalancePlaceholder WRITE setMorningBalancePlaceholder NOTIFY morningBalancePlaceholderChanged)
    Q_PROPERTY(QString result READ result WRITE setResult NOTIFY resultChanged)
    Q_PROPERTY(QString spent READ spent WRITE setSpent NOTIFY spentChanged)
    Q_PROPERTY(QString spentPasteTitle READ spentPasteTitle WRITE setSpentPasteTitle NOTIFY spentPasteTitleChanged)
    Q_PROPERTY(QString spentPlaceholder READ spentPlaceholder WRITE setSpentPlaceholder NOTIFY spentPlaceholderChanged)

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
        QString morningBalancePasteTitle() const;
        QString morningBalancePlaceholder() const;
        QString result() const;
        QString spent() const;
        QString spentPasteTitle() const;
        QString spentPlaceholder() const;

    public slots:
        void setMorningBalance(const QString &value);
        void setMorningBalancePasteTitle(const QString &value);
        void setMorningBalancePlaceholder(const QString &value);
        void setResult(const QString &value);
        void setSpent(const QString &value);
        void setSpentPasteTitle(const QString &value);
        void setSpentPlaceholder(const QString &value);

    signals:
        void morningBalanceChanged();
        void morningBalancePasteTitleChanged();
        void morningBalancePlaceholderChanged();
        void resultChanged();
        void spentChanged();
        void spentPasteTitleChanged();
        void spentPlaceholderChanged();

    private:
        QString _morningBalance;
        QString _morningBalancePasteTitle;
        QString _morningBalancePlaceholder;
        QString _result;
        QString _spent;
        QString _spentPasteTitle;
        QString _spentPlaceholder;
};

#endif // GB_VM_H
