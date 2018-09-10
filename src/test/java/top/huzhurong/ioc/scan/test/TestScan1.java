package top.huzhurong.ioc.scan.test;

import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.annotation.Inject;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/8
 */
@Bean("hhh")
public class TestScan1 {

    @Inject
    private TestScan2 testScan2;

    public void setTestScan2(TestScan2 testScan2) {
        this.testScan2 = testScan2;
    }

    public void hi() {
        System.out.println("----# hi TestScan1 #----");
    }

    public void hello() {
        System.out.println("----# start invoke testScan2's hello method #----");
        testScan2.hello();
        System.out.println("----# end invoke testScan2's hello method #----");
    }

}
