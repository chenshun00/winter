package top.huzhurong.aop.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/11
 */
public class NameGeneratorTest {

    @Test
    public void generator() {
        String generator = NameGenerator.generator(new NameGeneratorTest());
        Assert.assertEquals("NameGeneratorTest#1", generator);
    }
}