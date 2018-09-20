package top.huzhurong.web.support.http;


import lombok.Getter;
import lombok.Setter;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/20
 */
@Getter
@Setter
public class HttpCookie {
    private String name;
    private String value;

    private String path;
    private String domain;
    private boolean httpOnly;
    private long maxAge = Long.MIN_VALUE;
    private boolean secure;
}
