package top.huzhurong.ioc.bean.aware;

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

    private static Map<String, Object> context = new HashMap<>();

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
                Environment.class.getClassLoader().getResourceAsStream("application-" + profile + ".properties");
                Properties pp = new Properties();
                pp.stringPropertyNames().forEach(key -> context.put(key, properties.getProperty(key)));
            }

            if (context.get(MYBATIS_CONFIG) != null) {
                MYBATIS = (String) context.get(MYBATIS_CONFIG);
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

    public Object getStringOrDef(String key, Object def) {
        return Environment.context.getOrDefault(key, def);
    }


    public Integer getInteger(String key) {
        return (Integer) Environment.context.get(key);
    }
}
