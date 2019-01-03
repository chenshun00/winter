package top.huzhurong.web.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/22
 */
public class HttpStaticFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(true);
    private HttpPostRequestDecoder decoder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        System.out.println(request.method() + " request received");

        if (request.method() == HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
        } else if (request.method() == HttpMethod.POST) {
            uploadFile(ctx, request); // user requested to upload file, handle request
        } else {
            System.out.println(request.method() + " request received, sending 405");
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
        }

    }

    private void uploadFile(ChannelHandlerContext ctx, FullHttpRequest request) {

        // test comment
        try {
            decoder = new HttpPostRequestDecoder(factory, request);
            //System.out.println("decoder created");
        } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
            e1.printStackTrace();
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Failed to decode file data");
            return;
        }

        // New chunk is received
        try {
            decoder.offer(request);
        } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
            e1.printStackTrace();
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Failed to decode file data");
            return;
        }

        readHttpDataChunkByChunk(ctx);
        // example of reading only if at the end
        reset(request);

    }

    private void sendResponse(ChannelHandlerContext ctx, String responseString,
                              String contentType, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, status, Unpooled.copiedBuffer(responseString, CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, contentType);
        response.headers().add("Access-Control-Allow-Origin", "*");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendUploadedFileName(JSONObject fileName, ChannelHandlerContext ctx) {
        String msg = "Unexpected error occurred";
        String contentType = "application/json; charset=UTF-8";
        HttpResponseStatus status = HttpResponseStatus.OK;

        if (fileName != null) {
            msg = "你好";
        } else {
            status = HttpResponseStatus.BAD_REQUEST;
            contentType = "text/plain; charset=UTF-8";
        }

        sendResponse(ctx, msg, contentType, status);

    }

    /**
     * 反射修复异常
     */
    @SuppressWarnings("unchecked")
    private void reset(HttpRequest request) {
        try {
//            decoder.destroy();
            Field field = factory.getClass().getDeclaredField("requestFileDeleteMap");
            field.setAccessible(true);
            Map<HttpRequest, List<HttpData>> requestFileDeleteMap = (Map<HttpRequest, List<HttpData>>) field.get(factory);
            List<HttpData> list = requestFileDeleteMap.remove(request);
            if (list != null) {
                for (HttpData data : list) {
                    if (data instanceof AbstractDiskHttpData) {
                        AbstractDiskHttpData diskHttpData = (AbstractDiskHttpData) data;
                        if (diskHttpData.refCnt() > 0) {
                            diskHttpData.release();
                        }
                    } else {
                        data.release();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            decoder = null;
        }
    }

    /**
     * Example of reading request by chunk and getting values from chunk to
     * chunk
     */
    private void readHttpDataChunkByChunk(ChannelHandlerContext ctx) {
        if (decoder.isMultipart()) {
            try {
                while (decoder.hasNext()) {
                    InterfaceHttpData data = decoder.next();
                    if (data != null) {
                        writeHttpData(data, ctx);
                        data.release();
                    }
                }
            } catch (Exception ignore) {
            }
        } else {
            List<InterfaceHttpData> bodyHttpDatas = decoder.getBodyHttpDatas();
            bodyHttpDatas.forEach(data -> writeHttpData(data, ctx));
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Not a multipart request");
        }
    }

    private void writeHttpData(InterfaceHttpData data, ChannelHandlerContext ctx) {
        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
            FileUpload fileUpload = (FileUpload) data;
            if (fileUpload.isCompleted()) {
                JSONObject json = saveFileToDisk(fileUpload);
                String filename = fileUpload.getFilename();
                String contentType = fileUpload.getContentType();
                sendUploadedFileName(json, ctx);
            } else {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Unknown error occurred");
            }
        } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
            Attribute attribute = (Attribute) data;
            try {
                String name = attribute.getName();
                String value = attribute.getValue();
                System.out.println("name:" + name + ",value:" + value);
            } catch (IOException ignored) {

            }
        }
    }

    /**
     * Saves the uploaded file to disk.
     *
     * @param fileUpload FileUpload object that'll be saved
     * @return name of the saved file. null if error occurred
     */
    private JSONObject saveFileToDisk(FileUpload fileUpload) {
        System.out.println("文件存储:" + fileUpload);
        String filename = fileUpload.getFilename();
        System.out.println("filename:" + filename);
        try {
            System.out.println(":" + fileUpload.getString(CharsetUtil.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, status, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        sendError(ctx, status, "Failure: " + status.toString() + "\r\n");
    }

}
