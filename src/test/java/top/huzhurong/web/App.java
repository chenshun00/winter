package top.huzhurong.web;

import org.junit.Before;
import org.junit.Test;
import top.huzhurong.ioc.Init;
import top.huzhurong.ioc.annotation.EnableConfiguration;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/21
 */
@EnableConfiguration
public class App {

    private Init init;

    @Before
    public void before() {
        init = new Init(App.class);
    }

    @Test
    public void testFirst() {
        init.instantiation();
    }

}
