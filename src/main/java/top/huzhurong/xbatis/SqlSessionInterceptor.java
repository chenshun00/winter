package top.huzhurong.xbatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/6
 */
public class SqlSessionInterceptor implements InvocationHandler {


    private SqlSessionFactory sqlSessionFactory;
    private ExecutorType executorType;
    private SessionKit sessionKit;

    public SqlSessionInterceptor(SqlSessionFactory sqlSessionFactory, ExecutorType executorType, SessionKit sessionKit) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.executorType = executorType;
        this.sessionKit = sessionKit;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1、获取sqlSession
        SqlSession sqlSession = sessionKit.getSqlSession(sqlSessionFactory, executorType);
        //2、保存到当前上下文当中
        //3、执行 sqlSession 到方法
        Object object;
        try {
            object = method.invoke(sqlSession, args);
            return object;
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
