package com.xiaoju.basetech.entity;

public class HttpResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public HttpResult() {
        this.code = ErrorCode.SUCCESS.getCode();
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static <T> HttpResult<T> success() {
        return build(ErrorCode.SUCCESS);
    }

    public static <T> HttpResult<T> success(T data) {
        return build(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMsg(), data);
    }

    public static <T> HttpResult<T> fail() {
        return build(ErrorCode.FAIL);
    }

    public static <T> HttpResult<T> build(T data) {
        return build(ErrorCode.SUCCESS, data);
    }

    public static <T> HttpResult<T> build(ErrorCode code) {
        return build(code.getCode(), code.getMsg(),  null);
    }

    public static <T> HttpResult<T> build(Boolean success) {
        return build(success ? ErrorCode.SUCCESS : ErrorCode.FAIL);
    }

    public static <T> HttpResult<T> build(ErrorCode code, T data) {
        return build(code.getCode(), code.getMsg(), data);
    }

    public static <T> HttpResult<T> build(Boolean success, T data) {
        return build(success ? ErrorCode.SUCCESS : ErrorCode.FAIL, data);
    }

    public static <T> HttpResult<T> build(ErrorCode code, String msg) {
        return build(code.getCode(), msg,  null);
    }

    public static <T> HttpResult<T> build(Boolean success, String msg) {
        return build(success ? ErrorCode.SUCCESS : ErrorCode.FAIL, msg);
    }

    public static <T> HttpResult<T> build(ErrorCode code, String msg, T data) {
        return build(code.getCode(), msg, data);
    }

    public static <T> HttpResult<T> build(Boolean success, String msg, T data) {
        return build(success ? ErrorCode.SUCCESS : ErrorCode.FAIL, msg, data);
    }

    public static <T> HttpResult<T> build(int code, String msg) {
        return build(code, msg,  null);
    }

    public static <T>  HttpResult<T> build(int code, String msg, T data) {
        HttpResult<T> httpResult = new HttpResult();
        httpResult.setCode(code);
        httpResult.setMsg(msg);
        httpResult.setData(data);
        return httpResult;
    }
}
