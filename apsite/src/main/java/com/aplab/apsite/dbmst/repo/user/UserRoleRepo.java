package com.aplab.apsite.dbmst.repo.user;
import java.util.List;

import com.aplab.apsite.dbmst.entity.user.UserRoleEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepo extends CrudRepository<UserRoleEntity, String> {
    
    List<UserRoleEntity> findByUserCd(String userCd);
}
