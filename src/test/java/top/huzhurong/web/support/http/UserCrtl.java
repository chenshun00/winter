package top.huzhurong.web.support.http;

import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.annotation.Transactional;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.web.annotation.Controller;
import top.huzhurong.web.annotation.Json;
import top.huzhurong.web.annotation.RequestMapping;
import top.huzhurong.web.support.impl.Request;
import top.huzhurong.web.support.upload.MultipartFile;
import top.huzhurong.web.util.dao.TestDao;

import java.io.IOException;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/20
 */
@RequestMapping("t")
@Controller
@Slf4j
public class UserCrtl {

    @Inject
    private TestDao testDao;

    @Transactional
    @RequestMapping("t")
    @Json
    public Object first(Request request, MultipartFile file, String cc) throws IOException {
        System.out.println("cc:" + cc);
        Object attribute = request.getHttpSession().getAttribute("chenshun");
        System.out.println("attribute:" + attribute);
        System.out.println("file.getName():" + file.getName());
        System.out.println("file.getOriginalFilename():" + file.getOriginalFilename());
//        System.out.println("testDao:" + testDao);
//        Test test = testDao.selectTestByKey(18);
//        test.setAge(111);
//        test.setName("winter");
//        testDao.updateTestByKey(test);
//        System.out.println(JSONObject.toJSONString(test));
//        //int i = 10 / 0;

        return "1";
    }


}
