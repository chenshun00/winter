package top.huzhurong.aop.advisor.transaction.manager;

import org.junit.Test;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/1
 */
public class ExceptionTest {
    @Test
    public void testThrowException() {
        System.out.println("do some thing");
        try {
            int i = 10 / 0;
            System.out.println(i);
        } catch (Throwable throwable) {
            System.out.println("handle exception");
            throw throwable;
        }
        System.out.println("normal return");
    }
}
