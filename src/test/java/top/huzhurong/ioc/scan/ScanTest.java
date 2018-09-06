package top.huzhurong.ioc.scan;

import org.junit.Test;
import top.huzhurong.App;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/6
 */
public class ScanTest {


    @Test
    public void TestScan() throws IOException {
        String name = App.class.getPackage().getName();
        final String info = name.replaceAll("\\.", File.separator);
        Enumeration<URL> resources = App.class.getClassLoader().getResources(info);
        Set<String> set = new HashSet<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String path = url.getPath();
            File file = new File(path);
            set.addAll(readFromDir(file));
            break;
        }
        set.stream().filter(ii -> !ii.contains("$")).forEach(str -> {
            int index = str.indexOf(info);
            str = str.substring(index).replaceAll("/", ".");
            int index1 = str.lastIndexOf(".");
            str = str.substring(0, index1);
            if (str.contains("Bin")) {
                try {
                    Class<?> aClass = Class.forName(str);
                    System.out.println(aClass.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public Set<String> readFromDir(File file) {
        Set<String> set = new HashSet<>();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File file1 : files) {
                set.addAll(readFromDir(file1));
            }
        } else {
            set.add(file.getPath());
        }
        return set;
    }
}
