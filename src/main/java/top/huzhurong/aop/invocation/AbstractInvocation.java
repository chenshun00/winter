package top.huzhurong.aop.invocation;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/27
 */
public abstract class AbstractInvocation implements Invocation {
    protected int InvocationIndex = -1;

    @Override
    public Object proceed() throws Throwable {
        return null;
    }
}
