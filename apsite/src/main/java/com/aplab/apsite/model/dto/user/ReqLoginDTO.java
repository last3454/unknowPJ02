package com.aplab.apsite.model.dto.user;

import lombok.Data;

@Data
public class ReqLoginDTO {

    private String loginId;

    private String loginPw;

    private String rememberMe;

    private String loginDupleYn;
}
