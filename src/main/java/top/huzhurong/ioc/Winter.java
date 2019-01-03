package top.huzhurong.ioc;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.core.AspectjParser;
import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.annotation.Configuration;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.ioc.bean.ClassInfo;
import top.huzhurong.ioc.bean.DefaultIocContainer;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.*;
import top.huzhurong.ioc.bean.processor.AopConfigUtil;
import top.huzhurong.ioc.bean.processor.BeanProcessor;
import top.huzhurong.ioc.bean.processor.ConfigurationUtil;
import top.huzhurong.ioc.scan.BeanScanner;
import top.huzhurong.util.StringUtils;
import top.huzhurong.web.annotation.Controller;
import top.huzhurong.web.annotation.ControllerAdvice;
import top.huzhurong.web.annotation.Filter;
import top.huzhurong.web.netty.HttpServerHandler;
import top.huzhurong.web.netty.NettyServer;
import top.huzhurong.web.support.http.HttpTradingCenter;
import top.huzhurong.web.support.route.HttpMatcher;
import top.huzhurong.web.support.route.HttpRouteBuilder;
import top.huzhurong.web.support.route.Route;
import top.huzhurong.web.support.session.SessionInterceptor;
import top.huzhurong.xbatis.MybatisFactoryBean;
import top.huzhurong.xbatis.SessionKit;
import top.huzhurong.xbatis.SqlSessionBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * init ioc container
 *
 * @author chenshun00@gmail.com
 * @since 2018/9/8
 */
@Slf4j
public class Winter {
    @Setter
    private Class<?> bootClass;
    @Getter
    private IocContainer iocContainer = new DefaultIocContainer();

    private BeanScanner beanScanner = new BeanScanner();

    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public Winter() {
    }

    public Winter(@NonNull Class<?> bootClass) {
        this.bootClass = bootClass;
    }

    public Set<ClassInfo> scan(@NonNull String... basePackages) {
        return Arrays.stream(basePackages)
                .filter(basePackage -> !basePackage.isEmpty())
                .map(beanScanner::scan)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private void prepare(Set<ClassInfo> info) {
        info.add(new ClassInfo(Environment.class, StringUtils.handleClassName(Environment.class)));
        info.add(new ClassInfo(ConfigurationUtil.class, StringUtils.handleClassName(ConfigurationUtil.class)));

        info.add(new ClassInfo(HttpTradingCenter.class, StringUtils.handleClassName(HttpTradingCenter.class)));
        info.add(new ClassInfo(HttpRouteBuilder.class, StringUtils.handleClassName(HttpRouteBuilder.class)));
        info.add(new ClassInfo(HttpMatcher.class, StringUtils.handleClassName(HttpMatcher.class)));

        info.add(new ClassInfo(NettyServer.class, StringUtils.handleClassName(NettyServer.class)));
        info.add(new ClassInfo(HttpServerHandler.class, StringUtils.handleClassName(HttpServerHandler.class)));


        info.add(new ClassInfo(SessionInterceptor.class, StringUtils.handleClassName(SessionInterceptor.class)));

        AopConfigUtil.handleConfig(info, this.bootClass);
    }

    public void start() {
        if (atomicBoolean.get()) {
            throw new IllegalStateException("Repeat registration exception");
        }
        atomicBoolean.compareAndSet(false, true);
        String name = bootClass.getPackage().getName();
        Set<ClassInfo> classInfoSet = scan(name);
        Set<ClassInfo> info = classInfoSet.stream().filter(this::find).collect(Collectors.toSet());

        prepare(info);

        //handle bean name
        Set<ClassInfo> collect = info.stream().map(this::handleName).collect(Collectors.toSet());

        //register to ioc
        iocContainer.register(collect);

        handleAspectj(classInfoSet);

        //handle aware interface
        handleAware(collect);

        ConfigurationUtil configurationUtil = iocContainer.getBean(ConfigurationUtil.class);
        configurationUtil.handleConfig(collect);

        if (iocContainer.getBean(Environment.class).importOrm()) {
            ClassInfo sessionKit = new ClassInfo(SessionKit.class, StringUtils.handleClassName(SessionKit.class));
            ClassInfo factoryBean = new ClassInfo(MybatisFactoryBean.class, StringUtils.handleClassName(MybatisFactoryBean.class));
            ClassInfo sessionBean = new ClassInfo(SqlSessionBean.class, StringUtils.handleClassName(SqlSessionBean.class));
            iocContainer.register(sessionKit);
            iocContainer.register(factoryBean);
            iocContainer.register(sessionBean);

            collect.add(sessionKit);
            collect.add(factoryBean);
            collect.add(sessionBean);
        }

        //get BeanProcessor class set
        List<String> beanNameForType = iocContainer.getBeanNameForType(BeanProcessor.class);
        for (ClassInfo classInfo : collect) {
            for (String beanName : beanNameForType) {
                BeanProcessor beanProcessor = (BeanProcessor) iocContainer.getBean(beanName);
                Object origin = this.iocContainer.getBean(classInfo.getClassName());
                Object bean = beanProcessor.processBeforeInit(origin);
                if (!origin.equals(bean)) {
                    iocContainer.put(classInfo.getClassName(), bean);
                }
            }
        }
        //是这里的问题
        collect.stream().map(ClassInfo::getClassName).map(iocContainer::getBean).filter(this::needInject).forEach(this::inject);
        initBean(collect);
        for (ClassInfo classInfo : collect) {
            for (String beanName : beanNameForType) {
                BeanProcessor beanProcessor = (BeanProcessor) iocContainer.getBean(beanName);
                Object bean = beanProcessor.processAfterInit(this.iocContainer.getBean(classInfo.getClassName()));
                iocContainer.put(classInfo.getClassName(), bean);
            }
        }

        collect.stream().map(ClassInfo::getClassName).map(iocContainer::getBean).filter(this::needInject).forEach(this::inject);

        List<String> list = this.iocContainer.beanNamesList();
        List<Route> routeList = new LinkedList<>();
        HttpRouteBuilder builder = this.iocContainer.getBean(HttpRouteBuilder.class);
        for (String ii : list) {
            Object bean = this.iocContainer.getBean(ii);
            if (AopConfigUtil.isCglibProxyClass(bean.getClass()) && bean.getClass().getSuperclass().isAnnotationPresent(Controller.class)) {
                routeList.addAll(builder.buildRoute(bean));
            }
            if (bean.getClass().getAnnotation(Controller.class) != null) {
                routeList.addAll(builder.buildRoute(bean));
            }
        }

        HttpMatcher matcher = this.iocContainer.getBean(HttpMatcher.class);
        matcher.buildRouteMap(routeList);


        final NettyServer server = this.iocContainer.getBean(NettyServer.class);
        server.beforeStart().start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::close));
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
            name = StringUtils.handleClassName(classInfo.getaClass().getSimpleName());
        }
        classInfo.setClassName(name);
        return classInfo;
    }

    private void handleAware(Set<ClassInfo> classInfoSet) {
        for (ClassInfo classInfo : classInfoSet) {
            Object bean = this.iocContainer.getBean(classInfo.getClassName());
            if (bean instanceof Aware) {
                if (bean instanceof IocContainerAware) {
                    IocContainerAware aware = (IocContainerAware) bean;
                    aware.setIocContainer(this.iocContainer);
                }
                if (bean instanceof BeanFactoryAware) {
                    BeanFactoryAware aware = (BeanFactoryAware) bean;
                    aware.setBeanFactory(this.iocContainer);
                }
                if (bean instanceof EnvironmentAware) {
                    EnvironmentAware aware = (EnvironmentAware) bean;
                    aware.setEnvironment(this.iocContainer.getBean(Environment.class));
                }
            }
        }
    }

    /**
     * inject target bean
     */
    private void inject(Object object) {
        Field[] declaredFields;
        Class<?> clazz;
        if (AopConfigUtil.isCglibProxyClass(object.getClass())) {
            declaredFields = object.getClass().getSuperclass().getDeclaredFields();
            clazz = object.getClass().getSuperclass();
        } else {
            declaredFields = object.getClass().getDeclaredFields();
            clazz = object.getClass();
        }
        do {
            Arrays.stream(declaredFields)
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .forEach(ff -> {
                        Inject inject = ff.getAnnotation(Inject.class);
                        String value = inject.value();
                        if (value.equals("")) {
                            value = ff.getName();
                        }
                        Object bean = this.iocContainer.getBean(value);
                        doInject(bean, ff, object);
                    });
            clazz = clazz.getSuperclass();
            declaredFields = clazz.getDeclaredFields();
        } while (exist(declaredFields));
    }

    private boolean exist(Field[] declaredFields) {
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Inject.class)) {
                return true;
            }
        }
        return false;
    }

    private void doInject(Object bean, Field ff, Object object) {
        if (bean == null) {
            throw new IllegalStateException(object + " bean need to be injected is not exist:" + ff.getName());
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
        Class<?> clazz;
        Field[] declaredFields;
        if (object.getClass().getSimpleName().contains("$$")) {
            declaredFields = object.getClass().getSuperclass().getDeclaredFields();
            clazz = object.getClass().getSuperclass();
        } else {
            declaredFields = object.getClass().getDeclaredFields();
            clazz = object.getClass();
        }

        do {
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(Inject.class)) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
            declaredFields = clazz.getDeclaredFields();
        } while (exist(declaredFields));
        return false;
    }

    private boolean find(ClassInfo classInfo) {
        Class<?> aClass = classInfo.getaClass();
        return findAnnotation(aClass, Bean.class, Controller.class, Aspectj.class,
                Configuration.class, Filter.class, ControllerAdvice.class);
    }

    @SafeVarargs
    private final boolean findAnnotation(Class<?> aClass, Class<? extends Annotation>... annotation) {
        return Stream.of(annotation).anyMatch(nn -> aClass.getDeclaredAnnotation(nn) != null);
    }
}
