package top.huzhurong.aop.advisor.transaction.definition;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/31
 */
public class DefaultTransactionStatus extends AbstractTransactionStatus {

    private final Object transaction;

    private final boolean newTransaction;

    private final boolean readOnly;

    public DefaultTransactionStatus(Object transaction, boolean newTransaction, boolean readOnly) {
        this.transaction = transaction;
        this.newTransaction = newTransaction;
        this.readOnly = readOnly;
    }

    public Object getTransaction() {
        return this.transaction;
    }

    public boolean hasTransaction() {
        return (this.transaction != null);
    }

    @Override
    public boolean isNewTransaction() {
        return (hasTransaction() && this.newTransaction);
    }

    @Override
    public boolean isGlobalRollbackOnly() {
        return true;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

}
