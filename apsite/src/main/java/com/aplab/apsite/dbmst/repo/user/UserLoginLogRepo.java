package com.aplab.apsite.dbmst.repo.user;

import com.aplab.apsite.dbmst.entity.user.UserLoginLogEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginLogRepo extends CrudRepository<UserLoginLogEntity, Long> {
    
}
