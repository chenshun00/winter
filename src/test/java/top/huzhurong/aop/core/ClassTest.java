package top.huzhurong.aop.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/28
 */
public class ClassTest {

    @Test
    public void testNoInterfaceClass() {
        Bin bin = new Bin();
        Class<?>[] interfaces = bin.getClass().getInterfaces();
        Assert.assertNotNull(interfaces);
        Assert.assertEquals(interfaces.length, 0);
    }

    @Test
    public void testSuperClass() {
        Bin bin = new Bin();
        Assert.assertEquals(bin.getClass().getSuperclass(), Object.class);
    }

    @Test
    public void testhaveInterface() {
        TestIn testIn = new TestInImpl();
        Class<?>[] interfaces = testIn.getClass().getInterfaces();
        Assert.assertEquals(interfaces.length, 1);
    }
}
