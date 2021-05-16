package io.github.chenshun00.ioc.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/18
 */
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private Date birthday;
}
