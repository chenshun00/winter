package top.huzhurong.ioc;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.core.AspectjParser;
import top.huzhurong.aop.core.StringUtil;
import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.annotation.Controller;
import top.huzhurong.ioc.annotation.EnableConfiguration;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.ioc.bean.*;
import top.huzhurong.ioc.bean.aware.IocContainerAware;
import top.huzhurong.ioc.bean.processor.AopBeanProcessor;
import top.huzhurong.ioc.bean.processor.BeanProcessor;
import top.huzhurong.ioc.bean.processor.TransactionBeanProcessor;
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
    @Setter
    private Class<?> bootClass;
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
        //handle transaction and aop config
        handleConfig(classInfoSet);
        //handle bean name
        Set<ClassInfo> collect = classInfoSet.stream().filter(this::find).map(this::handleName).collect(Collectors.toSet());
        //register to ioc
        beanFactory.register(collect);
        handleAspectj(classInfoSet);
        //handle aware interface
        handleAware(collect);
        InfoBeanFactory infoBeanFactory;
        if (beanFactory instanceof InfoBeanFactory) {
            infoBeanFactory = (InfoBeanFactory) beanFactory;
        } else {
            throw new ClassCastException("beanFactory can't cast infoBeanFactory");
        }
        //get BeanProcessor class set
        List<String> beanNameForType = infoBeanFactory.getBeanNameForType(BeanProcessor.class);
        for (ClassInfo classInfo : collect) {
            for (String beanName : beanNameForType) {
                BeanProcessor beanProcessor = (BeanProcessor) beanFactory.getBean(beanName);
                Object bean = beanProcessor.processBeforeInit(this.beanFactory.getBean(classInfo.getClassName()));
                beanFactory.put(classInfo.getClassName(), bean);
            }
        }
        collect.stream().map(ClassInfo::getClassName).map(beanFactory::getBean).filter(this::needInject).forEach(this::inject);
    }

    /**
     * handle aspectj
     *
     * @param classInfoSet bean set
     */
    private void handleAspectj(Set<ClassInfo> classInfoSet) {
        Iterator<ClassInfo> iterator = classInfoSet.iterator();
        List<Object> advisors = new LinkedList<>();
        while (iterator.hasNext()) {
            ClassInfo classInfo = iterator.next();
            if (classInfo.getaClass().getAnnotation(Aspectj.class) != null) {
                advisors.add(this.beanFactory.getBean(classInfo.getClassName()));
                iterator.remove();
            }
        }
        List<Advisor> advisorList = AspectjParser.parserAspectj(advisors);
        Set<ClassInfo> collect = advisorList.stream().map(advisor -> new ClassInfo(advisor.getClass(), advisor.toString()))
                .collect(Collectors.toSet());
        classInfoSet.addAll(collect);
    }

    private ClassInfo handleName(ClassInfo classInfo) {
        Bean bean = classInfo.getaClass().getAnnotation(Bean.class);
        Controller controller = classInfo.getaClass().getAnnotation(Controller.class);
        Aspectj aspectj = classInfo.getaClass().getAnnotation(Aspectj.class);
        if (bean != null && controller != null) {
            throw new IllegalStateException("Bean and Controller Cannot appear simultaneously at ---> "
                    + classInfo.getaClass().getName());
        }
        String name = bean != null ? bean.value() : controller != null ?
                controller.value() : aspectj != null ? aspectj.value() : null;
        if (name == null || name.length() == 0) {
            name = StringUtil.handleClassName(classInfo.getaClass().getSimpleName());
        }
        classInfo.setClassName(name);
        return classInfo;
    }

    private void handleAware(Set<ClassInfo> classInfoSet) {
        for (ClassInfo classInfo : classInfoSet) {
            Object bean = this.getBeanFactory().getBean(classInfo.getClassName());
            if (bean instanceof IocContainerAware) {
                IocContainerAware aware = (IocContainerAware) bean;
                aware.setIocContainer(iocContainer);
            }
        }
    }

    private void handleConfig(Set<ClassInfo> classInfoSet) {
        Class<?> aClass = Objects.requireNonNull(bootClass, "bootClass can't be null");
        EnableConfiguration declaredAnnotation = aClass.getDeclaredAnnotation(EnableConfiguration.class);
        if (declaredAnnotation != null) {
            Arrays.stream(declaredAnnotation.value())
                    .filter(str -> !str.isEmpty())
                    .forEach(config -> this.doConfig(config, classInfoSet));
        }
    }

    private void doConfig(String config, Set<ClassInfo> classInfoSet) {
        if ("aop".equals(config)) {
            log.info("start handle aop, add AopBeanProcessor.class");
            ClassInfo classInfo = new ClassInfo(AopBeanProcessor.class, StringUtil.handleClassName(AopBeanProcessor.class));
            classInfoSet.add(classInfo);
        } else if ("transaction".equals(config)) {
            log.info("start handle transaction, add TransactionBeanProcessor.class");
            ClassInfo classInfo = new ClassInfo(TransactionBeanProcessor.class, StringUtil.handleClassName(TransactionBeanProcessor.class));
            classInfoSet.add(classInfo);
        }
    }

    /**
     * inject target bean
     */
    private void inject(Object object) {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        Arrays.stream(declaredFields)
                .filter(field -> field.getAnnotation(Inject.class) != null)
                .forEach(ff -> {
                    Inject inject = ff.getAnnotation(Inject.class);
                    String value = inject.value();
                    if (!value.equals("")) {
                        Object bean = this.beanFactory.getBean(value);
                        doInject(bean, ff, object);
                    } else {
                        String name = ff.getName();
                        Object bean = this.beanFactory.getBean(name);
                        doInject(bean, ff, object);
                    }
                });
    }

    private void doInject(Object bean, Field ff, Object object) {
        if (bean == null) {
            throw new IllegalStateException("bean need to be injected is not exist:" + ff.getName());
        } else {
            ff.setAccessible(true);
            try {
                ff.set(this.beanFactory.getBean(object.getClass()), bean);
            } catch (Exception e) {
                log.error("inject failed," + e);
            }
        }
    }

    /**
     * the bean exist the inject field
     *
     * @param object bean
     * @return true exist or false not exist
     */
    private boolean needInject(Object object) {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getAnnotation(Inject.class) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean find(ClassInfo classInfo) {
        Annotation[] annotations = classInfo.getaClass().getAnnotations();
        for (Annotation annotation : annotations)
            if (annotation.getClass().getAnnotation(Bean.class) != null) {
                return true;
            } else if (annotation.getClass().getAnnotation(Controller.class) != null) {
                return true;
            } else return annotation.getClass().getAnnotation(Aspectj.class) != null;
        return false;
    }
}