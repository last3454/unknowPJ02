package com.aplab.apsite.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aplab.apsite.config.SecurityUser;
import com.aplab.apsite.dbmst.entity.user.UserEntity;
import com.aplab.apsite.dbmst.entity.user.UserGroupEntity;
import com.aplab.apsite.dbmst.entity.user.UserRoleEntity;
import com.aplab.apsite.dbmst.repo.user.UserGroupRepo;
import com.aplab.apsite.dbmst.repo.user.UserRepo;
import com.aplab.apsite.dbmst.repo.user.UserRoleRepo;
import com.aplab.apsite.model.dto.user.UserDTO;
import com.aplab.apsite.model.vo.SessionVO;
import com.aplab.apsite.utils.ConvertUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    private final UserRoleRepo userRoleRepo;

    private final UserGroupRepo userGroupRepo;

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug(":: LoginUserDetailsService.loadUserByUsername :: username : {}", username);
        UserEntity userEntity = null;

        //내부망에서 접속
        if (username.startsWith("IN_LOGINID_")) {
            UserDTO userDto = userService.findUserTium(username.substring("IN_LOGINID_".length()));
            userEntity = ConvertUtil.convert(userDto, UserEntity.class);
        } else {
            //외부망에서 접속
            if (username.startsWith("LOGINID_")) {
                userEntity = userRepo.findByLoginIdIgnoreCaseAndStatusCd(username.substring("LOGINID_".length()),"US001");
            } else {
                userEntity = userRepo.findById(username.substring("USERCD_".length())).orElse(null);
            }
        }
        if (userEntity == null) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        List<String> roles = new ArrayList<>();
        List<UserRoleEntity> userRoleList = userRoleRepo.findByUserCd(userEntity.getUserCd());

        if (userRoleList != null && !userRoleList.isEmpty()) {
            userRoleList.forEach(entity -> {
                roles.add(entity.getRoleCd());
            });
        } else {
            roles.add(("BASIC"));
        }

        List<String> groups = new ArrayList<>();
        List<UserGroupEntity> userGroupList = userGroupRepo.findByUserid(userEntity.getLoginId());

        if (userGroupList != null && !userGroupList.isEmpty()) {
        	userGroupList.forEach(entity -> {
        		groups.add(entity.getGroupid());
            });
        } else {
        	//외부망에서 접속시 ROLE이 존재하지 않는다.
        	if("Y".equals(userEntity.getMasterFlag())) {
            	//아무 권한도 없고 마스터 권한일 시 업체 마스터 권한으로 표시
            	groups.add(("SGG000005"));
        	}else {
            	//아무 권한도 없을 시 업체 권한으로 표시
            	groups.add(("SGG000006"));
        	}
        }
        //외부에서 접속했을 시에만 업데이트 처리
        Date lastLoginDt = new Date();
        if(!username.startsWith("IN_LOGINID_")) {
        	// 마지막 로그인 일자 저장
        	userEntity.setLastLoginDt(lastLoginDt);
        	userRepo.save(userEntity);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);

        SessionVO sessionVO = SessionVO.builder()
            .userCd(userEntity.getUserCd())
            .loginId(userEntity.getLoginId())
            .loginPw(userEntity.getLoginPw())
            .userNm(userEntity.getUserNm())
            .compCd(userEntity.getCompCd())
            .masterFlag(userEntity.getMasterFlag())
            .roles(roles)
            .groups(groups)
            .lastLoginDt(sdf.format(lastLoginDt))
            .build();

        return new SecurityUser(sessionVO);
    }
}
