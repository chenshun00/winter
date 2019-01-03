package top.huzhurong.aop.advisor.transaction.definition;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/31
 */
public abstract class AbstractTransactionStatus implements TransactionStatus {

    private boolean rollbackOnly = false;
    private boolean completed = false;

    @Override
    public void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    @Override
    public boolean isRollbackOnly() {
        return (isLocalRollbackOnly() || isGlobalRollbackOnly());
    }

    public boolean isGlobalRollbackOnly() {
        return false;
    }


    public boolean isLocalRollbackOnly() {
        return this.rollbackOnly;
    }


    public void setCompleted() {
        this.completed = true;
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }
}
