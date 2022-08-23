package com.aplab.apsite.model.vo;

import java.util.List;

import com.aplab.apsite.model.enums.CommonResultCode;
import com.aplab.apsite.model.enums.ResultCode;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import lombok.Getter;

@Getter
public class ResponseVO {

    public int httpStatus; // HttpStatus

    public String code;

    public String message;

    public String messageKo;

    public Object data;

    public List<ObjectError> allErrors;

    public void setBadRequest(String code, String message, List<ObjectError> allErrors) {
        this.httpStatus = HttpStatus.BAD_REQUEST.value();
        this.code = code;
        this.message = message;
        this.allErrors = allErrors;
    }

    public void setBadRequest(String code, String message, String messageKo, List<ObjectError> allErrors) {
        this.httpStatus = HttpStatus.BAD_REQUEST.value();
        this.code = code;
        this.message = message;
        this.messageKo = messageKo;
        this.allErrors = allErrors;
    }

    public void setBadRequest(String code, String message, String messageKo, Object data, List<ObjectError> allErrors) {
        this.httpStatus = HttpStatus.BAD_REQUEST.value();
        this.code = code;
        this.message = message;
        this.messageKo = messageKo;
        this.data = data;
        this.allErrors = allErrors;
    }

    public void setOkWithCode(String code, String message, Object data) {
        this.httpStatus = HttpStatus.OK.value();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public void setOkWithCode(String code, String message, String messageKo, Object data) {
        this.httpStatus = HttpStatus.OK.value();
        this.code = code;
        this.message = message;
        this.messageKo = messageKo;
        this.data = data;
    }

    public void setOk(ResultCode resultCode, Object data) {
        this.httpStatus = HttpStatus.OK.value();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessageKey();
        this.messageKo = resultCode.getMessage();
        this.data = data;
    }

    public void setOk(Object data) {
        this.httpStatus = HttpStatus.OK.value();
        this.code = "C0000";
        this.message = "success";
        this.data = data;
    }

    public void setCreateOk(Object data) {
        this.httpStatus = HttpStatus.CREATED.value();
        this.code = "C0000";
        this.message = "success";
        this.data = data;
    }

    public void setBadRequest(String code, ResultCode resultCode, List<ObjectError> allErrors) {
        this.httpStatus = HttpStatus.BAD_REQUEST.value();
        this.code = code;
        this.message = resultCode.getMessageKey();
        this.messageKo = resultCode.getMessage();
        this.allErrors = allErrors;
    }
}
