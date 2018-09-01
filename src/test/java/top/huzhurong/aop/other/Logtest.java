package top.huzhurong.aop.other;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/1
 */
@Slf4j
public class Logtest {

    @Test
    public void testLogback() {
        log.trace("hello {}", "logback");
        log.debug("hello {}", "logback");
        log.warn ("hello {}", "logback");
        log.info ("hello {}", "logback");
        log.error("hello {}", "logback");
    }
}
