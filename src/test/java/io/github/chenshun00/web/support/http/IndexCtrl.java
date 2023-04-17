package io.github.chenshun00.web.support.http;

import io.github.chenshun00.ioc.annotation.Inject;
import io.github.chenshun00.web.annotation.Body;
import io.github.chenshun00.web.annotation.Controller;
import io.github.chenshun00.web.annotation.Json;
import io.github.chenshun00.web.annotation.RequestMapping;
import io.github.chenshun00.web.support.impl.Request;
import io.github.chenshun00.web.util.Test;
import io.github.chenshun00.web.util.dao.TestDao;
import io.github.chenshun00.web.util.query.TestQuery;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2023/4/14 16:02
 */
@Controller
@Slf4j
public class IndexCtrl {

    @Inject
    protected TestDao testDao;

    @RequestMapping("/")
    public Object first(Request request, String cc) throws IOException {
        return "<h1>hello world</h1>";
    }

    @Json
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<Test>> list() {
        final List<Test> testWithTestQuery = testDao.getTestWithTestQuery(new TestQuery());
        return Result.ofSuccess(testWithTestQuery);
    }


    @Json
    @RequestMapping(value = "add")
    public Result<Boolean> add() {
        final Test test = new Test();
        test.setAge((int) (System.currentTimeMillis() % 100));
        test.setName(System.currentTimeMillis() + "i");
        final Integer add = testDao.addTest(test);
        return Result.ofSuccess(add.equals(1));
    }

    @Json
    @RequestMapping(value = "testAdd", method = RequestMethod.POST)
    public Result testAdd(Test test) {
        final Integer add = testDao.addTest(test);
        return Result.ofSuccess(add.equals(1));
    }

    @Json
    @RequestMapping(value = "testJson", method = RequestMethod.POST)
    public Result testBody(@Body Test test) {
        final Integer add = testDao.addTest(test);
        return Result.ofSuccess(add.equals(1));
    }

}
