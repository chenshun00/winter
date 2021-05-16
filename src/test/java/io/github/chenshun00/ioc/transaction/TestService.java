package io.github.chenshun00.ioc.transaction;

import lombok.Getter;
import lombok.Setter;
import io.github.chenshun00.aop.annotation.Transactional;
import io.github.chenshun00.ioc.annotation.Bean;
import io.github.chenshun00.ioc.annotation.Inject;

import java.util.Date;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/2
 */
@Bean
public class TestService {

    @Inject
    @Getter
    @Setter
    private TestDao testDao;


    @Inject
    @Getter
    @Setter
    private UserDao userDao;

    /**
     * 测试事务回滚
     */
    @Transactional
    public Integer addTest(Test test) throws ClassNotFoundException {
        testDao.addTest(test);
        Test testById = testDao.getTestById(1);
        testById.setName("transaction测试");
        testDao.updatetestById(testById);
        //注释即可打开异常
//        int i = 10 / 0;

        userDao.addUser(new User(1, "cc", new Date()));

//        Class.forName("xxx.xxx.xxx.xxx");
        return 1;
    }

}
