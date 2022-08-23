package com.aplab.apsite.model.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SessionRoleVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String userCd;

    private String roleCd;
}
