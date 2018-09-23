package top.huzhurong.web.asm;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import top.huzhurong.ioc.transaction.User;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
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
                System.out.println(JSONObject.toJSONString(parameterNames));
                System.out.println(parameterNames.getClass());
//                assertEquals("[hello, age, localDateTime, file, user]", Arrays.asList(parameterNames).toString());
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
    }

}