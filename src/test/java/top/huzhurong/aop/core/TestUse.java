package top.huzhurong.aop.core;

import org.junit.Assert;
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
        TestAspectj testAspectj = new TestAspectj();
        List<Advisor> advisors = AspectjParser.parserAspectj(testAspectj.getClass());
//        advisors.addAll(AspectjParser.parserAspectj(TestAspectj2.class));
        //2、实例化对象
        Bin bin = new Bin();
        //3、后置处理判断bin是不是满足拦截要求
        Bin binProxy = (Bin) AspectjParser.findApplyAdvisor(bin, advisors, testAspectj);
        Assert.assertNotNull(binProxy);
        System.out.println(binProxy);
        binProxy.info2();
        //
    }


    @Test
    public void jdkTest() throws InstantiationException, IllegalAccessException {
        TestAspectj3 testAspectj3 = new TestAspectj3();
        List<Advisor> advisors = AspectjParser.parserAspectj(testAspectj3.getClass());
        TestIn testIn = new TestInImpl();
        TestIn testInProxy = (TestIn) AspectjParser.findApplyAdvisor(testIn, advisors, testAspectj3);
        assert testInProxy != null;
        System.out.println(testInProxy.doInfo());
    }
}
