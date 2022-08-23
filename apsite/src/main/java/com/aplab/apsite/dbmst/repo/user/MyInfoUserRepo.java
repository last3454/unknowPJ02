package com.aplab.apsite.dbmst.repo.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aplab.apsite.dbmst.entity.user.MyInfoUserEntity;

@Repository
public interface MyInfoUserRepo extends CrudRepository<MyInfoUserEntity, String> {

}
