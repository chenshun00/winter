package top.huzhurong.web.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext ssl;

    public HttpServerInitializer(SslContext ssl) {
        this.ssl = ssl;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (ssl != null) {
            pipeline.addLast(ssl.newHandler(ch.alloc()));
        }
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpServerHandler());
    }
}
