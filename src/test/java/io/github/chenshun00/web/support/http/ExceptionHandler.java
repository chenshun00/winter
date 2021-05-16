package io.github.chenshun00.web.support.http;

import io.github.chenshun00.web.annotation.ControllerAdvice;
import io.github.chenshun00.web.annotation.Exceptional;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/24
 */
@ControllerAdvice
public class ExceptionHandler {

    @Exceptional(ArithmeticException.class)
    public Result<?> arithmeticException(ArithmeticException arithmeticException) {
        return Result.offail(arithmeticException.getMessage());
    }

    @Exceptional(NullPointerException.class)
    public Result<?> nullPointerException(NullPointerException nullPointerException) {
        return Result.offail("message:" + nullPointerException.getMessage());
    }
}
