package top.huzhurong.web.support.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.*;
import top.huzhurong.web.support.impl.Response;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/22
 */
public class HttpParameterParser {
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(true);
    private Map<String, Object> NULL = null;
    private HttpPostRequestDecoder decoder;

    public Map<String, Object> parseGetParams(HttpRequest httpRequest) {
        String uri = httpRequest.uri();
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = decoder.parameters();
        Map<String, Object> parseParams = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (value.size() == 1) {
                parseParams.put(key, value.get(0));
            } else {
                parseParams.put(key, value);
            }
        });
        return parseParams;
    }

    public Map<String, Object> parsetPostParams(ChannelHandlerContext ctx, FullHttpRequest httpRequest, Response response) {
        Map<String, Object> parseParams = new HashMap<>();

        try {
            decoder = new HttpPostRequestDecoder(factory, httpRequest);
        } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
            e1.printStackTrace();
            response.sendError(HttpResponseStatus.BAD_REQUEST, "can't decode post request");
            return NULL;
        }

        try {
            decoder.offer(httpRequest);
        } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
            e1.printStackTrace();
            response.sendError(HttpResponseStatus.BAD_REQUEST, "can't decode post request");
            return NULL;
        }
        readHttpDataChunkByChunk(ctx, parseParams);
        reset(httpRequest);
        return parseParams;
    }


    private void readHttpDataChunkByChunk(ChannelHandlerContext ctx, Map<String, Object> parseParams) {
        //文件上传
        if (decoder.isMultipart()) {
            try {
                while (decoder.hasNext()) {
                    InterfaceHttpData data = decoder.next();
                    if (data != null) {
                        try {
                            writeHttpData(data, parseParams);
                        } finally {
                            data.release();
                        }
                    }
                }
            } catch (Exception ignore) {

            }
        } else {
            List<InterfaceHttpData> bodyHttpData = decoder.getBodyHttpDatas();
            bodyHttpData.forEach(data -> writeHttpData(data, parseParams));
        }
    }

    private void writeHttpData(InterfaceHttpData data, Map<String, Object> parseParams) {
        try {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                FileUpload fileUpload = (FileUpload) data;
                if (fileUpload.isCompleted()) {
                    parseParams.put(fileUpload.getName(), fileUpload);
                }
            } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                Attribute attribute = (Attribute) data;

                String name = attribute.getName();
                String value = attribute.getValue();
                parseParams.put(name, value);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * 反射修复异常
     */
    @SuppressWarnings("unchecked")
    private void reset(HttpRequest request) {
        try {
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

}
