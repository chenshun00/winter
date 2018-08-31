package top.huzhurong.aop.advisor.transaction.definition;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/31
 */
public class DefaultTransactionDefinition implements TransactionDefinition {

    @Override
    public int getPropagationBehavior() {
        return PROPAGATION_REQUIRED;
    }

    @Override
    public int getIsolationLevel() {
        return ISOLATION_READ_UNCOMMITTED;
    }

    @Override
    public int getTimeout() {
        return TIMEOUT_DEFAULT;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
