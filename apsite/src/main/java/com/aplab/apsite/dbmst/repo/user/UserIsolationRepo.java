package com.aplab.apsite.dbmst.repo.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.aplab.apsite.dbmst.entity.user.UserIsolationEntity;

public interface UserIsolationRepo extends CrudRepository<UserIsolationEntity, String> {

	public UserIsolationEntity findByUserDi(String userDi);

	@Query("select m from UserIsolationEntity m where add_months(to_date(to_char(reg_dtm,'YYYY-MM-DD'),'YYYY-MM-DD'), (1 * :addMonth)) < to_date(to_char(sysdate,'YYYY-MM-DD'),'YYYY-MM-DD')")
	public List<UserIsolationEntity> findByRegDtm(@Param("addMonth") int addMonth);

}
