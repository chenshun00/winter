package io.github.chenshun00.ioc.scan.test;

import io.github.chenshun00.ioc.annotation.Bean;
import io.github.chenshun00.ioc.annotation.Inject;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/8
 */
@Bean
public class TestScan2 {

    @Inject("gg")
    private TestScan3 TestScan3;

    public void hello() {
        System.out.println("----$ start invoke TestScan2's hello method $----");
        TestScan3.test3();
        System.out.println("----$ end invoke TestScan2's hello method $----");

    }
}
