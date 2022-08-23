package com.aplab.apsite.dbmst.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aplab.apsite.config.MstConnMapper;
import com.aplab.apsite.model.dto.user.UserDTO;

@Mapper
@MstConnMapper
public interface UserMapper {
	public UserDTO findUserId(String userid);

	public UserDTO findUserGrp(String loginId, String statusCd);

	public String findPwdByUserCd(String userCd);

	public List<UserDTO> findUserNmTiumList(String userNm, String managerYn);

	public List<UserDTO> findCompUserList(String compCd);

    public String findUserDiLog(String userDi);

    public String findTiumGroupEmail(String groupId);
}
