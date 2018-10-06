package top.huzhurong.xbatis;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/6
 */
public class SqlSessionBean implements SqlSession {

    private final SqlSessionFactory sqlSessionFactory;

    private final SqlSession sqlSessionProxy;

    private final ExecutorType executorType;

    public SqlSessionBean(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.executorType = executorType;
        this.sqlSessionProxy = (SqlSession) newProxyInstance(SqlSessionFactory.class.getClassLoader(),
                new Class[]{SqlSession.class}, new SqlSessionInterceptor(this.sqlSessionFactory, this.executorType));
    }

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
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String s, Object o, String s1) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String s, Object o, String s1, RowBounds rowBounds) {
        return null;
    }

    @Override
    public <T> Cursor<T> selectCursor(String s) {
        return null;
    }

    @Override
    public <T> Cursor<T> selectCursor(String s, Object o) {
        return null;
    }

    @Override
    public <T> Cursor<T> selectCursor(String s, Object o, RowBounds rowBounds) {
        return null;
    }

    @Override
    public void select(String s, Object o, ResultHandler resultHandler) {

    }

    @Override
    public void select(String s, ResultHandler resultHandler) {

    }

    @Override
    public void select(String s, Object o, RowBounds rowBounds, ResultHandler resultHandler) {

    }

    @Override
    public int insert(String s) {
        return 0;
    }

    @Override
    public int insert(String s, Object o) {
        return 0;
    }

    @Override
    public int update(String s) {
        return 0;
    }

    @Override
    public int update(String s, Object o) {
        return 0;
    }

    @Override
    public int delete(String s) {
        return 0;
    }

    @Override
    public int delete(String s, Object o) {
        return 0;
    }

    @Override
    public void commit() {

    }

    @Override
    public void commit(boolean b) {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void rollback(boolean b) {

    }

    @Override
    public List<BatchResult> flushStatements() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void clearCache() {

    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public <T> T getMapper(Class<T> aClass) {
        return null;
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
