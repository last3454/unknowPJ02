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
public class MyInfoUserEntity {

    @Id
    @GenericGenerator(name = "user_cd", strategy = "com.aplab.apsite.utils.CustomIdGenerator",
    parameters = {@Parameter(name = "seqName", value = "USER_MST_SEQ")})
    @GeneratedValue(generator = "user_cd")
    private String userCd;

    private String userNm;

    private String loginId;

    private String email;

    private String mobileNo;

    private String userCompDeptNm;

    private String masterFlag;
}