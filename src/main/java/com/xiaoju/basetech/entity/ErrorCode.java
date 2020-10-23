package com.xiaoju.basetech.entity;

import org.apache.commons.lang3.StringUtils;

public enum ErrorCode {
    SUCCESS(200, "success"),
    FAIL(-1, "fail");


    private int code;
    private String msg;

    private ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getMsg(String msg) {
        return StringUtils.isBlank(msg) ? this.msg : this.msg + ": " + msg;
    }
}


