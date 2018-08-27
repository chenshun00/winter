package top.huzhurong.aop.invocation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import top.huzhurong.aop.advisor.Advisor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/27
 */
@Data
public class JdkProxy implements InvocationHandler {


    private Object target;
    private List<Advisor> advisors;

    public JdkProxy(Object target, List<Advisor> advisors) {
        this.target = target;
        this.advisors = advisors;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return new JdkInvocation(target, method, args, advisors).proceed();
    }
}
