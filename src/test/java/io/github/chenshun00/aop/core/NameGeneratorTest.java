package io.github.chenshun00.aop.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/11
 */
public class NameGeneratorTest {

    @Test
    public void generator() {
        String generator = NameGenerator.generator(new NameGeneratorTest());
        Assert.assertEquals("NameGeneratorTest#1", generator);
    }
}