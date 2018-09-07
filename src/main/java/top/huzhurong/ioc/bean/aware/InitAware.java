package top.huzhurong.ioc.bean.aware;

/**
 * bean init
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/9/7
 */
public interface InitAware {

    /**
     * run after bean Instantiation，maybe check field is legal
     */
    void initBean();

}
