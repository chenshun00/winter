package top.huzhurong.web.factorybean;

import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.bean.aware.FactoryBean;
import top.huzhurong.ioc.bean.aware.InitAware;
import top.huzhurong.web.support.http.User;

/**
 * @author luobo.cs@raycloud.com
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
