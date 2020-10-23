package com.xiaoju.basetech.config;



import com.xiaoju.basetech.entity.ErrorCode;
import com.xiaoju.basetech.entity.HttpResult;
import com.xiaoju.basetech.entity.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author guojinqiong
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * response异常处理拦截器
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    HttpResult handleException(Exception e) {
        log.error("{}", e);
        return HttpResult.build(ErrorCode.FAIL);
    }

    /**
     * response异常处理拦截器
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ResponseException.class)
    HttpResult handleResponseException(ResponseException e) {
        log.error("{}", e);
        return HttpResult.build(e.getErrorCode(), e.getMsg());
    }

    /**
     * 数据校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    HttpResult handleValidateException(MethodArgumentNotValidException e) {
        log.error("{}", e);
        return HttpResult.build(ErrorCode.FAIL, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

}
