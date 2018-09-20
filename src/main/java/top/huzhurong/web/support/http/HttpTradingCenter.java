package top.huzhurong.web.support.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import top.huzhurong.web.support.impl.SimpleHttpRequest;

import java.net.URISyntaxException;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
public class HttpTradingCenter {


    /**
     * 根据method和uri，请求头，cookie，参数这些信息去找到最符合我们请求的一个数据
     *
     * @param httpRequest http 请求
     * @return http响应
     * @throws URISyntaxException
     */
    public HttpResponse handleRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) throws URISyntaxException {
        SimpleHttpRequest simpleHttpRequest = SimpleHttpRequest.buildRequest(ctx, httpRequest);




        return null;
    }

}
