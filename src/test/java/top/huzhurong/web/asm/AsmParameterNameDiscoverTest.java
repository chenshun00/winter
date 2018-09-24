package top.huzhurong.web.asm;

import org.junit.Test;
import top.huzhurong.ioc.transaction.User;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

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
                Map<String, String> parameterNames = parameterNameDiscoverer.getParameterNames(declaredMethod);
                assertEquals(5, parameterNames.size());
                assertEquals("[{hello=java.lang.String, " +
                                "age=short, " +
                                "localDateTime=java.time.LocalDateTime, " +
                                "file=java.io.File, " +
                                "user=top.huzhurong.ioc.transaction.User}]",
                        Collections.singletonList(parameterNames).toString());
            }
        }
    }

    @Test
    public void getClassFileName() {
        Class<AsmParameterNameDiscoverTest> asmParameterNameDiscoverTestClass = AsmParameterNameDiscoverTest.class;
        String name = asmParameterNameDiscoverTestClass.getName().replace(".", "/");
        assertEquals("top/huzhurong/web/asm/AsmParameterNameDiscoverTest", name);
    }

    public String hello(String hello, short age, LocalDateTime localDateTime, File file, User user) {
        System.out.print(hello + "\t");
        System.out.print(age + "\t");
        System.out.print(localDateTime + "\t");
        System.out.print(file + "\t");
        System.out.println(user);
        return "1";
    }

    @Test
    public void testType() {
        Class<?> aClass = int.class;
        boolean primitive = aClass.isPrimitive();
        assertTrue(primitive);
    }

    @Test
    public void tes() throws Exception {
        Class<?> aClass = Class.forName("java/lang/String".replace("/", "."));
        assertFalse(aClass.isPrimitive());

        Class<?> aClass1 = Class.forName("java.util.Date");
        Date date = (Date) aClass1.newInstance();
        date.setTime(1537789545549L);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2018-09-24", dateFormat.format(date));

    }

}