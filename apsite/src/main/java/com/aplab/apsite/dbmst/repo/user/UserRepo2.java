package com.aplab.apsite.dbmst.repo.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aplab.apsite.dbmst.entity.user.UserEntity2;

@Repository
public interface UserRepo2 extends CrudRepository<UserEntity2, String> {
  
}
