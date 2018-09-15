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
    public void addTest(Test test) {
        testDao.addTest(test);
        Test testById = testDao.getTestById(1);
        testById.setName("陈顺测试");
        testDao.updatetestById(testById);
//        int i = 10 / 0;
    }

}
