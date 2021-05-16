package io.github.chenshun00.aop.core;

import io.github.chenshun00.aop.advisor.Advisor;
import io.github.chenshun00.aop.annotation.Aspectj;
import io.github.chenshun00.aop.annotation.Before;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public class AspectjParserTest {

    @Test
    public void testAspectjOrder() {
        TestAspectj testAspectj = new TestAspectj();
        TestAspectj2 testAspectj2 = new TestAspectj2();
        List<Advisor> advisors = AspectjParser.parserAspectj(Arrays.asList(testAspectj, testAspectj2));
        advisors.forEach(advisor -> System.out.println(advisor.toString()));
    }

    @Test
    public void parserAspectj() {
        TestAspectj testAspectj = new TestAspectj();
        List<Advisor> advisors = AspectjParser.parserAspectj(testAspectj.getClass(), testAspectj);
        Assert.assertEquals(advisors.size(), 1);
    }

    @Test
    public void findAAnnotation() {
        boolean aAnnotation = AspectjParser.findAAnnotation(TestAspectj.class, Aspectj.class);
        Assert.assertTrue(aAnnotation);
    }

    @Test
    public void findAAnnotationInUse() throws NoSuchMethodException {
        Method before = TestAspectj.class.getDeclaredMethod("before");
        Annotation aAnnotation = AspectjParser.findAAnnotationInUse(before, Before.class).get();
        Assert.assertNotNull(aAnnotation);
    }
}
