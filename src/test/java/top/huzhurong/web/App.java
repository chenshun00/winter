package top.huzhurong.web;

import org.junit.Before;
import org.junit.Test;
import top.huzhurong.ioc.Winter;
import top.huzhurong.ioc.annotation.EnableConfiguration;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/21
 */
@EnableConfiguration
public class App {

    private Winter winter;

    @Before
    public void before() {
        winter = new Winter(App.class);
    }

    @Test
    public void testFirst() {
        winter.start();
    }

}
