package top.huzhurong.aop.advisor.pointcut;

import top.huzhurong.util.StringUtils;

import java.lang.reflect.Method;

/**
 * public packageClass.method(parameter...) ： eg: public com.example.Hello helloWorld(string,int);
 * <ul>
 * <li>方法修饰符 public,支持 * 通配符</li>
 * <li>包名+类名 com.example.Hello * 通配符代表全部类</li>
 * <li>方法名 helloWorld * 代表所有方法</li>
 * <li>参数 String,int *.* 代表所有的参数(单个通配符仅代表一个)</li>
 * </ul>
 * <p>
 * 实例:com.example.hello包，子包first，second
 * 匹配所有的方法 * * *(*.*)
 * 匹配first类下的所有方法 * *.first *(*.*)
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/9/3
 */
public class StringPointcut implements Pointcut {

    //方法修饰符号
    private String modifier;
    private int modifierNumber = 0;
    //类名
    private String className;
    //方法名
    private String methodName;
    //参数
    private String parameter;
    /**
     * 切点表达式
     */
    private String pointcutExpression;

    public StringPointcut(String pointcutExpression) {
        doParser(pointcutExpression);
        this.pointcutExpression = pointcutExpression;
    }

    private void doParser(String pointcutExpression) {
        String[] split = pointcutExpression.split(" ");
        assert split.length == 4;
        modifier = split[0];
        this.className = split[2];
        String[] param = split[3].split("\\(");
        methodName = param[0];
        parameter = param[1].substring(0, param[1].length() - 1);
        handler();
        StringUtils.validate(modifier, className, methodName);
    }

    private void handler() {
        switch (modifier) {
            case "*":
                modifierNumber = -1;
                break;
            case "public":
                modifierNumber = 1;
                break;
            case "private":
                modifierNumber = 2;
                break;
            case "PROTECTED":
                modifierNumber = 4;
                break;
            default:
                modifierNumber = -2;
                break;
        }
    }

    /**
     * jdk 是采取实现接口的方式，接口的方法都是public + abstract = 1+ 1024(修饰符)
     *
     * @param method 方法
     * @return
     */
    @Override
    public boolean match(Method method) {
        //比较方法修饰符
        int modifiers = method.getModifiers();
        if (method.getDeclaringClass().isInterface()) {
            modifierNumber = 1024 + modifierNumber;
            className = method.getDeclaringClass().getName();
        }
        if (!modifier.equals("*") && modifiers != modifierNumber) {
            return false;
        }
        //比较类名
        String name = method.getDeclaringClass().getName();
        if (!className.equals("*") && !name.equals(className)) {
            return false;
        }
        //比较方法名
        String methodNameInfo = method.getName();
        if (!methodName.equals("*") && !methodNameInfo.equals(methodName)) {
            return false;
        }
        if (parameter.equals("*.*")) {
            return true;
        }
        if (parameter.equals("") && method.getParameterCount() == 0) {
            return true;
        }
        if (parameter.equals("") && method.getParameterCount() > 0) {
            return false;
        }
        if (!parameter.equals("") && method.getParameterCount() == 0) {
            return false;
        }
        if (!parameter.equals("") && method.getParameterCount() > 0) {
            String[] params = parameter.split(",");
            if (params.length != method.getParameterCount()) {
                return false;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                if (!params[i].equals(parameterTypes[i].getName().substring(parameterTypes[i].getName().lastIndexOf(".") + 1))) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getPointcutExpression() {
        return pointcutExpression;
    }

    public void setPointcutExpression(String pointcutExpression) {
        doParser(pointcutExpression);
        this.pointcutExpression = pointcutExpression;
    }
}
