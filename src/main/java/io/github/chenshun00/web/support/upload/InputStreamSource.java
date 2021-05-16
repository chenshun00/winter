package io.github.chenshun00.web.support.upload;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/28
 */
public interface InputStreamSource {
    InputStream getInputStream() throws IOException;
}
