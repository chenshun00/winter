package top.huzhurong.xbatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/6
 */
public class SqlSessionInterceptor implements InvocationHandler {

    private SqlSessionFactory sqlSessionFactory;
    private ExecutorType executorType;

    public SqlSessionInterceptor(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.executorType = executorType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1、获取sqlSession

        //2、保存到当前上下文当中

        //3、执行 sqlSession 到方法
        return null;
    }
}
