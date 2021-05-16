package io.github.chenshun00.web.util.dao;

import io.github.chenshun00.web.util.query.TestQuery;
import io.github.chenshun00.ioc.annotation.Bean;
import io.github.chenshun00.web.util.Test;
import io.github.chenshun00.xbatis.BaseDao;

import java.util.List;
import java.util.Objects;

/**
 * @author chenshun
 * @since 2018-10-07 01:21:30
 */
@Bean("testDao")
public class TestDaoImpl extends BaseDao<Test> implements TestDao {

    @Override
    public Test selectTestByKey(Integer key) {
        Objects.requireNonNull(key, "key不能为null");
        System.out.println(this.sqlSessionBean);
        return this.sqlSessionBean.selectOne("Test.selectTestByKey", key);
    }

    @Override
    public Integer addTest(Test test) {
        return this.sqlSessionBean.insert("Test.insertTest", test);
    }

    @Override
    public Integer updateTestByKey(Test test) {
        Objects.requireNonNull(test.getId(), "update时test.getId()不能为null");
        return this.sqlSessionBean.update("Test.updateTestByKey", test);
    }

    @Override
    public Integer deleteTestByKey(Integer key) {
        Objects.requireNonNull(key, "delete时key不能为null");
        return this.sqlSessionBean.delete("Test.deleteTestByKey", key);
    }

    @Override
    public List<Test> getTestWithTestQuery(TestQuery testQuery) {
        return this.sqlSessionBean.selectList("Test.getTestWithTestQuery", testQuery);
    }

    @Override
    public List<Test> getTestWithPage(TestQuery testQuery) {
        return this.sqlSessionBean.selectList("Test.getTestWithPage", testQuery);
    }

    @Override
    public Integer batchInsertTest(List<Test> testList) {
        return this.batchInsert("Test.batchInsertTest", testList);
    }

    @Override
    public Integer batchUpdateTest(List<Test> testList) {
        return this.batchUpdate("Test.batchUpdateTest", testList);
    }

    @Override
    public Integer batchDeleteTest(List<Integer> idList) {
        Objects.requireNonNull(idList, "批量删除idList不能为null");
        return this.batchDelete("Test.batchDeleteTest", idList);
    }

}
