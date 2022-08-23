package com.aplab.apsite.model.enums;

public enum UserType implements EnumCodeMapperType{
    
    COMP_USER("사용자", "code.user.comp_user"),
    COMP_ADMIN("업체의 관리자", "code.user.comp_admin"),
    EMPLOYE("AP 임직원", "code.user.employe");

    private String codeNm;

    private String codeKey;

    UserType(String codeNm, String codeKey) {
        this.codeNm = codeNm;
        this.codeKey = codeKey;
    }

    @Override
	public String getCode() {
		return name();
	}
	
	@Override
	public String getCodeKey() {
		return codeKey;
	}

	@Override
	public String getCodeNm() {
		return codeNm;
	}
	
	public static UserType findById(String code) {
		if (code == null) {
			return null;
		}
		
		for (UserType e : UserType.values()) {
			if (code.equals(e.getCode())) {
				return e;
			}
		}
		return null;
	}
}
