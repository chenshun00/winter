package io.github.chenshun00.web.factorybean;

import io.github.chenshun00.ioc.annotation.Bean;
import io.github.chenshun00.ioc.bean.aware.FactoryBean;
import io.github.chenshun00.ioc.bean.aware.InitAware;
import io.github.chenshun00.web.support.http.User;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/4
 */
@Bean
public class FactoryBeanTest implements FactoryBean<User>, InitAware {
    @Override
    public User getObject() {
        User user = new User();
        user.setAge(22);
        user.setHello("hello");
        user.setName("cc");
        user.setWorld("world");
        return user;
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public void initBean() {
        System.out.println("InitAware");
    }
}
