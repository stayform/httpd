package com.stay.jerrymouse.http;

/**
 * AuThor：StAY_
 * Create:2019/11/16
 */
public enum Status {
    OK(200,"OK"),
    BAD_REQUEST(400,"Bad_Request"),
    NOT_FOUND(404,"Not_Found"),
    INTERNAL_SERVER_ERROR(500,"Internal Server Error"),
    METHOD_NOT_ALLOWED(405,"Method");

    private int code;
    private String reason;

    Status(int code,String reason){
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}

