package com.aplab.apsite.model.dto.user;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespLoginDTO {
    
    private String userCd;

    private String loginId;

    private String userNm;
    
    private String compCd;

    private List<String> roles;
    
    private List<String> groups;

    private String lastLoginDt;

    private String token;

    private String ip;

    private String rememberMeToken;
    
    private String masterFlag;

}
