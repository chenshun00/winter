package io.github.chenshun00.web.netty;

import io.github.chenshun00.ioc.bean.aware.Environment;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.ssl.SslContext;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/19
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext ssl;
    private HttpServerHandler httpServerHandler;

    private CorsConfig corsConfig;
    private boolean enable;

    public HttpServerInitializer(SslContext ssl, HttpServerHandler httpServerHandler, Environment environment) {
        this.ssl = ssl;
        this.httpServerHandler = httpServerHandler;
        this.enable = environment.getString("enableCors") != null;
        String origin = environment.getString("origin");
        this.corsConfig = CorsConfigBuilder.forOrigin(origin == null ? "*" : origin)
                .maxAge(3600L).allowedRequestMethods(HttpMethod.GET, HttpMethod.POST).allowNullOrigin().build();
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if (ssl != null) {
            pipeline.addLast(ssl.newHandler(ch.alloc()));
        }
        ChannelPipeline entries = pipeline.addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(1024 * 1024 * 64));
        if (enable) {
            entries.addLast("cors", new CorsHandler(this.corsConfig));
        }
        entries.addLast(this.httpServerHandler);
    }
}
