package io.github.chenshun00.ioc.bean.aware;

/**
 * bean init
 *
 * @author chenshun00@gmail.com
 * @since 2018/9/7
 */
public interface InitAware extends Aware {

    /**
     * run after bean Instantiation，maybe check field is legal
     */
    void initBean();

}
