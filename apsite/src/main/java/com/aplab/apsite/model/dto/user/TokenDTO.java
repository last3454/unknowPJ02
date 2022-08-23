package com.aplab.apsite.model.dto.user;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class TokenDTO {

    private String userCd;

    private String userNm;

    private String loginId;

    private String lastLoginDt;
    
    private String masterFlag;

    private List<String> roles;

    private List<String> groups;

    private String compCd;
}
