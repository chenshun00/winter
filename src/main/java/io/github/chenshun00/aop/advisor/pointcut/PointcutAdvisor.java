package io.github.chenshun00.aop.advisor.pointcut;

import io.github.chenshun00.aop.advisor.Advisor;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/3
 */
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}
