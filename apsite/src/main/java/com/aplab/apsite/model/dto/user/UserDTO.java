package com.aplab.apsite.model.dto.user;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {

    private String userCd;

    private String userNm;

    private String userDi;

    private String loginId;

    private String loginPw;

    private String email;

    private String statusCd;

    private String deptNm;

    private String compCd;

    private String mobileNo;

    private String masterFlag;

    private String prevLoginPw1;

    private String prevLoginPw2;

    private String userCompDeptNm;

    private int pwFailCnt;

    private String delYn;

    private String restYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="ASIA/Seoul")
    private Timestamp pwExpiredDt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginDt;

    private String regUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="ASIA/Seoul")
    private String regDtm;

    private String updUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="ASIA/Seoul")
    private String updDtm;
}
