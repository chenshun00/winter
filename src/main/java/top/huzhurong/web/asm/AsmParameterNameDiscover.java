package top.huzhurong.web.asm;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/22
 */
public class AsmParameterNameDiscover implements ParameterNameDiscoverer {

    private static final Map<Member, String[]> NO_DATA = Collections.emptyMap();

    @Override
    public String[] getParameterNames(Method method) {
        //获取类信息
        Class<?> declaringClass = method.getDeclaringClass();
        //获取方法的参数信息
        Map<Member, String[]> map = inspectClass(declaringClass);
        if (map != NO_DATA) {
            return map.get(method);
        }
        return null;
    }

    private Map<Member, String[]> inspectClass(Class<?> clazz) {
        //加载类的数据，asm使用
        String replace = getClassFileName(clazz);
        try {
            ClassReader classReader = new ClassReader(replace);
            Map<Member, String[]> map = new ConcurrentHashMap<>(32);
            classReader.accept(new ParameterNameDiscoveringVisitor(clazz, map), 0);

            return map;
        } catch (IOException | IllegalArgumentException ignored) {
            return NO_DATA;
        }
    }

    private String getClassFileName(Class clazz) {
        return clazz.getName().replace(".", "/");
    }


}
