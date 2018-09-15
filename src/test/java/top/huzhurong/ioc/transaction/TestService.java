package top.huzhurong.ioc.transaction;

import lombok.Getter;
import lombok.Setter;
import top.huzhurong.aop.annotation.Transactional;
import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.annotation.Inject;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/2
 */
@Bean
public class TestService {

    @Inject
    @Getter
    @Setter
    private TestDao testDao;

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

        Class.forName("xxx.xxx.xxx.xxx");
        return 1;
    }

}
