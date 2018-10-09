package top.huzhurong.ioc.bean.processor;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/9
 */
@Getter
@Setter
public class MethodObject {
    private Method method;
    private Object object;
}
