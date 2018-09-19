package top.huzhurong.web.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import top.huzhurong.web.support.http.HttpTradingCenter;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
@ChannelHandler.Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private HttpTradingCenter httpTradingCenter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            HttpResponse response = httpTradingCenter.handleRequest(req);
            //这里写的不对，还没有把respone解析掉，理论上netty是不会帮我把HttpResponse
            ctx.channel().write(response);
        }
    }
}
