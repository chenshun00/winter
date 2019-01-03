package top.huzhurong.aop.advisor;

import top.huzhurong.aop.advisor.pointcut.PointcutAdvisor;
import top.huzhurong.aop.invocation.Invocation;

/**
 * 调用增强器方法
 *
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public interface MethodInterceptor extends PointcutAdvisor {

    Object invoke(Invocation invocation) throws Throwable;

}
