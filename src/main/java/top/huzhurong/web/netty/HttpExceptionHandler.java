package top.huzhurong.web.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpResponse;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
@ChannelHandler.Sharable
public class HttpExceptionHandler extends SimpleChannelInboundHandler<HttpResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpResponse msg) throws Exception {

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
