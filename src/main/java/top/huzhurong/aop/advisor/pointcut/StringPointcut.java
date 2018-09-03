package top.huzhurong.aop.advisor.pointcut;

import java.lang.reflect.Method;

/**
 * execution（modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern（param-pattern）throws-pattern?）
 * ? 表示可以可选，分别是修饰类型 返回值类型 申明包类型 防止的方法名称 参数类型 抛出的异常
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/9/3
 */
public class StringPointcut implements Pointcut {

    /**
     * 切点表达式
     */
    private String pointcutExpression;

    public StringPointcut(String pointcutExpression) {
        this.pointcutExpression = pointcutExpression;
    }

    @Override
    public boolean match(Method method) {
        String name = method.getClass().getName();
        String methodName = method.getName();
        int modifiers = method.getModifiers();
        return false;
    }

    public String getPointcutExpression() {
        return pointcutExpression;
    }

    public void setPointcutExpression(String pointcutExpression) {
        this.pointcutExpression = pointcutExpression;
    }
}
