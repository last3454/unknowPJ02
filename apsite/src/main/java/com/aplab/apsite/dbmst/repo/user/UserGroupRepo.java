package com.aplab.apsite.dbmst.repo.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aplab.apsite.dbmst.entity.user.UserGroupEntity;

@Repository
public interface UserGroupRepo extends CrudRepository<UserGroupEntity, String> {
	
	List<UserGroupEntity> findByUserid(String userId);
}
