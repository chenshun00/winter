package top.huzhurong.aop.core;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author chenshun00@gmail.com
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

    @Test
    public void testMethodParam() throws NoSuchMethodException {
        TestParam testParam = new TestParam();
        Method test = testParam.getClass().getDeclaredMethod("test",String.class,Integer.class,Date.class);
        Assert.assertNotNull(test);
        Assert.assertEquals(3,test.getParameterCount());
    }

    private class TestParam{
        public String test(String name, Integer age, Date birthday){
            return "test";
        }
    }
}
