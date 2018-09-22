package top.huzhurong.web.asm;

import org.junit.Test;
import top.huzhurong.ioc.transaction.User;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/22
 */
public class AsmParameterNameDiscoverTest {

    private ParameterNameDiscoverer parameterNameDiscoverer = new AsmParameterNameDiscover();

    @Test
    public void getParameterNames() {
        Method[] declaredMethods = AsmParameterNameDiscoverTest.class.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equalsIgnoreCase("hello")) {
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(declaredMethod);
                assertEquals(5, parameterNames.length);
                assertEquals("[hello, age, localDateTime, file, user]", Arrays.asList(parameterNames).toString());
            }
        }
    }

    @Test
    public void getClassFileName() {
        Class<AsmParameterNameDiscoverTest> asmParameterNameDiscoverTestClass = AsmParameterNameDiscoverTest.class;
        String name = asmParameterNameDiscoverTestClass.getName().replace(".", "/");
        assertEquals("top/huzhurong/web/asm/AsmParameterNameDiscoverTest", name);
    }

    public String hello(String hello, Integer age, LocalDateTime localDateTime, File file, User user) {
        System.out.print(hello + "\t");
        System.out.print(age + "\t");
        System.out.print(localDateTime + "\t");
        System.out.print(file + "\t");
        System.out.println(user);
        return "1";
    }

}