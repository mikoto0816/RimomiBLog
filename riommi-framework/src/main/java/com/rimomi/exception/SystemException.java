package com.rimomi.exception;

import com.rimomi.enums.AppHttpCodeEnum;

public class SystemException extends RuntimeException{

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum enums) {
        super(enums.getMsg());
        this.code = enums.getCode();
        this.msg = enums.getMsg();
    }
}
