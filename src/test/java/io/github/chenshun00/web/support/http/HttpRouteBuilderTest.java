package io.github.chenshun00.web.support.http;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.slf4j.LoggerFactory;
import io.github.chenshun00.web.support.route.HttpRouteBuilder;
import io.github.chenshun00.web.support.route.Route;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/20
 */
public class HttpRouteBuilderTest {

    private static final boolean standardReflectionAvailable = isPresent("java.lang.reflect.Executable",
            HttpRouteBuilderTest.class.getClassLoader());

    private static boolean isPresent(String name, ClassLoader classLoader) {
        try {
            Class.forName(name, false, classLoader);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private HttpRouteBuilder httpRouteBuilder;

    @Before
    public void before() {
        httpRouteBuilder = new HttpRouteBuilder();
    }

    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

    @Test
    public void testBuildRoute() {
        UserCrtl userCrtl = new UserCrtl();
        List<Route> routeList = httpRouteBuilder.buildRoute(userCrtl);
        assertEquals(7, routeList.size());
        routeList.forEach(System.out::println);
        System.out.println();
    }

    @Test
    public void testCast() {
        boolean assignableFrom = Integer.class.isAssignableFrom(Integer.class);
        assertTrue(assignableFrom);
    }

    @Test
    public void testLocal() {
        assertTrue(isJavaClass(Integer.class));
        assertTrue(isJavaClass(Integer.TYPE));
        assertTrue(isJavaClass(List.class));
        assertTrue(isJavaClass(ArrayList.class));
        assertFalse(isJavaClass(ClassWriter.class));
        assertFalse(isJavaClass(JSONObject.class));
        assertFalse(isJavaClass(MysqlDataSource.class));
        assertFalse(isJavaClass(LoggerFactory.class));
    }

    @Test
    public void testParameter() {
        Executable[] methods = HttpRouteBuilderTest.class.getDeclaredMethods();
        if (standardReflectionAvailable) {
            for (Executable method : methods) {
                String name = method.getName();
                if (name.equalsIgnoreCase("hello")) {
                    Parameter[] parameters = method.getParameters();
                    for (Parameter parameter : parameters) {
                        if (parameter.isNamePresent()) {
                            String parameterName = parameter.getName();
                            System.out.println("parameterName:" + parameterName);
                        }
                    }
                }
            }
        }
    }

    public String hello(String name, Integer age) {
        return "name:" + name + ",age:" + age;
    }
}