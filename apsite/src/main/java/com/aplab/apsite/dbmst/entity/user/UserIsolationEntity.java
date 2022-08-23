package com.aplab.apsite.dbmst.entity.user;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user_mst_isolation")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserIsolationEntity {

	@Id
	private String userCd;

	private String userNm;

	private String userDi;

	private String loginId;

	private String loginPw;

	private String email;

	private String deptNm;

	private String compCd;

	private String mobileNo;

	private String regUserId;

    @Column(updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="ASIA/Seoul")
	private Timestamp regDtm;

	private String userCompDeptNm;
}
