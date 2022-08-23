package com.aplab.apsite.dbmst.repo.user;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aplab.apsite.dbmst.entity.user.UserTokenEntity;

@Repository
public interface UserTokenRepo extends CrudRepository<UserTokenEntity, String> {

	List<UserTokenEntity> findByUserCd(String userCd);

    int deleteByExpireDtmLessThan(Timestamp expireDtm);

    int deleteByUserCdAndTypeCd(String userCd, String typeCd);
}
