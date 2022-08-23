package com.aplab.apsite.model.enums;

public enum IngrdResultCode implements ResultCode {
    INGRD_MODREQ_STATUS_FAIL("ingredient.msg.fail_modreq_delete_msg", "'변경신청 작성중' 상태에서만 삭제하실 수 있습니다."),
    INGRD_MODREQ_DELETE_FAIL("ingredient.msg.fail_modreq_delete_msg2", "삭제에 실패하였습니다. 관리자에게 문의해주시기 바랍니다."),
    INGRD_MODREQ_NO_DATA("ingredient.msg.delete_no_data", "삭제하려는 데이터가 존재하지 않습니다. 관리자에게 문의해주시기 바랍니다.")
    ;

    private String message;

    private String messageKey;

    IngrdResultCode (String messageKey, String message) {
        this.messageKey = messageKey;
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getMessageKey() {
        return this.messageKey;
    }
}
