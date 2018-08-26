package top.huzhurong.aop.core;

import org.junit.Test;
import top.huzhurong.aop.advisor.Advisor;

import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class TestUse {
    @Test
    public void test() throws InstantiationException, IllegalAccessException {
        //1、获取所有的advisor
//        List<Advisor> advisors = AspectjParser.parserAspectj(TestAspectj2.class);
        List<Advisor> advisors = AspectjParser.parserAspectj(TestAspectj.class);
        advisors.addAll(AspectjParser.parserAspectj(TestAspectj2.class));
        //2、实例化对象
        Bin bin = new Bin();
        //3、后置处理判断bin是不是满足拦截要求
        Bin binProxy = (Bin) AspectjParser.findApplyAdvisor(bin, advisors);
        binProxy.other();
        //
    }
}
