package io.github.chenshun00.aop.core;

import io.github.chenshun00.aop.annotation.Order;

import java.util.Comparator;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/7
 */
public class OrderComparator implements Comparator<Object> {
    @Override
    public int compare(Object aspectj1, Object aspectj2) {
        Order first = aspectj1.getClass().getAnnotation(Order.class);
        Order second = aspectj2.getClass().getAnnotation(Order.class);
        int firstOrder = first == null ? Integer.MAX_VALUE : first.value();
        int secondOrder = second == null ? Integer.MAX_VALUE : second.value();
        return Integer.compare(firstOrder, secondOrder);
    }
}
