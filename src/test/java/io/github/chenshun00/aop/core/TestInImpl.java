package io.github.chenshun00.aop.core;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/27
 */
public class TestInImpl implements TestIn {
    @Override
    public String doInfo() {
        System.out.println("运行被拦截方法");
        return "doInfo";
    }
}
