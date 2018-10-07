package top.huzhurong.xbatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.*;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.ioc.bean.aware.InitAware;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/6
 */
@Slf4j
public class SqlSessionBean implements SqlSession, InitAware {

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    private SqlSession sqlSessionProxy;

    @Inject
    private SessionKit sessionKit;

    @Override
    public <T> T selectOne(String s) {
        return sqlSessionProxy.selectOne(s);
    }

    @Override
    public <T> T selectOne(String s, Object o) {
        return sqlSessionProxy.selectOne(s, o);
    }

    @Override
    public <E> List<E> selectList(String s) {
        return sqlSessionProxy.selectList(s);
    }

    @Override
    public <E> List<E> selectList(String s, Object o) {
        return sqlSessionProxy.selectList(s, o);

    }

    @Override
    public <E> List<E> selectList(String s, Object o, RowBounds rowBounds) {
        return sqlSessionProxy.selectList(s, o, rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String s, String s1) {
        return this.sqlSessionProxy.selectMap(s, s1);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String s, Object o, String s1) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String s, Object o, String s1, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectMap(s, o, s1, rowBounds);
    }

    @Override
    public <T> Cursor<T> selectCursor(String s) {
        return this.sqlSessionProxy.selectCursor(s);
    }

    @Override
    public <T> Cursor<T> selectCursor(String s, Object o) {
        return this.sqlSessionProxy.selectCursor(s, o);
    }

    @Override
    public <T> Cursor<T> selectCursor(String s, Object o, RowBounds rowBounds) {
        return this.sqlSessionProxy.selectCursor(s, o, rowBounds);
    }

    @Override
    public void select(String s, Object o, ResultHandler resultHandler) {
        this.sqlSessionProxy.select(s, o, resultHandler);
    }

    @Override
    public void select(String s, ResultHandler resultHandler) {
        this.sqlSessionProxy.select(s, resultHandler);
    }

    @Override
    public void select(String s, Object o, RowBounds rowBounds, ResultHandler resultHandler) {
        this.sqlSessionProxy.select(s, o, rowBounds, resultHandler);
    }

    @Override
    public int insert(String s) {
        return this.sqlSessionProxy.insert(s);
    }

    @Override
    public int insert(String s, Object o) {
        return this.sqlSessionProxy.insert(s, o);
    }

    @Override
    public int update(String s) {
        return this.sqlSessionProxy.update(s);
    }

    @Override
    public int update(String s, Object o) {
        return this.sqlSessionProxy.update(s, o);
    }

    @Override
    public int delete(String s) {
        return this.sqlSessionProxy.delete(s);
    }

    @Override
    public int delete(String s, Object o) {
        return this.sqlSessionProxy.delete(s, o);
    }

    @Override
    public void commit() {
        throw new RuntimeException("容器管理事务上下文");
    }

    @Override
    public void commit(boolean b) {
        throw new RuntimeException("容器管理事务上下文");
    }

    @Override
    public void rollback() {
        throw new RuntimeException("容器管理事务上下文");
    }

    @Override
    public void rollback(boolean b) {
        throw new RuntimeException("容器管理事务上下文");
    }

    @Override
    public List<BatchResult> flushStatements() {
        return null;
    }

    @Override
    public void close() {
        throw new RuntimeException("容器管理事务上下文");
    }

    @Override
    public void clearCache() {
        this.sqlSessionProxy.clearCache();
    }

    @Override
    public Configuration getConfiguration() {
        return this.sqlSessionFactory.getConfiguration();
    }

    @Override
    public <T> T getMapper(Class<T> aClass) {
        return getConfiguration().getMapper(aClass, this);
    }

    @Override
    public Connection getConnection() {
        return this.sqlSessionProxy.getConnection();
    }

    @Override
    public void initBean() {
        this.sqlSessionProxy = (SqlSession) newProxyInstance(SqlSessionFactory.class.getClassLoader(),
                new Class[]{SqlSession.class}, new SqlSessionInterceptor(this.sqlSessionFactory, ExecutorType.SIMPLE, this.sessionKit));
        log.info("init sqlSessionProxy");
        if (this.sqlSessionProxy == null) {
            throw new RuntimeException("sqlSessionProxy init failure");
        }
    }
}
