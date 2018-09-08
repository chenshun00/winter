package top.huzhurong.ioc;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.annotation.Controller;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.ioc.bean.*;
import top.huzhurong.ioc.bean.processor.BeanProcessor;
import top.huzhurong.ioc.scan.BeanScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * init ioc container
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/9/8
 */
@Slf4j
public class Init {
    @Getter
    private IocContainer iocContainer = new DefaultIocContainer();
    @Getter
    private BeanFactory beanFactory = new DefaultBeanFactory(iocContainer);
    @Getter
    private BeanScanner beanScanner = new BeanScanner();

    public Set<ClassInfo> scan(String... basePackages) {
        Set<ClassInfo> classInfoSet = new HashSet<>();
        if (basePackages == null || basePackages.length == 0) {
            return classInfoSet;
        }
        return Arrays.stream(basePackages)
                .filter(basePackage -> !basePackage.isEmpty())
                .map(beanScanner::scan)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public void instantiation(Set<ClassInfo> classInfoSet) {
        Set<ClassInfo> collect = classInfoSet.stream().filter(this::find).collect(Collectors.toSet());
        //获取注入字段，每一个有可能注入对象
        beanFactory.register(collect);
        InfoBeanFactory infoBeanFactory;
        if (beanFactory instanceof InfoBeanFactory) {
            infoBeanFactory = (InfoBeanFactory) beanFactory;
        } else {
            throw new ClassCastException("beanFactory can't cast infoBeanFactory");
        }
        List<String> beanNameForType = infoBeanFactory.getBeanNameForType(BeanProcessor.class);
        beanNameForType.forEach(beanName -> processor(beanName, collect));
        collect.stream().filter(this::needInject).forEach(this::inject);
    }

    /**
     * processor
     *
     * @param beanName     target beanName
     * @param classInfoSet class set should be handled
     */
    private void processor(String beanName, Set<ClassInfo> classInfoSet) {
        BeanProcessor beanProcessor = (BeanProcessor) beanFactory.getBean(beanName);
        classInfoSet.forEach(beanProcessor::processBeforeInit);
    }

    /**
     * inject target bean
     */
    private void inject(ClassInfo classInfo) {
        Field[] declaredFields = classInfo.getaClass().getDeclaredFields();
        Arrays.stream(declaredFields)
                .filter(field -> field.getAnnotation(Inject.class) != null)
                .forEach(ff -> {
                    Inject inject = ff.getAnnotation(Inject.class);
                    String value = inject.value();
                    if (!value.equals("")) {
                        Object bean = this.beanFactory.getBean(value);
                        doInject(bean, ff, classInfo);
                    } else {
                        String name = ff.getName();
                        Object bean = this.beanFactory.getBean(name);
                        doInject(bean, ff, classInfo);
                    }
                });
    }

    private void doInject(Object bean, Field ff, ClassInfo classInfo) {
        if (bean == null) {
            throw new IllegalStateException("bean need to be injected is not exist:" + ff.getName());
        } else {
            ff.setAccessible(true);
            try {
                ff.set(this.beanFactory.getBean(classInfo.getaClass()), bean);
            } catch (Exception e) {
                log.error("inject failed," + e);
            }
        }
    }

    /**
     * the bean exist the inject field
     *
     * @param classInfo bean
     * @return true exist or false not exist
     */
    private boolean needInject(ClassInfo classInfo) {
        Field[] declaredFields = classInfo.getaClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getAnnotation(Inject.class) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean find(ClassInfo classInfo) {
        Annotation[] annotations = classInfo.getaClass().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.getClass().isAssignableFrom(Bean.class)) {
                return false;
            }
            if (annotation.getClass().isAssignableFrom(Controller.class)) {
                return false;
            }
        }
        return true;

    }

}
