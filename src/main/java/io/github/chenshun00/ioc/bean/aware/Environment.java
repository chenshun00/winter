package io.github.chenshun00.ioc.bean.aware;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/17
 */
@Slf4j
public class Environment {

    private final static String MYBATIS_CONFIG = "mybatis-config";
    public static String MYBATIS = null;

    private static final Map<String, String> context = new HashMap<>();

    static {
        try {
            InputStream resource = Environment.class.getClassLoader()
                    .getResourceAsStream("application.properties");
            Properties properties = new Properties();
            properties.load(resource);
            Set<String> set = properties.stringPropertyNames();
            set.forEach(key -> context.put(key, properties.getProperty(key)));
            String profile = (String) context.get("profiles.active");
            if (profile != null && profile.length() != 0) {
                final InputStream profileInputStream = Environment.class.getClassLoader().getResourceAsStream("application-" + profile + ".properties");
                if (profileInputStream != null) {
                    Properties pp = new Properties();
                    pp.load(profileInputStream);
                    pp.stringPropertyNames().forEach(key -> context.put(key, pp.getProperty(key)));
                }
            }

            if (context.get(MYBATIS_CONFIG) != null) {
                MYBATIS = context.get(MYBATIS_CONFIG);
            } else {
                MYBATIS = "";
            }
        } catch (Exception e) {
            log.error("load application.properties file occur exception : " + e);
        }
    }

    public boolean importOrm() {
        return context.get(MYBATIS_CONFIG) != null;
    }

    public String getString(String key) {
        return (String) Environment.context.get(key);
    }

    public Object getStringOrDef(String key, String def) {
        return Environment.context.getOrDefault(key, def);
    }


    public Integer getIntegerOrDef(String key, Integer def) {
        return Integer.parseInt(Environment.context.getOrDefault(key, def.toString()));
    }
}
