package top.huzhurong.ioc.scan;

import lombok.extern.slf4j.Slf4j;
import top.huzhurong.ioc.bean.ClassInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/6
 */
@Slf4j
public class BeanScanner {

    /**
     * scan base packages
     *
     * @param basePackage base packages from user
     * @return final ClassInfo set
     */
    public Set<ClassInfo> scan(String basePackage) {
        Set<ClassInfo> classInfoSet = new HashSet<>();
        if (basePackage == null) {
            return classInfoSet;
        }

        final String filePath = basePackage.replaceAll("\\.", File.separator);
        try {
            Enumeration<URL> resources = this.getClass().getClassLoader().getResources(filePath);
            if (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                File file = new File(url.getPath());
                classInfoSet.addAll(readFromDir(file, filePath));
            }
            return classInfoSet;
        } catch (IOException e) {
            log.warn("scan path:{} occur error:{}", basePackage, e.getMessage());
            return classInfoSet;
        }
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
            String simpleName = aClass.getSimpleName();
            ClassInfo classInfo = new ClassInfo(aClass,
                    simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1));
            return Optional.of(classInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
