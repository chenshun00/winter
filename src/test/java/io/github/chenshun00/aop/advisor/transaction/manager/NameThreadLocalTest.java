package io.github.chenshun00.aop.advisor.transaction.manager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/29
 */
public class NameThreadLocalTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNameThreadLocal() {
        ThreadLocal<String> nameThreadLocal = new NameThreadLocal<>("testThreadLocal");
        assertEquals("testThreadLocal", nameThreadLocal.toString());
        nameThreadLocal.set("info");
        String info = nameThreadLocal.get();
        Assert.assertEquals("info", info);
        nameThreadLocal.remove();
        String s = nameThreadLocal.get();
        assertNull(s);
    }
}