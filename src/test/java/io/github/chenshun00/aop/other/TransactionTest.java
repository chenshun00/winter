package io.github.chenshun00.aop.other;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.chenshun00.aop.advisor.Advisor;
import io.github.chenshun00.aop.advisor.transaction.TransactionAdvisor;
import io.github.chenshun00.aop.advisor.transaction.manager.JdbcTransactionManager;
import io.github.chenshun00.aop.core.AspectjParser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/2
 */
public class TransactionTest {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true";
    private static final String user = "root";
    private static final String password = "chenshun";

    private TestService testService;
    private DruidDataSource druidDataSource;

    @Before
    public void before() {
        testService = new TestService();
        druidDataSource = new DruidDataSource();
        druidDataSource.setMaxActive(10);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(user);
        druidDataSource.setPassword(password);
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
    }

    @Test
    public void TestTransaction() {

        io.github.chenshun00.aop.other.Test testAdd = io.github.chenshun00.aop.other.Test.builder().id(23).age(23).name("test add again").build();

        TransactionTest transactionTest = new TransactionTest();
        List<Advisor> advisors = AspectjParser.parserAspectj(transactionTest.getClass(), transactionTest);
        for (Advisor advisor : advisors) {
            if (advisor instanceof TransactionAdvisor) {
                TransactionAdvisor transactionAdvisor = (TransactionAdvisor) advisor;
                JdbcTransactionManager transactionManager = (JdbcTransactionManager) transactionAdvisor.getTransactionManager();
                transactionManager.setDataSource(druidDataSource);
            }
        }
        TestService testServiceProxy = (TestService) AspectjParser.findApplyAdvisor(testService, advisors);
        testServiceProxy.addTest(testAdd);
    }
}
