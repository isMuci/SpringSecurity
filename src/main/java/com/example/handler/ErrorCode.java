package com.example.handler;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数不正确"),
    SERVER_ERROR(500, "系统异常"),
    UNKNOWN(999, "未知错误");

    private Integer code;
    private String msg;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

}
