package top.huzhurong.aop.core;

import org.junit.Assert;
import org.junit.Test;
import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class AspectjParserTest {

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
        Annotation aAnnotation = AspectjParser.findAAnnotationInUse(before, Before.class);
        Assert.assertNotNull(aAnnotation);
    }
}
