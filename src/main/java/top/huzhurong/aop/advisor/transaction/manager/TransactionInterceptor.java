package top.huzhurong.aop.advisor.transaction.manager;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.advisor.transaction.TransactionAttrSource;
import top.huzhurong.aop.advisor.transaction.TransactionManager;
import top.huzhurong.aop.advisor.transaction.definition.TransactionDefinition;
import top.huzhurong.aop.advisor.transaction.definition.TransactionStatus;
import top.huzhurong.aop.invocation.AbstractInvocation;
import top.huzhurong.aop.invocation.Invocation;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/1
 */
@Slf4j
public class TransactionInterceptor {

    @Getter
    @Setter
    private TransactionManager transactionManager;

    public TransactionInterceptor(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object doTransaction(Invocation invocation, Object object) throws SQLException {
        AbstractInvocation abstractInvocation = (AbstractInvocation) invocation;
        TransactionDefinition definition = TransactionAttrSource.getDefinition(abstractInvocation.getMethod());
        //获取事务
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        log.info("成功获取事务");
        Object invoke = null;
        try {
            invoke = abstractInvocation.getMethod().invoke(object, abstractInvocation.getArgs());
            commitTransactionAfterReturning(transaction);
        } catch (InvocationTargetException e) {
            log.error("事务处理出现异常:{},开始进行事务回滚", e.getTargetException().getMessage());
            completeTransactionAfterThrowing(transaction);
            e.printStackTrace();
        } catch (Throwable e) {
            log.error("事务处理出现异常:{},开始进行事务回滚", e.getMessage());
            completeTransactionAfterThrowing(transaction);
            e.printStackTrace();
        } finally {
            cleanupTransactionInfo();
        }
        return invoke;
    }

    //事务成功完成，开始提交
    private void commitTransactionAfterReturning(TransactionStatus transaction) throws SQLException {
        transactionManager.commit(transaction);
    }

    //移除绑定在线程上的事务属性
    private void cleanupTransactionInfo() {
        SyncTransactionUtil.clear();
        ConnectionManager.remove();
    }

    /**
     * 事务处理过程当中抛出的异常，可能提交，也可能会滚，取决于配置信息
     */
    private void completeTransactionAfterThrowing(TransactionStatus transaction) throws SQLException {
        transactionManager.rollback(transaction);
    }

}
