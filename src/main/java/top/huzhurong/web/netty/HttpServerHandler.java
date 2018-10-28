package top.huzhurong.web.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.web.support.http.HttpTradingCenter;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
@ChannelHandler.Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Inject
    private HttpTradingCenter httpTradingCenter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            httpTradingCenter.handleRequest(ctx, req);
        }
    }
}
