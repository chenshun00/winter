package top.huzhurong.aop.advisor.transaction.definition;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/31
 */
public class DefaultTransactionDefinition implements TransactionDefinition {

    //默认的传播行为
    private int propagation = PROPAGATION_REQUIRED;
    //默认的隔离级别
    private int isolation = ISOLATION_READ_UNCOMMITTED;
    //默认超时
    private int timeout = TIMEOUT_DEFAULT;
    //默认只读
    private boolean readonly = false;

    @Override
    public int getPropagationBehavior() {
        return propagation;
    }

    public void setPropagation(int propagation) {
        this.propagation = propagation;
    }

    public void setIsolationLevel(int isolation) {
        this.isolation = isolation;
    }

    @Override
    public int getIsolationLevel() {
        return isolation;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public boolean isReadOnly() {
        return readonly;
    }
}
