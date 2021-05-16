package io.github.chenshun00.ioc.scan.test;

import io.github.chenshun00.ioc.annotation.Bean;
import io.github.chenshun00.ioc.annotation.Inject;
import io.github.chenshun00.ioc.bean.aware.InitAware;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/8
 */
@Bean("hhh")
public class TestScan1 implements InitAware, AAA {

    @Inject
    private TestScan2 testScan2;

    public void hello() {
        System.out.println("----# start invoke testScan1's hello method #----");
        testScan2.hello();
        System.out.println("----# end invoke testScan1's hello method #----");
    }

    @Override
    public void initBean() {
        System.out.println("----# testScan1's initAware interface #----");
    }

    @Override
    public void gg() {
        testScan2.hello();
    }
}
