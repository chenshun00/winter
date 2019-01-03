package top.huzhurong.aop.advisor.pointcut;

import org.junit.Assert;
import org.junit.Test;
import top.huzhurong.aop.core.Bin;
import top.huzhurong.aop.core.TestIn;
import top.huzhurong.aop.core.TestInImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * @author chenshun00@gmail.com
 * @since 2018/9/4
 */
public class StringPointcutTest {

    @Test
    public void testModifier() throws NoSuchMethodException {
        int publicModifier = 1;
        Assert.assertTrue(Modifier.isPublic(publicModifier));

        int privateModifier = 2;
        Assert.assertTrue(Modifier.isPrivate(privateModifier));

        TestIn testIn = new TestInImpl();
        Method doInfo = testIn.getClass().getDeclaredMethod("doInfo");
        Assert.assertEquals(TestInImpl.class, doInfo.getDeclaringClass());
        Assert.assertEquals(1, doInfo.getModifiers());
    }

    @Test
    public void testMethodInfo() throws NoSuchMethodException {
        Method testMethodInfo = TestInfo.class.getDeclaredMethod("hello", String.class, String.class, int.class);
        Assert.assertTrue(Modifier.isPublic(testMethodInfo.getModifiers()));
        Assert.assertEquals("hello", testMethodInfo.getName());
        Assert.assertEquals("top.huzhurong.aop.advisor.pointcut.TestInfo", testMethodInfo.getDeclaringClass().getName());

        Class<?> returnType = testMethodInfo.getReturnType();
        Assert.assertEquals(int.class, returnType);
        Assert.assertEquals("String", String.class.getName().substring(String.class.getName().lastIndexOf(".") + 1));
        Assert.assertEquals("int", int.class.getName().substring(int.class.getName().lastIndexOf(".") + 1));
        Assert.assertEquals("TestInfo", TestInfo.class.getName().substring(TestInfo.class.getName().lastIndexOf(".") + 1));

        String param = ")";
        Assert.assertEquals("", param.substring(1));
    }

    @Test
    public void parseString() {
        String className = "top.huzhurong.aop.advisor.pointcut.StringPointcutTest";
        Assert.assertEquals("top.huzhurong.aop.advisor.pointcut", className.substring(0, className.lastIndexOf(".")));
        Assert.assertEquals("StringPointcutTest", className.substring(className.lastIndexOf(".") + 1));

        Assert.assertNotEquals(Integer.class, int.class);
    }

    @Test
    public void testMatch() throws NoSuchMethodException {
        StringPointcut pointcut = new StringPointcut("public int top.huzhurong.aop.advisor.pointcut.TestInfo hello(String,String,int)");
        Method testMethodInfo = TestInfo.class.getDeclaredMethod("hello", String.class, String.class, int.class);
        Assert.assertTrue(pointcut.match(testMethodInfo));


        StringPointcut pointcut2 = new StringPointcut("public void top.huzhurong.aop.core.Bin info2()");
        Method testMethodInfo2 = Bin.class.getDeclaredMethod("info2");
        Assert.assertTrue(pointcut2.match(testMethodInfo2));
    }

}