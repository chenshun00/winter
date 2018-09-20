package top.huzhurong.web.support.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/18
 */
@Getter
@Setter
@EqualsAndHashCode
public class HttpHeader {

    private String name;
    private String value;

    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
