package top.huzhurong.web.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Member;
import java.util.Map;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/22
 */
public class ParameterNameDiscoveringVisitor extends ClassVisitor {

    private static final String STATIC_CLASS_INIT = "<clinit>";

    private final Class<?> clazz;

    private final Map<Member, String[]> memberMap;

    public ParameterNameDiscoveringVisitor(Class<?> clazz, Map<Member, String[]> memberMap) {
        super(Opcodes.ASM5);
        this.clazz = clazz;
        this.memberMap = memberMap;
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
            return new LocalVariableTableVisitor(this.clazz, this.memberMap, name, desc, isStatic(access));
        }
        return null;
    }

}
