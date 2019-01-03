package top.huzhurong.web.netty;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.MemoryFileUpload;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/22
 */
public class NettyUploadTest {

    final DefaultHttpDataFactory httpDataFactory = new DefaultHttpDataFactory(false);

    @Test
    public void testUpload() {

        final String boundary = "dLV9Wyq26L_-JQxk6ferf-RT153LhOO";
        final String contentTypeValue = "multipart/form-data; boundary=" + boundary;


        DefaultHttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/user/add");
        req.setDecoderResult(DecoderResult.SUCCESS);
        req.headers().add(HttpHeaderNames.CONTENT_TYPE, contentTypeValue);
        req.headers().add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);

        for (String data : Arrays.asList("", "\r", "\r\r", "\r\r\r")) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(httpDataFactory, req);
            final String body =
                    "--" + boundary + "\r\n" +
                            "Content-Disposition: form-data; name=\"file\"; filename=\"tmp-0.txt\"\r\n" +
                            "Content-Type: image/gif\r\n" +
                            "\r\n" +
                            data + "\r\n" +
                            "--" + boundary + "--\r\n";
            decoder.offer(new DefaultHttpContent(Unpooled.copiedBuffer(body, CharsetUtil.UTF_8)));
            decoder.offer(new DefaultHttpContent(Unpooled.EMPTY_BUFFER));


            Assert.assertTrue(decoder.hasNext());
            MemoryFileUpload upload = (MemoryFileUpload) decoder.next();

            Assert.assertEquals("Invalid decoded data [data=" + data.replaceAll("\r", "\\\\r") + ", upload=" + upload + ']',
                    data, upload.getString(CharsetUtil.UTF_8));
            upload.release();
            decoder.destroy();
        }
    }
}
