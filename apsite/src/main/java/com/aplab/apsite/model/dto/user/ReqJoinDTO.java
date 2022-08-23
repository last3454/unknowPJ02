package com.aplab.apsite.model.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReqJoinDTO {

	@NotNull
	private String userNm;
	
	@NotNull
    private String loginId;

	@NotNull
    private String loginPw;
    
	@Email
    private String email;
	
	@NotNull
	private String mobileNo;
    
	@NotNull
    private String compCd;
	
	@NotNull
	private String userDi;
    
    private String masterFlag;   
    
    private String userCompDeptNm;
}
