package top.huzhurong.web.support.http;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/18
 */
public interface Request {

    List<HttpHeader> headers();

    Map<String, Object> params();

    String getMethod();

    URI getURI() throws URISyntaxException;

    String body();

    InetSocketAddress getRemoteAddress();

    String protocolVersion();
}
