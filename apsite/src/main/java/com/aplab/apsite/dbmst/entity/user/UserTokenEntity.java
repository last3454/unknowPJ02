package com.aplab.apsite.dbmst.entity.user;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user_token")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTokenEntity {

    @Id
    private String token;

    private String userCd;

    private String loginId;

    private String loginIp;

    private String loginPw;

    private String typeCd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp expireDtm;

    private String jsonData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private Timestamp regDtm;
}
