package com.aplab.apsite.dbmst.entity.user;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.aplab.apsite.model.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user_login_log")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserLoginLogSeq")
    @SequenceGenerator(sequenceName = "USER_LOGIN_LOG_SEQ", name = "UserLoginLogSeq", allocationSize = 1)
    private long seq;

    private String userCd;

    private String loginId;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String loginIp;

    private String flag;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="ASIA/Seoul")
    private Timestamp regDtm;
}
