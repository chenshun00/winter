package top.huzhurong.web.support.http;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.annotation.Transactional;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.InitAware;
import top.huzhurong.ioc.bean.aware.IocContainerAware;
import top.huzhurong.web.annotation.Controller;
import top.huzhurong.web.annotation.Json;
import top.huzhurong.web.annotation.RequestMapping;
import top.huzhurong.web.support.impl.Request;
import top.huzhurong.web.util.dao.TestDao;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author luobo.cs@raycloud.com
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

    //@Transactional
    @RequestMapping("zz")
    @Json
    public Object attr(Request request) throws IOException, SQLException {
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
