package top.huzhurong.aop.advisor.pointcut;

import top.huzhurong.aop.advisor.Advisor;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/3
 */
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}
