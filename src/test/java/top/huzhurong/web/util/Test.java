package top.huzhurong.web.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author chenshun
 * @since 2018-10-07 01:21:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test {
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
}