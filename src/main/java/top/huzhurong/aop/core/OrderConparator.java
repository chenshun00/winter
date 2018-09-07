package top.huzhurong.aop.core;

import top.huzhurong.aop.annotation.Order;

import java.util.Comparator;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/7
 */
public class OrderConparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return 0;
    }
}
