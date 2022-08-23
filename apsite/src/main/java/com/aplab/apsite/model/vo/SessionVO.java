package com.aplab.apsite.model.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionVO implements Serializable{

    private static final long serialVersionUID = 1L;

    private String loginId;

    private String loginPw;

    private String userCd;

    private String userNm;

    private String langCd;

    private String compCd;

    private String lastLoginDt;

    private String masterFlag;

    private String pwExpiredYn;

    private String restYn;

    private List<String> roles;

    private List<String> groups;

}
