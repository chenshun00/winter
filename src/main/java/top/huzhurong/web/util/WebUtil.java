package top.huzhurong.web.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import java.net.InetSocketAddress;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
public class WebUtil {

    public static String ip(ChannelHandlerContext ctx, HttpRequest req) {
        HttpHeaders headers = req.headers();
        String clientIp = headers.get("X-Real-IP");
        if (clientIp == null) {
            clientIp = headers.get("X-Forwarded-For");
        }
        if (clientIp == null) {
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            clientIp = socketAddress.getAddress().getHostAddress();
        }
        return clientIp;
    }

}
