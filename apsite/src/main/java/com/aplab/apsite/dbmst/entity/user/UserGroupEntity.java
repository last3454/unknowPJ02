package com.aplab.apsite.dbmst.entity.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "ssm_grpuser_grp")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(UserGroupEntity.Pk.class)
public class UserGroupEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private String groupid;

	@Id
	private String userid;

	private String seqno;

	private String regUserid;

	private String regDtm;

	private String updateUserid;

	private String updateDtm;

	private String buffer1;

	private String adminYn;

	private String adminSendmailYn;

	private String mailRcvYn;

	private String deptCd;

	private String startDtm;

	private String endDtm;

	@Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {

        private static final long serialVersionUID = 1L;

        private String groupid;

        private String userid;
    }
}
