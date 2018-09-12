package top.huzhurong.ioc.bean.processor;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/7
 */
public interface BeanProcessor {

    /**
     * bean processor before init method
     *
     * @param object origin bean or target
     * @return maybe origin bean, maybe the wrapper bean like proxy
     */
    default Object processBeforeInit(Object object) {
        return object;
    }

    default Object processAfterInit(Object object) {
        return object;
    }
}
