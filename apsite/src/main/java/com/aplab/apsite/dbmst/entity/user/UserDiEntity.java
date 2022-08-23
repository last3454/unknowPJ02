package com.aplab.apsite.dbmst.entity.user;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "user_di_log")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDiEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "USER_DI_LOG_SEQ")
    @SequenceGenerator(name="USER_DI_LOG_SEQ", sequenceName = "USER_DI_LOG_SEQ", allocationSize = 1)
    private long seq;
    
    private String userCd;
    
    private String userDi;
    
    @Column(updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="ASIA/Seoul")
    private Timestamp regDtm;
}
