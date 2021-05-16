package io.github.chenshun00.web.support.http;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/22
 */
@Getter
@Setter
@ToString
public class User {
    private String name;
    private Integer age;
    private String hello;
    private String world;
}
