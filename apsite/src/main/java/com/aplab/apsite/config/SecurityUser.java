package com.aplab.apsite.config;

import java.util.ArrayList;
import java.util.List;

import com.aplab.apsite.model.vo.SessionVO;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class SecurityUser extends User {
    
    private static final long serialVersionUID = 1L;

    private static final String ROLE_PREFIX = "ROLE_";

    private SessionVO sessionVO;

    public SecurityUser(SessionVO sessionVO) {
        super(sessionVO.getUserCd(), sessionVO.getLoginPw(), makeGrantedAuthority(sessionVO.getRoles()));

        this.sessionVO = sessionVO;
    }

    private static List<GrantedAuthority> makeGrantedAuthority(List<String> roles) {
        List<GrantedAuthority> list = new ArrayList<>();
        roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role)));
        return list;
    }
}
