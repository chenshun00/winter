package top.huzhurong.web.support.impl;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpRequest;
import lombok.Getter;
import lombok.Setter;
import top.huzhurong.web.support.http.HttpHeader;
import top.huzhurong.web.support.http.Request;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/18
 */
public class SimpleHttpRequest implements Request {

    @Getter
    @Setter
    private DefaultHttpRequest defaultHttpRequest;

    public SimpleHttpRequest(DefaultHttpRequest defaultHttpRequest) {
        this.defaultHttpRequest = defaultHttpRequest;
    }

    @Override
    public List<HttpHeader> headers() {
        return null;
    }

    @Override
    public Map<String, Object> params() {
        return null;
    }

    @Override
    public String getMethod() {
        return this.defaultHttpRequest.getMethod().name();
    }

    @Override
    public URI getURI() throws URISyntaxException {
        return new URI(this.defaultHttpRequest.getUri());
    }

    @Override
    public String body() {
        if (this.defaultHttpRequest instanceof DefaultFullHttpRequest) {
            DefaultFullHttpRequest fullHttpRequest = (DefaultFullHttpRequest) this.defaultHttpRequest;
        }
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public String protocolVersion() {
        return defaultHttpRequest.getProtocolVersion().protocolName();
    }

}
