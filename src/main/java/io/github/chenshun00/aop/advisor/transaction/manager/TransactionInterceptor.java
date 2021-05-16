package io.github.chenshun00.aop.advisor.transaction.manager;

import io.github.chenshun00.aop.advisor.transaction.definition.TransactionDefinition;
import io.github.chenshun00.aop.advisor.transaction.definition.TransactionStatus;
import io.github.chenshun00.aop.invocation.AbstractInvocation;
import io.github.chenshun00.aop.invocation.Invocation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import io.github.chenshun00.aop.advisor.transaction.TransactionAttrSource;
import io.github.chenshun00.aop.advisor.transaction.TransactionManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * @author chenshun00@gmail.com
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

    public Object doTransaction(Invocation invocation, Object object) throws SQLException, InvocationTargetException, IllegalAccessException {
        AbstractInvocation abstractInvocation = (AbstractInvocation) invocation;
        TransactionDefinition definition = TransactionAttrSource.getDefinition(abstractInvocation.getMethod());
        //获取事务
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        log.info("成功获取事务");
        Object invoke;
        try {
            invoke = abstractInvocation.getMethod().invoke(object, abstractInvocation.getArgs());
            commitTransactionAfterReturning(transaction);
        } catch (InvocationTargetException e) {
            log.error("事务处理出现异常:{},开始进行事务回滚", e.getTargetException().getMessage());
            completeTransactionAfterThrowing(transaction);
            throw e;
        } catch (Throwable e) {
            log.error("事务处理出现异常:{},开始进行事务回滚", e.getMessage());
            completeTransactionAfterThrowing(transaction);
            throw e;
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
    private void cleanupTransactionInfo() throws SQLException {
        SyncTransactionUtil.clear();
        ConnectionManager connectionManager = ConnectionManager.get();
        connectionManager.getConnection().close();
        ConnectionManager.remove();
    }

    /**
     * 事务处理过程当中抛出的异常，可能提交，也可能会滚，取决于配置信息
     */
    private void completeTransactionAfterThrowing(TransactionStatus transaction) throws SQLException {
        transactionManager.rollback(transaction);
    }

}
