package top.huzhurong.web.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/22
 */
public class ParameterNameDiscoveringVisitor extends ClassVisitor {

    private static final String STATIC_CLASS_INIT = "<clinit>";

    private final Class<?> clazz;

    private final Map<Member, Map<String, String>> memberMap;
    private final Map<Member, List<String>> params;

    public ParameterNameDiscoveringVisitor(Class<?> clazz, Map<Member, Map<String, String>> memberMap) {
        super(Opcodes.ASM5);
        this.clazz = clazz;
        this.memberMap = memberMap;
        this.params = new HashMap<>();
    }

    private static boolean isSyntheticOrBridged(int access) {
        return (((access & Opcodes.ACC_SYNTHETIC) | (access & Opcodes.ACC_BRIDGE)) > 0);
    }

    private static boolean isStatic(int access) {
        return ((access & Opcodes.ACC_STATIC) > 0);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (!isSyntheticOrBridged(access) && !STATIC_CLASS_INIT.equals(name)) {
            return new LocalVariableTableVisitor(this.clazz, this.memberMap == null ?
                    new HashMap<>() : this.memberMap, name, desc, isStatic(access));
        }
        return null;
    }

}
