package top.huzhurong.aop.core;

import org.junit.Assert;
import org.junit.Test;
import top.huzhurong.aop.advisor.Advisor;

import java.util.Arrays;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public class TestUse {
    @Test
    public void test() {
        //1、获取所有的advisor
        TestAspectj testAspectj = new TestAspectj();
        TestAspectj2 testAspectj2 = new TestAspectj2();
        List<Advisor> advisors = AspectjParser.parserAspectj(Arrays.asList(testAspectj, testAspectj2));
        //2、实例化对象
        Bin bin = new Bin();
        //3、后置处理判断bin是不是满足拦截要求
        Bin binProxy = (Bin) AspectjParser.findApplyAdvisor(bin, advisors);
        Assert.assertNotNull(binProxy);
        binProxy.info2();
        //
    }


    @Test
    public void jdkTest() {
        TestAspectj3 testAspectj3 = new TestAspectj3();
        List<Advisor> advisors = AspectjParser.parserAspectj(testAspectj3.getClass(), testAspectj3);
        TestIn testIn = new TestInImpl();
        TestIn testInProxy = (TestIn) AspectjParser.findApplyAdvisor(testIn, advisors);
        Assert.assertNotNull(testInProxy);
        System.out.println(testInProxy.doInfo());
    }
}
