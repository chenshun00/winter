package top.huzhurong.xbatis;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.huzhurong.ioc.annotation.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenshun
 * @since 2018-10-07 01:21:30
 */
@Slf4j
public abstract class BaseDao<T> {

    private final static Integer BATCH_SIZE = 20;

    @Getter
    @Setter
    @Inject
    protected SqlSessionBean sqlSessionBean;

    protected Integer batchInsert(String statement, List<T> lists) {
        if (lists == null || lists.size() == 0) {
            return 0;
        }
        List<T> middle = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            middle.add(lists.get(i));
            if (i == lists.size() - 1 || (i % BATCH_SIZE == 0 && i > 0)) {
                this.sqlSessionBean.insert(statement, middle);
                this.sqlSessionBean.flushStatements();
                middle.clear();
            }
        }
        return lists.size();
    }

    protected Integer batchUpdate(String statement, List<T> lists) {
        if (lists == null || lists.size() == 0) {
            return 0;
        }
        List<T> middle = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            middle.add(lists.get(i));
            if (i == lists.size() - 1 || (i % BATCH_SIZE == 0 && i > 0)) {
                this.sqlSessionBean.update(statement, middle);
                this.sqlSessionBean.flushStatements();
                middle.clear();
            }
        }
        return lists.size();
    }

    protected Integer batchDelete(String statement, List<Integer> keys) {
        if (keys == null || keys.size() == 0) {
            return 0;
        }
        List<Integer> middle = new ArrayList<>();
        for (int i = 0; i < keys.size() - 1; i++) {
            middle.add(keys.get(i));
            if (i == keys.size() || (i % BATCH_SIZE == 0 && i > 0)) {
                this.sqlSessionBean.delete(statement, middle);
                this.sqlSessionBean.flushStatements();
                middle.clear();
            }
        }
        return keys.size();
    }
}
