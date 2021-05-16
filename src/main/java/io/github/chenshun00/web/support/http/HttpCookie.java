package io.github.chenshun00.web.support.http;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/20
 */
@Getter
@Setter
@EqualsAndHashCode
public class HttpCookie {
    private String name;
    private String value;

    private String path = "/";
    private String domain;
    private boolean httpOnly = false;
    private long maxAge = Long.MIN_VALUE;
    private boolean secure = false;

    public HttpCookie(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
