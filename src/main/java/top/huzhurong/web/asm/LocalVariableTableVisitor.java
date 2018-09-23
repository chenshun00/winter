package top.huzhurong.web.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.Closeable;
import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.*;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/22
 */
public class LocalVariableTableVisitor extends MethodVisitor {

    public static final String ARRAY_SUFFIX = "[]";
    /**
     * The CGLIB class separator: {@code "$$"}.
     */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";
    /**
     * The ".class" file suffix.
     */
    public static final String CLASS_FILE_SUFFIX = ".class";
    private static final String CONSTRUCTOR = "<init>";
    /**
     * Prefix for internal array class names: {@code "["}.
     */
    private static final String INTERNAL_ARRAY_PREFIX = "[";
    /**
     * Prefix for internal non-primitive array class names: {@code "[L"}.
     */
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
    /**
     * The package separator character: {@code '.'}.
     */
    private static final char PACKAGE_SEPARATOR = '.';
    /**
     * The path separator character: {@code '/'}.
     */
    private static final char PATH_SEPARATOR = '/';
    /**
     * The inner class separator character: {@code '$'}.
     */
    private static final char INNER_CLASS_SEPARATOR = '$';
    private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<>(32);
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(8);
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(8);
    private static final Map<String, Class<?>> commonClassCache = new HashMap<>(64);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);

        primitiveWrapperTypeMap.forEach((key, value) -> {
            primitiveTypeToWrapperMap.put(value, key);
            registerCommonClasses(key);
        });

        Set<Class<?>> primitiveTypes = new HashSet<>(32);
        primitiveTypes.addAll(primitiveWrapperTypeMap.values());
        Collections.addAll(primitiveTypes, boolean[].class, byte[].class, char[].class,
                double[].class, float[].class, int[].class, long[].class, short[].class);
        primitiveTypes.add(void.class);
        for (Class<?> primitiveType : primitiveTypes) {
            primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
        }

        registerCommonClasses(Boolean[].class, Byte[].class, Character[].class, Double[].class,
                Float[].class, Integer[].class, Long[].class, Short[].class);
        registerCommonClasses(Number.class, Number[].class, String.class, String[].class,
                Class.class, Class[].class, Object.class, Object[].class);
        registerCommonClasses(Throwable.class, Exception.class, RuntimeException.class,
                Error.class, StackTraceElement.class, StackTraceElement[].class);
        registerCommonClasses(Enum.class, Iterable.class, Iterator.class, Enumeration.class,
                Collection.class, List.class, Set.class, Map.class, Map.Entry.class, Optional.class);

        Class<?>[] javaLanguageInterfaceArray = {Serializable.class, Externalizable.class,
                Closeable.class, AutoCloseable.class, Cloneable.class, Comparable.class};
        registerCommonClasses(javaLanguageInterfaceArray);
    }

    private final Class<?> clazz;
    private final Map<Member, Map<String, String>> memberMap;
    private final String name;
    private final Type[] args;
    private final String[] parameterNames;
    private final boolean isStatic;
    private final int[] lvtSlotIndex;
    private boolean hasLvtInfo = false;

    private final Map<String, String> mapping;

    public LocalVariableTableVisitor(Class<?> clazz, Map<Member, Map<String, String>> map, String name, String desc, boolean isStatic) {
        super(Opcodes.ASM5);
        this.clazz = clazz;
        this.memberMap = map;
        this.name = name;
        this.args = Type.getArgumentTypes(desc);
        this.parameterNames = new String[this.args.length];
        this.mapping = new LinkedHashMap<>(this.args.length);
        this.isStatic = isStatic;
        this.lvtSlotIndex = computeLvtSlotIndices(isStatic, this.args);
    }

    private static int[] computeLvtSlotIndices(boolean isStatic, Type[] paramTypes) {
        int[] lvtIndex = new int[paramTypes.length];
        int nextIndex = (isStatic ? 0 : 1);
        for (int i = 0; i < paramTypes.length; i++) {
            lvtIndex[i] = nextIndex;
            if (isWideType(paramTypes[i])) {
                nextIndex += 2;
            } else {
                nextIndex++;
            }
        }
        return lvtIndex;
    }

    private static boolean isWideType(Type aType) {
        // float is not a wide type
        return (aType == Type.LONG_TYPE || aType == Type.DOUBLE_TYPE);
    }

    public static Class<?> forName(String name, ClassLoader classLoader)
            throws ClassNotFoundException, LinkageError {

        Class<?> clazz = resolvePrimitiveClassName(name);
        if (clazz != null) {
            return clazz;
        }

        // "java.lang.String[]" style arrays
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // "[Ljava.lang.String;" style arrays
        if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
            String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
            Class<?> elementClass = forName(elementName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // "[[I" or "[[Ljava.lang.String;" style arrays
        if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {
            String elementName = name.substring(INTERNAL_ARRAY_PREFIX.length());
            Class<?> elementClass = forName(elementName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        ClassLoader clToUse = classLoader;
        if (clToUse == null) {
            clToUse = getDefaultClassLoader();
        }
        try {
            return (clToUse != null ? clToUse.loadClass(name) : Class.forName(name));
        } catch (ClassNotFoundException ex) {
            int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
            if (lastDotIndex != -1) {
                String innerClassName =
                        name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);
                try {
                    return (clToUse != null ? clToUse.loadClass(innerClassName) : Class.forName(innerClassName));
                } catch (ClassNotFoundException ex2) {
                    // Swallow - let original exception get through
                }
            }
            throw ex;
        }
    }

    public static Class<?> resolvePrimitiveClassName(String name) {
        Class<?> result = null;
        // Most class names will be quite long, considering that they
        // SHOULD sit in a package, so a length check is worthwhile.
        if (name != null && name.length() <= 8) {
            // Could be a primitive - likely.
            result = primitiveTypeNameMap.get(name);
        }
        return result;
    }

    private static void registerCommonClasses(Class<?>... commonClasses) {
        for (Class<?> clazz : commonClasses) {
            commonClassCache.put(clazz.getName(), clazz);
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        if (cl == null) {
            cl = LocalVariableTableVisitor.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignored) {
                }
            }
        }
        return cl;
    }

    @Override
    public void visitLocalVariable(String name, String description, String signature, Label start, Label end, int index) {
        this.hasLvtInfo = true;
        for (int i = 0; i < this.lvtSlotIndex.length; i++) {
            if (this.lvtSlotIndex[i] == index) {
                this.mapping.put(name, Type.getType(description).getClassName());
                this.parameterNames[i] = name;
            }
        }
    }

    @Override
    public void visitEnd() {
        if (this.hasLvtInfo || (this.isStatic && this.parameterNames.length == 0)) {
            this.memberMap.put(resolveMember(), this.mapping);
        }
    }

    private Member resolveMember() {
        ClassLoader loader = this.clazz.getClassLoader();
        Class<?>[] argTypes = new Class<?>[this.args.length];
        for (int i = 0; i < this.args.length; i++) {
            argTypes[i] = resolveClassName(this.args[i].getClassName(), loader);
        }
        try {
            if (CONSTRUCTOR.equals(this.name)) {
                return this.clazz.getDeclaredConstructor(argTypes);
            }
            return this.clazz.getDeclaredMethod(this.name, argTypes);
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("Method [" + this.name +
                    "] was discovered in the .class file but cannot be resolved in the class object", ex);
        }
    }

    public Class<?> resolveClassName(String className, ClassLoader classLoader)
            throws IllegalArgumentException {

        try {
            return forName(className, classLoader);
        } catch (IllegalAccessError err) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" +
                    className + "]: " + err.getMessage(), err);
        } catch (LinkageError err) {
            throw new IllegalArgumentException("Unresolvable class definition for class [" + className + "]", err);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Could not find class [" + className + "]", ex);
        }
    }
}
