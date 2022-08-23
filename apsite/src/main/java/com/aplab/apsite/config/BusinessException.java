package com.aplab.apsite.config;

import com.aplab.apsite.model.enums.ResultCode;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    private final String messageKo;

    private final Object data;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessageKey());
        this.messageKo = resultCode.getMessage();
        this.code = resultCode.getCode();
        this.data = null;
    }

    public BusinessException(ResultCode resultCode, Object data) {
        super(resultCode.getMessageKey());
        this.messageKo = resultCode.getMessage();
        this.code = resultCode.getCode();
        this.data = data;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.messageKo = null;
        this.code = code;
        this.data = null;
    }

    public BusinessException(String code, String message, Object data) {
        super(message);
        this.messageKo = null;
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public Object getData() {
        return this.data;
    }

    public String getMessageKo() {
        return this.messageKo;
    }
    
}
