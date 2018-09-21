package top.huzhurong.web.util;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import top.huzhurong.ioc.transaction.User;

import java.util.Date;

import static org.junit.Assert.assertFalse;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/21
 */
public class JsonUtilTest {

    @Test
    public void toJson() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        User user = new User(1, "chen", new Date());
        String string = JSONObject.toJSONString(user);
        embeddedChannel.writeInbound(Unpooled.copiedBuffer(string, CharsetUtil.UTF_8));

        ByteBuf res = embeddedChannel.readInbound();
        System.out.println(res.toString(CharsetUtil.UTF_8));
        res.release();
        assertFalse(embeddedChannel.finish());

    }


}