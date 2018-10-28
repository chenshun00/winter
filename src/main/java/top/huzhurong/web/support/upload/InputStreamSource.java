package top.huzhurong.web.support.upload;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/28
 */
public interface InputStreamSource {
    InputStream getInputStream() throws IOException;
}
