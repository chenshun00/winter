package io.github.chenshun00.web.support.http;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/22
 */
@Getter
@Setter
@Builder
public class Result<R> {
    private R data;
    private Integer code;
    private String reason;
    private LocalDateTime timestamp;


    public static Result ofSuccess(Object data) {
        return Result.builder().code(100).data(data).reason("success").timestamp(LocalDateTime.now()).build();
    }

    public static Result offail(String reason) {
        return offail(null, reason);
    }

    public static Result offail(Object data, String reason) {
        return Result.builder().code(-1).data(data).reason(reason).timestamp(LocalDateTime.now()).build();
    }

}