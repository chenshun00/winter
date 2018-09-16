package top.huzhurong.ioc;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.core.AspectjParser;
import top.huzhurong.aop.core.StringUtil;
import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.annotation.Configuration;
import top.huzhurong.ioc.annotation.Controller;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.ioc.bean.ClassInfo;
import top.huzhurong.ioc.bean.DefaultIocContainer;
import top.huzhurong.ioc.bean.InfoBeanFactory;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.BeanFactoryAware;
import top.huzhurong.ioc.bean.aware.InitAware;
import top.huzhurong.ioc.bean.aware.IocContainerAware;
import top.huzhurong.ioc.bean.processor.AopConfigUtil;
import top.huzhurong.ioc.bean.processor.BeanProcessor;
import top.huzhurong.ioc.bean.processor.ConfigurationUtil;
import top.huzhurong.ioc.scan.BeanScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * init ioc container
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/9/8
 */
@Slf4j
public class Init {
    @Setter
    private Class<?> bootClass;
    @Getter
    private IocContainer iocContainer = new DefaultIocContainer();
    private BeanScanner beanScanner = new BeanScanner();

    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public Init() {
    }

    public Init(@NonNull Class<?> bootClass) {
        this.bootClass = bootClass;
        instantiation();
    }

    public Set<ClassInfo> scan(@NonNull String... basePackages) {
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

    public void instantiation() {
        if (atomicBoolean.get()) {
            throw new IllegalStateException("Repeat registration exception");
        }
        atomicBoolean.compareAndSet(false, true);
        String name = bootClass.getPackage().getName();
        Set<ClassInfo> classInfoSet = scan(name);

        Set<ClassInfo> info = classInfoSet.stream().filter(this::find).collect(Collectors.toSet());
        info.add(new ClassInfo(ConfigurationUtil.class, StringUtil.handleClassName(ConfigurationUtil.class)));
        //handle transaction and aop config
        AopConfigUtil.handleConfig(info, bootClass);

        //handle bean name
        Set<ClassInfo> collect = info.stream().map(this::handleName).collect(Collectors.toSet());
        //register to ioc
        iocContainer.register(collect);

        handleAspectj(classInfoSet);
        //handle aware interface
        handleAware(collect);

        ConfigurationUtil configurationUtil = iocContainer.getBean(ConfigurationUtil.class);
        configurationUtil.handleConfig(collect);

        InfoBeanFactory infoBeanFactory;
        if (iocContainer instanceof InfoBeanFactory) {
            infoBeanFactory = iocContainer;
        } else {
            throw new ClassCastException("iocContainer can't cast infoBeanFactory");
        }
        //get BeanProcessor class set
        List<String> beanNameForType = infoBeanFactory.getBeanNameForType(BeanProcessor.class);
        for (ClassInfo classInfo : collect) {
            for (String beanName : beanNameForType) {
                BeanProcessor beanProcessor = (BeanProcessor) iocContainer.getBean(beanName);
                Object bean = beanProcessor.processBeforeInit(this.iocContainer.getBean(classInfo.getClassName()));
                iocContainer.put(classInfo.getClassName(), bean);
            }
        }
        collect.stream().map(ClassInfo::getClassName).map(iocContainer::getBean).filter(this::needInject).forEach(this::inject);
        initBean(collect);
        for (ClassInfo classInfo : collect) {
            for (String beanName : beanNameForType) {
                BeanProcessor beanProcessor = (BeanProcessor) iocContainer.getBean(beanName);
                Object bean = beanProcessor.processAfterInit(this.iocContainer.getBean(classInfo.getClassName()));
                iocContainer.put(classInfo.getClassName(), bean);
            }
        }
    }

    private void initBean(Set<ClassInfo> collect) {
        collect.stream().map(ClassInfo::getClassName).map(iocContainer::getBean)
                .filter(bean -> (bean instanceof InitAware))
                .forEach(bean -> {
                    InitAware aware = (InitAware) bean;
                    aware.initBean();
                });
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
                advisors.add(this.iocContainer.getBean(classInfo.getClassName()));
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
            Object bean = this.iocContainer.getBean(classInfo.getClassName());
            if (bean instanceof IocContainerAware) {
                IocContainerAware aware = (IocContainerAware) bean;
                aware.setIocContainer(iocContainer);
            } else if (bean instanceof BeanFactoryAware) {
                BeanFactoryAware aware = (BeanFactoryAware) bean;
                aware.setBeanFactory(iocContainer);
            }
        }
    }

    /**
     * inject target bean
     */
    private void inject(Object object) {
        Field[] declaredFields;
        if (AopConfigUtil.isCglibProxyClass(object.getClass())) {
            declaredFields = object.getClass().getSuperclass().getDeclaredFields();
        } else {
            declaredFields = object.getClass().getDeclaredFields();
        }
        Arrays.stream(declaredFields)
                .filter(field -> field.getAnnotation(Inject.class) != null)
                .forEach(ff -> {
                    Inject inject = ff.getAnnotation(Inject.class);
                    String value = inject.value();
                    if (!value.equals("")) {
                        Object bean = this.iocContainer.getBean(value);
                        doInject(bean, ff, object);
                    } else {
                        String name = ff.getName();
                        Object bean = this.iocContainer.getBean(name);
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
                ff.set(object, bean);
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
        Field[] declaredFields;
        if (object.getClass().getSimpleName().contains("$$")) {
            declaredFields = object.getClass().getSuperclass().getDeclaredFields();
        } else {
            declaredFields = object.getClass().getDeclaredFields();
        }

        for (Field declaredField : declaredFields) {
            if (declaredField.getAnnotation(Inject.class) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean find(ClassInfo classInfo) {
        Class<?> aClass = classInfo.getaClass();
        return findAnnotation(aClass, Bean.class, Controller.class, Aspectj.class, Configuration.class);
    }

    @SafeVarargs
    private final boolean findAnnotation(Class<?> aClass, Class<? extends Annotation>... annotation) {
        return Stream.of(annotation).anyMatch(nn -> aClass.getDeclaredAnnotation(nn) != null);
    }
}
