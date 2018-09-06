package top.huzhurong.ioc.scan;

import top.huzhurong.ioc.bean.ClassInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/6
 */
public class BeanScanner {

    /**
     * scan base packages
     *
     * @param basePackages base packages from user
     * @return final ClassInfo set
     */
    public Set<ClassInfo> scan(String... basePackages) throws IOException {
        Set<ClassInfo> classInfoSet = new HashSet<>();
        if (basePackages == null || basePackages.length == 0) {
            return classInfoSet;
        }

        for (String basePackage : basePackages) {
            final String filePath = basePackage.replaceAll("\\.", File.separator);
            Enumeration<URL> resources = this.getClass().getClassLoader().getResources(filePath);
            if (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                File file = new File(url.getPath());
                classInfoSet.addAll(readFromDir(file, filePath));
            }
        }
        return classInfoSet;
    }

    private Collection<? extends ClassInfo> readFromDir(File file, String filePath) {
        Set<ClassInfo> classInfoSet = new HashSet<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File ff : files) {
                classInfoSet.addAll(readFromDir(ff, filePath));
            }
        } else {
            getClassInfo(file.getPath(), filePath).ifPresent(classInfoSet::add);
        }
        return classInfoSet;
    }

    private Optional<ClassInfo> getClassInfo(String path, String filePath) {
        int index = path.indexOf(filePath);
        path = path.substring(index).replaceAll("/", ".");
        path = path.substring(0, path.lastIndexOf("."));
        if (path.contains("$")) {
            return Optional.empty();
        }
        try {
            Class<?> aClass = Class.forName(path);
            ClassInfo classInfo = new ClassInfo(aClass, aClass.getName());
            return Optional.of(classInfo);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
            return Optional.empty();
        }
    }

}
