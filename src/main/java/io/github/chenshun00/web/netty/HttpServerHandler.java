package io.github.chenshun00.web.netty;

import io.github.chenshun00.ioc.annotation.Inject;
import io.github.chenshun00.web.support.http.HttpTradingCenter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author chenshun00@gmail.com
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
