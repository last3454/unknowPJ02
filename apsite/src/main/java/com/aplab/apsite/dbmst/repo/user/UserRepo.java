package com.aplab.apsite.dbmst.repo.user;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aplab.apsite.dbmst.entity.user.UserEntity;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, String> {

	public UserEntity findByLoginIdIgnoreCase(String loginId);

    public UserEntity findByLoginIdIgnoreCaseAndStatusCd(String loginId, String statusCd);

    public List<UserEntity> findByCompCd(String compCd);

    public UserEntity findByCompCdAndMasterFlag(String compCd, String masterFlag);

	public UserEntity findByEmail(String email);

	public UserEntity findByUserDi(String userDi);

	@Query("select m from UserEntity m where del_yn = 'N' AND rest_yn = 'N' and to_timestamp(to_char(last_login_dt,'YYYY-MM-DD')) = add_months(to_date(to_char(sysdate,'YYYY-MM-DD'),'YYYY-MM-DD'), (-1 * :addMonth))")
	public List<UserEntity> findByDelYnAndLastLoginDt(@Param("addMonth") int addMonth);

    @Modifying(clearAutomatically = true)
    @Query("update UserEntity m set m.masterFlag = 'N' where m.compCd = :compCd")
    public int updateMasterFlag(@Param("compCd") String compCd);

	@Query("select m from UserEntity m where del_yn = 'Y' and to_timestamp(to_char(upd_dtm,'YYYY-MM-DD')) = add_months(to_date(to_char(sysdate,'YYYY-MM-DD'),'YYYY-MM-DD'), (-1 * :addMonth))")
	public List<UserEntity> findByDelYnAndUpdDtm(@Param("addMonth") int addMonth);
}
