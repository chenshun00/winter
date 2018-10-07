package top.huzhurong.web.util.dao;

import top.huzhurong.web.util.Test;
import top.huzhurong.web.util.query.TestQuery;

import java.util.List;

/**
 * @author chenshun
 * @since 2018-10-07 01:21:30
 */
public interface TestDao {

    Test selectTestByKey(Integer key);

    Integer addTest(Test test);

    Integer updateTestByKey(Test test);

    Integer deleteTestByKey(Integer key);

    List<Test> getTestWithTestQuery(TestQuery testQuery);

    List<Test> getTestWithPage(TestQuery testQuery);

    Integer batchInsertTest(List<Test> testList);

    Integer batchUpdateTest(List<Test> testList);

    Integer batchDeleteTest(List<Integer> idList);
}