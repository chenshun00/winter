package top.huzhurong.ioc.scan.test;

import top.huzhurong.ioc.annotation.Bean;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/8
 */
@Bean("gg")
public class TestScan3 {

    public void test3() {
        System.out.println("----^ invoke TestScan3's test3 method ^----");
    }
}
