package com.aplab.apsite.model.enums;

public enum LoginResultCode implements ResultCode {

    USER_NOT_FOUND("common.msg.login_0001", "사용자를 찾을 수 없습니다."),
    USER_PW_FAIL("common.msg.login_0002", "아이디/비밀번호를 확인해 주세요."),
    USER_PW_RESET("common.msg.login_0003", "비밀번호 재설정이 필요합니다."),
    USER_PW_RESET_FAIL_LIMIT("common.msg.login_0004", "비밀번호가 5회 이상 틀렸습니다. 비밀번호 재 설정후 로그인 해주시기 바랍니다."),
    USER_AP_PW_RESET_FAIL_LIMIT("common.msg.login_0014", "비밀번호가 5회 이상 틀렸습니다. 사이트 담당자에게 비밀번호 초기화를 요청해주세요."),
    FAIL_NOW_PASSwORD("common.msg.login_0010", "현재 비밀번호가 일치하지 않습니다."),
    DUPLE_PASSWORD0("common.msg.login_0006", "기존 비밀번호와 동일합니다. </br>다른 비밀 번호로 변경 부탁드립니다."),
    DUPLE_PASSWORD1("common.msg.login_0007", "기존에 변경했던 비밀번호와 동일합니다. </br>다른 비밀 번호로 변경 부탁드립니다."),
    USER_PW_EXPIRED("common.msg.login_0008", "비밀번호 변경이 필요합니다."),
    USER_PW_SEVEN_EXPIRED("common.msg.login_0012", "비밀번호가 7일 이후에 만료 예정입니다. </br> 비밀번호 변경이 필요합니다."),
    USER_DUPLE("common.msg.login_0013", "비밀번호가 7일 이후에 만료 예정입니다. </br> 비밀번호 변경이 필요합니다."),
    USER_REST("common.msg.login_0011", "고객님은 장기 미접속으로 휴면회원 전환 상태 입니다."),
    USER_DI_FAIL("common.msg.login_0015", "인증 정보가 올바르지 않습니다. </br>관리자에게 문의해 주시기 바랍니다.")
    ;

    private String message;

    private String messageKey;

    LoginResultCode (String messageKey, String message) {
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
