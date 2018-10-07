package top.huzhurong.web.util.query;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenshun
 * @since 2018-10-07 01:21:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TestQuery extends BaseQuery {
    /**
     * id
     */
    private Integer id;
    /**
     * name
     */
    private String name;
    /**
     * age
     */
    private Integer age;

    private List<OrderFields> orderFields = new ArrayList<>();

    public TestQuery orderById(boolean desc) {
        orderFields.add(OrderFields.builder().orderBy("id").desc(desc ? "ASC" : "DESC").build());
        return this;
    }

    public TestQuery orderByName(boolean desc) {
        orderFields.add(OrderFields.builder().orderBy("name").desc(desc ? "ASC" : "DESC").build());
        return this;
    }

    public TestQuery orderByAge(boolean desc) {
        orderFields.add(OrderFields.builder().orderBy("age").desc(desc ? "ASC" : "DESC").build());
        return this;
    }

    @Builder
    private static class OrderFields {
        @Getter
        @Setter
        private String orderBy;
        @Getter
        @Setter
        private String desc;
    }
}
