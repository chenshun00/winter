package top.huzhurong.web.support.http;

import top.huzhurong.web.annotation.ControllerAdvice;
import top.huzhurong.web.annotation.Exceptional;

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
