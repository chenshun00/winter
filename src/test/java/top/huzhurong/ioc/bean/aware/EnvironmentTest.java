package top.huzhurong.ioc.bean.aware;

import org.junit.Test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/17
 */
public class EnvironmentTest {

    @Test
    public void testProperties() {
        Set<String> set;
        Properties properties = new Properties();
        try {
            InputStream resource = this.getClass().getClassLoader()
                    .getResourceAsStream("application.properties");
            properties.load(resource);
            set = properties.stringPropertyNames();
        } catch (Exception e) {
            set = new HashSet<>();
            System.err.println("加载application.properties文件出现异常:" + e);
        }
        set.forEach(key -> {
            System.out.println(key + ":" + properties.getProperty(key));
        });
    }

}