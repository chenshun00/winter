package top.huzhurong.util;

import java.util.logging.Logger;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/6
 */
public class Log {
    public static Logger logger(String name) {
        return Logger.getLogger(name);
    }
}
