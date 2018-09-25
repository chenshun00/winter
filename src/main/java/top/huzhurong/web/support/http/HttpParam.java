package top.huzhurong.web.support.http;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/25
 */
@Getter
@Setter
@Builder
public class HttpParam {
    private String name;
    private String requestParamName;
    private boolean required;
    private String pathVariable;
}
