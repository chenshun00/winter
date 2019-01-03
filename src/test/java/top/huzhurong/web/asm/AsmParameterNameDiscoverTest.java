package top.huzhurong.web.asm;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author chenshun00@gmail.com
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

    @Test
    public void testType() {
        Class<?> aClass = int.class;
        boolean primitive = aClass.isPrimitive();
        assertTrue(primitive);
    }

}