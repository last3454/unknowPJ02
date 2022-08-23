package com.aplab.apsite.dbmst.entity.user;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user_mst")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity {

    @Id
    @GenericGenerator(name = "user_cd", strategy = "com.aplab.apsite.utils.CustomIdGenerator",
    parameters = {@Parameter(name = "seqName", value = "USER_MST_SEQ")})
    @GeneratedValue(generator = "user_cd")
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
    @CreationTimestamp
    private Date lastLoginDt;

    @Column(updatable = false)
    private String regUserId;

    @Column(updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="ASIA/Seoul")
    private Timestamp regDtm;

    private String updUserId;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="ASIA/Seoul")
    private Timestamp updDtm;

    public UserEntity(String userNm, String loginId, String email, Date lastLoginDt){
    	this.userNm = userNm;
	    this.loginId = loginId;
	    this.email = email;
	    this.lastLoginDt = lastLoginDt;
    }
}