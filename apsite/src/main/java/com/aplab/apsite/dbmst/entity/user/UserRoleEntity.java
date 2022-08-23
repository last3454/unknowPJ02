package com.aplab.apsite.dbmst.entity.user;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
@Table(name = "user_role")
public class UserRoleEntity {
    
    @Id
    private String userCd;

    private String roleCd;

    @Column(updatable = false)
    private String regUserId;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate regDtm;

    private String updUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate updDtm;
}
