package io.github.chenshun00.ioc.transaction;

import lombok.Builder;
import lombok.Data;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/1
 */
@Data
@Builder
public class Test {
    private Integer id;
    private String name;
    private Integer age;
}
