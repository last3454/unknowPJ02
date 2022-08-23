package com.aplab.apsite.dbmst.repo.user;

import org.springframework.data.repository.CrudRepository;

import com.aplab.apsite.dbmst.entity.user.UserActionLogEntity;

public interface UserActionLogRepo extends CrudRepository<UserActionLogEntity, Long> {
    
}
