package com.xiaoju.hallowmas.enumType;


import org.apache.commons.lang.StringUtils;

/**
 * Created by yhg on 2016/8/15.
 */
public enum ErrorCode {
    SUCCESS(200, "success"),
    SERVER_ERROR(300, "系统异常！"),
    ID_IS_NOT_EXIST(500, "id 缺省！"),
    STATUS_IS_NOT_EXIST(501, "status 缺省！"),
    NAME_IS_NOT_EXIST(701, "名称不能为空！"),
    NAME_IS_EXIST(701, "该名称已经存在！"),
    PARAMS_IS_NULL(840, "入参为空！");


    private int code;
    private String msg;

    private ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return this.msg;
        }
        return this.msg + ": " + msg;
    }
}
