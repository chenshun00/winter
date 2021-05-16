package io.github.chenshun00.web.support.http;

import io.github.chenshun00.aop.annotation.Transactional;
import io.github.chenshun00.web.util.dao.TestDao;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.github.chenshun00.ioc.annotation.Inject;
import io.github.chenshun00.ioc.bean.IocContainer;
import io.github.chenshun00.ioc.bean.aware.InitAware;
import io.github.chenshun00.ioc.bean.aware.IocContainerAware;
import io.github.chenshun00.web.annotation.Controller;
import io.github.chenshun00.web.annotation.Json;
import io.github.chenshun00.web.annotation.RequestMapping;
import io.github.chenshun00.web.support.impl.Request;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/20
 */
@RequestMapping("t")
@Controller
@Slf4j
public class UserCrtl implements IocContainerAware, InitAware {

    @Inject
    private TestDao testDao;

    @Transactional
    @RequestMapping("t")
    @Json
    public Object first(Request request, String cc) throws IOException {
        request.getHttpSession().setAttribute("1", "我是陈顺");
//        System.out.println("cc:" + cc);
//        Object attribute = request.getHttpSession().getAttribute("chenshun");
//        System.out.println("attribute:" + attribute);
//        System.out.println("file.getName():" + file.getName());
//        System.out.println("file.getOriginalFilename():" + file.getOriginalFilename());
//        System.out.println("testDao:" + testDao);
//        Test test = testDao.selectTestByKey(18);
//        test.setAge(111);
//        test.setName("winter");
//        testDao.updateTestByKey(test);
//        System.out.println(JSONObject.toJSONString(test));
//        //int i = 10 / 0;
        return "1";
    }

    @Getter
    private IocContainer iocContainer;

    @Transactional
    @RequestMapping("zz")
    @Json
    public Object attr(Request request) throws IOException, SQLException {
        DataSource dataSource = (DataSource) iocContainer.getBean("dataSource");
        Connection connection = dataSource.getConnection();
        connection.close();
//        PreparedStatement preparedStatement = connection.prepareStatement("select 1");
        //ResultSet resultSet = preparedStatement.executeQuery();
        //Object attribute = request.getHttpSession().getAttribute("1");
        //System.out.println(attribute);
        return 2;
    }

    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
        System.out.println(this.iocContainer.getBean(DataSource.class));
    }

    @Override
    public void initBean() {
        if (this.iocContainer == null) {
            throw new RuntimeException("GG");
        }
        System.out.println("=============");
    }
}
