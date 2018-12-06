package top.huzhurong.web.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.Environment;
import top.huzhurong.ioc.bean.aware.EnvironmentAware;
import top.huzhurong.ioc.bean.aware.InitAware;
import top.huzhurong.ioc.bean.aware.IocContainerAware;
import top.huzhurong.web.support.Server;

/**
 * init netty basic info ,  listen xxx port default 9652;
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
public class NettyServer implements Server, EnvironmentAware, IocContainerAware, InitAware {

    private static int POST = 9652;

    private Environment environment;
    private IocContainer iocContainer;
    private HttpServerHandler httpServerHandler;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Override
    public Server beforeStart() {
        POST = (int) environment.getStringOrDef("port", POST);
        return this;
    }

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("winter-http-"));
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new DefaultThreadFactory("winter-work-io-"));

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new HttpServerInitializer(null, this.httpServerHandler, this.environment));
        try {
            Channel ch = b.bind(POST).sync().channel();
            System.err.println("Open your web browser and navigate to http://127.0.0.1:" + POST + '/');
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            System.err.println("start shutdown");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup = new NioEventLoopGroup();
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }

    @Override
    public void initBean() {
        this.httpServerHandler = this.iocContainer.getBean(HttpServerHandler.class);
        if (httpServerHandler == null) {
            throw new RuntimeException("httpServerHandler is null");
        }
    }
}
