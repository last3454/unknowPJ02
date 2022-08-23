package com.aplab.apsite.dbmst.entity.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "ssm_usermaster")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String vUserid;

    private String vUsernm;

    private String vUsernmEn;

    private String vEmail;
}