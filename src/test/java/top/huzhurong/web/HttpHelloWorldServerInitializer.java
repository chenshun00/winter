package top.huzhurong.web;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext ssl;

    public HttpHelloWorldServerInitializer(SslContext ssl) {
        this.ssl = ssl;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (ssl != null) {
            pipeline.addLast(ssl.newHandler(ch.alloc()));
        }
        pipeline.addLast(new HttpServerCodec());
//        pipeline.addLast(new HttpRequestDecoder());
//        pipeline.addLast(new HttpResponseEncoder());
        //仅实例化一次即可
        pipeline.addLast("aggregator", new HttpObjectAggregator(1024 * 1024 * 64));
        pipeline.addLast(new HttpHelloWorldServerHandler());
    }
}
