package com.xiaoju.basetech.entity;



public class ResponseException  extends RuntimeException {
    private Integer errorCode;
    private String msg;

    private ResponseException() {
    }

    public ResponseException(ErrorCode error, String errorMsg) {
        super(errorMsg);
        this.errorCode = error.getCode();
        this.msg = errorMsg;
    }

    public ResponseException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ResponseException(ErrorCode error) {
        this.errorCode = error.getCode();
        this.msg = error.getMsg();
    }

    public ResponseException(Integer errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public String getMsg() {
        return this.msg;
    }
}
