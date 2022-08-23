package com.aplab.apsite.model.enums;

public enum CommonResultCode implements ResultCode {
    
    NO_DATA("common.msg.no_data", "데이터가 존재하지 않습니다."),
    NO_AUTH("common.msg.no_auth", "권한이 없습니다."),
    NO_ESSENTIAL_DATA("common.msg.no_essential_data", "필수값이 없습니다. 관리자에게 문의하시기 바랍니다."),
    SAVE_FAIL("common.msg.save_fail", "저장에 실패했습니다.<br>관리자에게 문의하시기 바랍니다."),
    FAIL_EXT("common.msg.check_file_ext", "업로드 불가능한 확장 파일을 포함하고 있습니다."),
    MASTER_RETIRE_FAIL("myinfo.msg.profile.desc11", "{userNm} 님은 업체 마스터 계정입니다. 마스터계정 위임 후에 탈퇴처리가 가능합니다."),
    DIFF_PWD_FAIL("myinfo.msg.chkPwd.desc2", "비밀번호를 확인하세요.")
    ;

    private String message;

    private String messageKey;

    CommonResultCode (String messageKey, String message) {
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
