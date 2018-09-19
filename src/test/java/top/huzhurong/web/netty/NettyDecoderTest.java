package top.huzhurong.web.netty;

import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
public class NettyDecoderTest {

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk if size exceed

    /**
     * test query string
     */
    @Test
    public void tessParseQueryString() {
        QueryStringDecoder decoder = new QueryStringDecoder("/hello?recipient=world&x=1;y=2", StandardCharsets.UTF_8);

        String path = decoder.path();
        Assert.assertEquals("/hello", path);

        String uri = decoder.uri();
        Assert.assertEquals("/hello?recipient=world&x=1;y=2", uri);

        Map<String, List<String>> parameters = decoder.parameters();
        Assert.assertEquals(3, parameters.size());

        List<String> list = parameters.get("recipient");
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("world", list.get(0));

        List<String> list1 = parameters.get("x");
        Assert.assertEquals(1, list1.size());
        Assert.assertEquals("1", list1.get(0));

        List<String> list2 = parameters.get("y");
        Assert.assertEquals(1, list2.size());
        Assert.assertEquals("2", list2.get(0));
    }

    @Test
    public void testUpload() {
        FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/hello");
        HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(factory, fullHttpRequest);

        //trunk
        boolean transferEncodingChunked = HttpHeaders.isTransferEncodingChunked(fullHttpRequest);
        //upload
        boolean multipart = postRequestDecoder.isMultipart();


    }

    @Test
    public void testHeaders() {
        FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/hello");
        HttpHeaders headers = fullHttpRequest.headers();
        Assert.assertEquals(0, headers.names().size());
    }

}
