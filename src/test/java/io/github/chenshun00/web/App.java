package io.github.chenshun00.web;

import org.junit.Before;
import org.junit.Test;
import io.github.chenshun00.ioc.Winter;
import io.github.chenshun00.ioc.annotation.EnableConfiguration;

/**
 * @author chenshun00@gmail.com
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
