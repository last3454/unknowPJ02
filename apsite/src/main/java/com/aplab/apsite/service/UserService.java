package com.aplab.apsite.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aplab.apsite.config.BusinessException;
import com.aplab.apsite.config.SHAPasswordEncoder;
import com.aplab.apsite.dbmst.entity.user.UserEntity;
import com.aplab.apsite.dbmst.entity.user.UserGroupEntity;
import com.aplab.apsite.dbmst.entity.user.UserIsolationEntity;
import com.aplab.apsite.dbmst.entity.user.UserLoginLogEntity;
import com.aplab.apsite.dbmst.entity.user.UserRoleEntity;
import com.aplab.apsite.dbmst.entity.user.UserStaffLoginEntity;
import com.aplab.apsite.dbmst.entity.user.UserTokenEntity;
import com.aplab.apsite.dbmst.mapper.UserMapper;
import com.aplab.apsite.dbmst.repo.user.UserGroupRepo;
import com.aplab.apsite.dbmst.repo.user.UserIsolationRepo;
import com.aplab.apsite.dbmst.repo.user.UserLoginLogRepo;
import com.aplab.apsite.dbmst.repo.user.UserRepo;
import com.aplab.apsite.dbmst.repo.user.UserRoleRepo;
import com.aplab.apsite.dbmst.repo.user.UserStaffLoginRepo;
import com.aplab.apsite.dbmst.repo.user.UserTokenRepo;
import com.aplab.apsite.form.MailForm;
import com.aplab.apsite.model.dto.comm.MailDTO;
import com.aplab.apsite.model.dto.myInfo.ReqMyInfoDTO;
import com.aplab.apsite.model.dto.myInfo.ReqPassChangeDTO;
import com.aplab.apsite.model.dto.myInfo.ReqUpdateMyInfoDTO;
import com.aplab.apsite.model.dto.user.ReqJoinDTO;
import com.aplab.apsite.model.dto.user.RespLoginDTO;
import com.aplab.apsite.model.dto.user.TokenDTO;
import com.aplab.apsite.model.dto.user.UserDTO;
import com.aplab.apsite.model.enums.CommonResultCode;
import com.aplab.apsite.model.enums.LoginResultCode;
import com.aplab.apsite.model.enums.UserType;
import com.aplab.apsite.model.vo.ResponseVO;
import com.aplab.apsite.model.vo.SessionVO;
import com.aplab.apsite.utils.CmSecretUtil;
import com.aplab.apsite.utils.CommonUtil;
import com.aplab.apsite.utils.ConvertUtil;
import com.aplab.apsite.utils.JwtTokenUtil;
import com.aplab.apsite.utils.MailUtil;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserTokenRepo userTokenRepo;

    private final UserRepo userRepo;

    private final UserRoleRepo userRoleRepo;

    private final UserGroupRepo userGroupRepo;

    private final UserLoginLogRepo userLoginLogRepo;

    private final UserIsolationRepo userIsolationRepo;

    private final UserStaffLoginRepo userStaffLoginRepo;

    private final UserMapper userMapper;

    private final CommonService commonService;

    private final ErrorInqService errorInqService;

    @Value("${grp.web.i-url}")
    private String INTERNAL_WEB_URL;

    public List<UserTokenEntity> findByUserCd(UserEntity userEntity) {
    	return userTokenRepo.findByUserCd(userEntity.getUserCd());
    }

    public String convertLoginToken(UserEntity userEntity) {
    	HttpServletRequest request = CommonUtil.getRequest();
        String subject = userEntity.getUserCd() + "_" + userEntity.getLoginId();
        Map<String, Object> claims = new HashMap<String, Object>();

        claims.put("userCd", userEntity.getUserCd());
        claims.put("userNm", userEntity.getUserNm());
        claims.put("loginId", userEntity.getLoginId());
        claims.put("compCd", userEntity.getCompCd());
        claims.put("masterFlag", userEntity.getMasterFlag());
        claims.put("lastLoginDt", userEntity.getLastLoginDt());

        List<String> roles = new ArrayList<>();
        List<UserRoleEntity> userRoleList = userRoleRepo.findByUserCd(userEntity.getUserCd());

        if (userRoleList != null && !userRoleList.isEmpty()) {
            userRoleList.forEach(entity -> {
                roles.add(entity.getRoleCd());
            });
        } else {
            roles.add(("BASIC"));
        }
        claims.put("roles", roles);

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
        claims.put("groups", groups);

        Gson gson = new Gson();
        String token = jwtTokenUtil.generateToken(subject, claims, 1L * 60L * 60L);
        long expireDtm = (new Date()).getTime() + 1L * 60L * 60L * 1000L;

        UserTokenEntity userTokenEntity = UserTokenEntity.builder()
            .token(token)
            .loginId(userEntity.getLoginId())
            .loginPw(userEntity.getLoginPw())
            .loginIp(CommonUtil.getClientIp(request))
            .typeCd("LOGIN")
            .userCd(userEntity.getUserCd())
            .jsonData(gson.toJson(claims))
            .expireDtm(new Timestamp(expireDtm))
            .build();

        userTokenRepo.save(userTokenEntity);

        return token;
    }

    @Transactional
    public String convertLoginToken(SessionVO sessionVO) {
    	HttpServletRequest request = CommonUtil.getRequest();
        String subject = sessionVO.getUserCd() + "_" + sessionVO.getLoginId();
        Map<String, Object> claims = new HashMap<String, Object>();

        claims.put("userCd", sessionVO.getUserCd());
        claims.put("userNm", sessionVO.getUserNm());
        claims.put("loginId", sessionVO.getLoginId());
        claims.put("compCd", sessionVO.getCompCd());
        claims.put("lastLoginDt", sessionVO.getLastLoginDt());
        claims.put("masterFlag", sessionVO.getMasterFlag());

        List<String> roles = sessionVO.getRoles();
        claims.put("roles", roles);

        List<String> groups = sessionVO.getGroups();
        claims.put("groups", groups);

        Gson gson = new Gson();
        String token = jwtTokenUtil.generateToken(subject, claims, 1L * 60L * 60L);
        long expireDtm = (new Date()).getTime() + 1L * 60L * 60L * 1000L;
        userTokenRepo.deleteByUserCdAndTypeCd(sessionVO.getUserCd().toString(),"LOGIN");
        UserTokenEntity userTokenEntity = UserTokenEntity.builder()
            .token(token)
            .loginId(sessionVO.getLoginId())
            .loginPw(sessionVO.getLoginPw())
            .loginIp(CommonUtil.getClientIp(request))
            .typeCd("LOGIN")
            .userCd(sessionVO.getUserCd())
            .jsonData(gson.toJson(claims))
            .expireDtm(new Timestamp(expireDtm))
            .build();

        userTokenRepo.save(userTokenEntity);

        return token;
    }

    public String convertRememberMeToken(SessionVO sessionVO) {
        String subject = sessionVO.getUserCd() + "_" + sessionVO.getLoginId();
        Map<String, Object> claims = new HashMap<String, Object>();

        claims.put("userCd", sessionVO.getUserCd());
        claims.put("userNm", sessionVO.getUserNm());
        claims.put("loginId", sessionVO.getLoginId());
        claims.put("compCd", sessionVO.getCompCd());

        List<String> roles = sessionVO.getRoles();
        claims.put("roles", roles);

        List<String> groups = sessionVO.getGroups();
        claims.put("groups", groups);

        Gson gson = new Gson();
        String token = jwtTokenUtil.generateToken(subject, claims, 365L * 24L * 60L * 60L);
        long today = (new Date()).getTime();
        long expireDtm = today + 365L * 24L * 60L * 60L * 1000L;

        UserTokenEntity userTokenEntity = UserTokenEntity.builder()
            .token(token)
            .loginId(sessionVO.getLoginId())
            .loginPw(sessionVO.getLoginPw())
            .typeCd("REMEMBER_ME")
            .userCd(sessionVO.getUserCd())
            .jsonData(gson.toJson(claims))
            .expireDtm(new Timestamp(expireDtm))
            .build();

        userTokenRepo.save(userTokenEntity);

        return token;
    }

    public ResponseVO tokenCheck(String sessionToken, String localToken) {
        ResponseVO responseVo = new ResponseVO();
        HttpServletRequest request = CommonUtil.getRequest();
        if (sessionToken != null && !sessionToken.equals("")) {
            Claims claims = jwtTokenUtil.getAllClaimsFromToken(sessionToken);
            Date expiration = jwtTokenUtil.getClaimFromToken(claims, Claims::getExpiration);

            if (expiration.after(new Date())) {
                UserTokenEntity userTokenEntity = userTokenRepo.findById(sessionToken).orElse(null);
                if (userTokenEntity != null) {
                    String jsonData = userTokenEntity.getJsonData();
                    TokenDTO tokenDTO = (new Gson()).fromJson(jsonData, TokenDTO.class);

                    RespLoginDTO respLoginDto = RespLoginDTO.builder()
                        .userCd(tokenDTO.getUserCd())
                        .userNm(tokenDTO.getUserNm())
                        .loginId(tokenDTO.getLoginId())
                        .compCd(tokenDTO.getCompCd())
                        .roles(tokenDTO.getRoles())
                        .masterFlag(tokenDTO.getMasterFlag())
                        .groups(tokenDTO.getGroups())
                        .lastLoginDt(tokenDTO.getLastLoginDt())
                        .token(sessionToken)
                        .ip(CommonUtil.getClientIp(request))
                        .build();

                    responseVo.setOkWithCode("T0001", "", respLoginDto);

                    return responseVo;
                }
            }
        }

        if (localToken != null && !localToken.equals("")) {
            Claims claims = jwtTokenUtil.getAllClaimsFromToken(localToken);
            Date expiration = jwtTokenUtil.getClaimFromToken(claims, Claims::getExpiration);

            if (expiration.after(new Date())) {
                UserTokenEntity userTokenEntity = userTokenRepo.findById(localToken).orElse(null);

                if (userTokenEntity != null) {
                    String jsonData = userTokenEntity.getJsonData();
                    TokenDTO tokenDTO = (new Gson()).fromJson(jsonData, TokenDTO.class);

                    try {
                        UserEntity userEntity = userRepo.findById(tokenDTO.getUserCd()).orElse(null);

                        if (userEntity != null && userEntity.getLoginPw().equals(userTokenEntity.getLoginPw())) {
                            String token = this.convertLoginToken(userEntity);
                            Date lastLoginDt = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);
                            RespLoginDTO respLoginDto = RespLoginDTO.builder()
                                .userCd(tokenDTO.getUserCd())
                                .userNm(tokenDTO.getUserNm())
                                .loginId(tokenDTO.getLoginId())
                                .compCd(tokenDTO.getCompCd())
                                .masterFlag(tokenDTO.getMasterFlag())
                                .roles(tokenDTO.getRoles())
                                .groups(tokenDTO.getGroups())
                                .token(token)
                                .lastLoginDt("")
                                .ip(sdf.format(lastLoginDt))
                                .build();

                            responseVo.setOkWithCode("T0002", "", respLoginDto);
                        }

                        return responseVo;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        responseVo.setOkWithCode("F0000", "로그인 정보 없음", null);
        return responseVo;
    }

    public void removeToken(String sessionToken, String localToken) {

        if (StringUtils.isNotEmpty(sessionToken)) {
            UserTokenEntity entity = userTokenRepo.findById(sessionToken).orElse(null);
            if (entity != null) {
                userTokenRepo.delete(entity);
            }
        }
        if (StringUtils.isNotEmpty(localToken)) {
            UserTokenEntity entity = userTokenRepo.findById(localToken).orElse(null);
            if (entity != null) {
                userTokenRepo.delete(entity);
            }
        }
    }

    public List<UserEntity> findByCompCd(String compCd) {
    	return userRepo.findByCompCd(compCd);
    }

    public UserEntity findById(String userCd) {
    	return userRepo.findById(userCd).orElse(null);
    }

    public UserEntity idCheck(String loginId) {
    	return userRepo.findByLoginIdIgnoreCase(loginId);
    }

    public int updateMasterFlag(String compCd) {
    	return userRepo.updateMasterFlag(compCd);
    }

    public void deleteUser(UserEntity userEntity) {
    	userRepo.delete(userEntity);
    }

    public UserEntity idSearch(String email) {
    	UserEntity userEntity = userRepo.findByEmail(email);

    	if(userEntity == null) {
    		return null;
    	}

		MailForm form 		= new MailForm();
		String mailTile 	= "아이디 찾기";
		String mailContent 	= form.getMailContent(MailDTO.MAIL_CON6, userEntity.getLoginId(), userEntity.getEmail());
		MailUtil.send("", "", userEntity.getEmail(), null, mailTile, mailContent, null);
    	return userEntity;
    }

    public UserEntity emailCheck(String email) {
    	return userRepo.findByEmail(email);
    }

    public boolean pwdCheck(ReqMyInfoDTO dto) {
        // 기존 DB에 저장되어있는 PWD를 가져온다.
        String encodedPwd = userMapper.findPwdByUserCd(dto.getUserCd());
        String loginPw = CmSecretUtil.getInstance().encodeSha512(dto.getPwd());

        SHAPasswordEncoder encoder = new SHAPasswordEncoder();
        return encoder.matches(loginPw, encodedPwd);
    }

	@Transactional
	public String insertJoin(ReqJoinDTO params) {
		UserEntity userEntity = ConvertUtil.convertWithSession(params, UserEntity.class, null);
		long pwExpiredDt = CommonUtil.getDateAddDays(new Date(), 91l).getTime();

		userEntity.setLoginPw(userEntity.getLoginPw());
		userEntity.setRegUserId(userEntity.getLoginId());
		userEntity.setUpdUserId(userEntity.getLoginId());
		userEntity.setPwExpiredDt(new Timestamp(pwExpiredDt));
		userEntity.setDelYn("N");
		userEntity.setRestYn("N");
		userRepo.save(userEntity);

		//[S]회원 가입 시 승인 메일 발송
		if(StringUtils.isNotEmpty(userEntity.getUserCd())) {
			try {

				MailForm form 		= new MailForm();
				String mailTile 	= "회원가입 메일";
				String mailContent 	= form.getMailContent(MailDTO.MAIL_CON1, userEntity.getLoginId(), userEntity.getEmail());
				//회원 가입 대기승인 메일 발송
				MailUtil.send("", "", userEntity.getEmail(), null, mailTile, mailContent, null);
				userEntity.setStatusCd("US000");
				//마스터 이면 AP 담당자에게 발송
				if("Y".equals(userEntity.getMasterFlag())) {
					//userEntity.setStatusCd("US001");
					//※※※※※※마스터 이면 업체 등록 후 메일 발송 되게 변경※※※※※
					//담당자 없을 시 default로 되어있어 있는 메일에 노출
//					CompEntity compEntity = compRepo.findByCompCd(userEntity.getCompCd());
//					compEntity.setRegUserCd(userEntity.getUserCd());
//					compEntity.setUpdUserCd(userEntity.getUserCd());
//					if(StringUtils.isNotEmpty(compEntity.getCompApUserId())) {
//						UserDTO apUserDTO = this.findUserTium(compEntity.getCompApUserId());
//						if(apUserDTO != null && StringUtils.isNotEmpty(apUserDTO.getEmail())) {
//							HashMap<String, String> map = new HashMap<String, String>();
//							map.put("compNm", compEntity.getCompNm());
//							mailContent = form.getMailContent(MailDTO.MAIL_APPR_CON3, compEntity.getCompApUserId(),"", map);
//							MailUtil.send("", "", apUserDTO.getEmail(), null, mailTile, mailContent, null);
//						}
//					}
				}else {
					//userEntity.setStatusCd("US000");
					//마스터가 아니면 각 회사 마스터에게 메일 발송
					UserEntity masterEntity = userRepo.findByCompCdAndMasterFlag(userEntity.getCompCd(), "Y");
					if(masterEntity != null) {
						//회원 가입 요청승인 메일 발송
						mailContent = form.getMailContent(MailDTO.MAIL_CON2, userEntity.getLoginId(), userEntity.getEmail());
						MailUtil.send("", "", masterEntity.getEmail(), null, mailTile, mailContent, null);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//[E]회원 가입 시 승인 메일 발송
		return userEntity.getUserCd();
	}

	@Transactional
	public String updatePassChange(ReqPassChangeDTO dto) {
		UserEntity userEntity = userRepo.findById(dto.getUserCd()).orElse(null);

		if (userEntity == null) {
			throw new BusinessException(CommonResultCode.NO_DATA);
		}

        if ("PASS_EXPIRED".equals(dto.getType())) {
            if (!dto.getNowLoginPw().equals(userEntity.getLoginPw())) {
                throw new BusinessException(LoginResultCode.FAIL_NOW_PASSwORD);
            }
        }
        //휴면 해제 일때는 없을 수 있다.
        if(userEntity.getLoginPw() != null) {
    		if (userEntity.getLoginPw().equals(dto.getLoginPw())) {
    			throw new BusinessException(LoginResultCode.DUPLE_PASSWORD0);
    		}
    		if (dto.getLoginPw().equals(userEntity.getPrevLoginPw1()) || dto.getLoginPw().equals(userEntity.getPrevLoginPw2())) {
    			throw new BusinessException(LoginResultCode.DUPLE_PASSWORD1);
    		}
        }

		if("Y".equals(dto.getRestClearYn())) {
			//휴면회원 정보 -> 사용자 테이블로 이동
			UserIsolationEntity userIsolationEntity = userIsolationRepo.findById(dto.getUserCd()).orElse(null);
			userEntity.setUserNm(userIsolationEntity.getUserNm());
			userEntity.setLoginId(userIsolationEntity.getLoginId());
			userEntity.setEmail(userIsolationEntity.getEmail());
			userEntity.setStatusCd("US001");
			userEntity.setDeptNm(userIsolationEntity.getDeptNm());
			userEntity.setCompCd(userIsolationEntity.getCompCd());
			userEntity.setMobileNo(userIsolationEntity.getMobileNo());
			userEntity.setUserDi(userIsolationEntity.getUserDi());
			userEntity.setRestYn("N");
			userEntity.setUserCompDeptNm(userIsolationEntity.getUserCompDeptNm());
			userIsolationRepo.delete(userIsolationEntity);
		}

		long pwExpiredDt = CommonUtil.getDateAddDays(new Date(), 91l).getTime();

		userEntity.setPrevLoginPw2(userEntity.getPrevLoginPw1());
		userEntity.setPrevLoginPw1(userEntity.getLoginPw());
		userEntity.setLoginPw(dto.getLoginPw());
		userEntity.setPwFailCnt(0); // 비밀번호 실패횟수 초기화
		userEntity.setPwExpiredDt(new Timestamp(pwExpiredDt));
		return userEntity.getUserCd();
	}

	@Transactional
	public String updatePassChangeNextTime(String userCd) {
		UserEntity userEntity = userRepo.findById(userCd).orElse(null);

		if (userEntity == null) {
			throw new BusinessException(CommonResultCode.NO_DATA);
		}

		long pwExpiredDt = CommonUtil.getDateAddDays(new Date(), 91l).getTime();
		userEntity.setPwExpiredDt(new Timestamp(pwExpiredDt));
		return userEntity.getUserCd();
	}

	public UserDTO findUserGrp(String loginId, String statusCd) {
		return userMapper.findUserGrp(loginId,statusCd);
	}

	public UserDTO findUserTium(String userid) {
		return userMapper.findUserId(userid);
	}

	public List<UserDTO> findUserNmTiumList(String userNm, String managerYn) {
		return userMapper.findUserNmTiumList(userNm, managerYn);
	}

    @Transactional
	public String updateInfo(ReqUpdateMyInfoDTO dto, SessionVO sessionVO) {
        dto.setUserCd(sessionVO.getUserCd());
        dto.setLoginId(sessionVO.getLoginId());
		UserEntity userEntity = userRepo.findById(dto.getUserCd()).orElse(null);

		if (userEntity == null) {
			throw new BusinessException("NO_DATA", "데이터를 찾을 수 없습니다.");
		}

        UserEntity newUserEntity = ConvertUtil.convertWithSession(dto, UserEntity.class, sessionVO);

        try {
            // 사용자 정보 업데이트시 로그 쌓기
            commonService.insertUserActionLog("MYINFO", newUserEntity, sessionVO);
        } catch(Exception e) {
            e.printStackTrace();
        }

        ConvertUtil.mergeWithSession(userEntity, newUserEntity, sessionVO);

		return dto.getLoginId();
	}

    public SessionVO checkLogin(String loginId, String loginPw, String dupleYn, HttpServletRequest request) throws BusinessException{

    	boolean isAp = false;
    	UserDTO userDto = userMapper.findUserGrp(loginId.toUpperCase(), "US001");

    	//휴면회원 체크
    	if(userDto != null && "Y".equals(userDto.getRestYn())) {
    		throw new BusinessException(LoginResultCode.USER_REST);
    	}

    	if(userDto == null) {
    		//내부 망일때만 로그인 하게 추가
    		if(INTERNAL_WEB_URL.indexOf(request.getServerName()) > -1 ) {
    			isAp = true;
    			userDto = this.findUserTium(loginId.toUpperCase());
    		}
    	}

        if (userDto == null) {
            throw new BusinessException(LoginResultCode.USER_NOT_FOUND);
        }

        UserEntity userEntity = ConvertUtil.convert(userDto, UserEntity.class);

        String ip = CommonUtil.getClientIp(request);
        //TODO - 현재는 토큰을 무조건 생성하게 해놔서 동일한 IP로 들어와도 토큰을 새로 생성해서
        //기존 종류가 다른 브라우저에서 실행시 토큰이 바뀜
        //해당 부분 나중에 토큰 만료 시간 체크해서 만료 되지 않았을 시 토큰 풀어서
        //다시 sessionVo에 넣어주는 작업이 필요하다.
        if ("N".equals(dupleYn) && userEntity != null) {
        	List<UserTokenEntity> list = this.findByUserCd(userEntity);
        	if(list != null) {
        		list.forEach(entity -> {
	    			if(!ip.equals(entity.getLoginIp())) {
	    				throw new BusinessException(LoginResultCode.USER_DUPLE);
	    			}
        		});
        	}
        }

        if(!isAp) {
        	return this.checkUserLogin(userEntity, loginPw, ip);
        }else {
        	return this.checkEmployeeLogin(userEntity, loginPw, ip);
        }
    }

    public SessionVO checkUserLogin(UserEntity userEntity, String loginPw, String ip) {

        // 로그인 이력
        UserLoginLogEntity logEntity = userLoginLogRepo.save(UserLoginLogEntity.builder()
            .userCd(userEntity.getUserCd())
            .loginId(userEntity.getLoginId())
            .userType("Y".equals(userEntity.getMasterFlag()) ? UserType.COMP_ADMIN : UserType.COMP_USER)
            .loginIp(ip)
            .flag("P")
            .build());

        // 비밀번호 미설정
        if (StringUtils.isEmpty(userEntity.getLoginPw())) {
        	logEntity.setFlag("F");
        	userLoginLogRepo.save(logEntity);
            throw new BusinessException(LoginResultCode.USER_PW_RESET);
        }

        // 비밀번호 5회 이상 실패
        if (userEntity.getPwFailCnt() >= 5) {
        	logEntity.setFlag("F");
        	userLoginLogRepo.save(logEntity);
            throw new BusinessException(LoginResultCode.USER_PW_RESET_FAIL_LIMIT);
        }

        // 비밀번호 불일치
        if (!loginPw.equals(userEntity.getLoginPw())) {
        	logEntity.setFlag("F");
        	userLoginLogRepo.save(logEntity);
            userEntity.setPwFailCnt(userEntity.getPwFailCnt() + 1);
            userRepo.save(userEntity);

            if (userEntity.getPwFailCnt() >= 5) {
                throw new BusinessException(LoginResultCode.USER_PW_RESET_FAIL_LIMIT);
            }

            Map<String, Integer> map = new HashMap<>();
            map.put("max", 5);
            map.put("fail", userEntity.getPwFailCnt());

            throw new BusinessException(LoginResultCode.USER_PW_FAIL, map);
        }

        // 로그인 이력
        userLoginLogRepo.save(UserLoginLogEntity.builder()
            .userCd(userEntity.getUserCd())
            .loginId(userEntity.getLoginId())
            .userType("Y".equals(userEntity.getMasterFlag()) ? UserType.COMP_ADMIN : UserType.COMP_USER)
            .loginIp(ip)
            .build());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);
        Date lastLoginDt = new Date();

        userEntity.setPwFailCnt(0); // 비밀번호 실패건수 초기화
        userEntity.setLastLoginDt(lastLoginDt); // 마지막 로그인 일자
        userRepo.save(userEntity);

        List<String> roles = new ArrayList<>();
        List<String> groups = new ArrayList<>();

        roles.add("BASIC");

        if ("Y".equals(userEntity.getMasterFlag())) {
            roles.add("COMP_ADMIN");
            groups.add(("SGG000005")); // 업체 마스터 권한으로 표시
        } else {
            groups.add(("SGG000006")); // 업체 일반 사용자 권한
        }

        String pwExpiredYn = "N";
        if(userEntity.getPwExpiredDt() != null) {
        	if(userEntity.getPwExpiredDt().getTime() <= lastLoginDt.getTime()) {
        		pwExpiredYn = "Y";
        	}else {
        		Date compDate = CommonUtil.getDateAddDays(userEntity.getPwExpiredDt(), -7);
            	if(compDate.getTime() < lastLoginDt.getTime()) {
            		pwExpiredYn = "F";
            	}
        	}
        }

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
            .pwExpiredYn(pwExpiredYn)
            .restYn(userEntity.getRestYn())
            .build();
        logEntity.setFlag("S");
        return sessionVO;
    }

    public SessionVO checkEmployeeLogin(UserEntity userEntity, String loginPw, String ip) {

    	// 로그인 이력
        UserLoginLogEntity logEntity = userLoginLogRepo.save(UserLoginLogEntity.builder()
            .userCd(userEntity.getUserCd())
            .loginId(userEntity.getUserCd())
            .userType(UserType.EMPLOYE)
            .loginIp(ip)
            .flag("P")
            .build());

        UserStaffLoginEntity userStaffLoginEntity = userStaffLoginRepo.findById(userEntity.getUserCd())
                .orElse(UserStaffLoginEntity.builder()
                        .userId(userEntity.getUserCd())
                        .regUserCd(userEntity.getUserCd())
                        .updUserCd(userEntity.getUserCd())
                        .pwFailCnt(0).build());

        // SSM_USERMASTER 테이블의 비밀번호와 다르면 변경 되었다고 판단
        if (!userEntity.getLoginPw().equals(userStaffLoginEntity.getUserPw())) {
            userStaffLoginEntity.setUserPw(userEntity.getLoginPw());
            userStaffLoginEntity.setPwFailCnt(0);

            userStaffLoginRepo.save(userStaffLoginEntity);
        }

        if (userStaffLoginEntity.getPwFailCnt() >= 5) {
        	logEntity.setFlag("F");
        	userLoginLogRepo.save(logEntity);
            throw new BusinessException(LoginResultCode.USER_AP_PW_RESET_FAIL_LIMIT);
        }

        // 비밀번호 불일치
        if (!loginPw.equals(userEntity.getLoginPw())) {
        	logEntity.setFlag("F");
        	userLoginLogRepo.save(logEntity);
            userStaffLoginEntity.setPwFailCnt(userStaffLoginEntity.getPwFailCnt() + 1);
            userStaffLoginRepo.save(userStaffLoginEntity);

            if (userStaffLoginEntity.getPwFailCnt() >= 5) {
                throw new BusinessException(LoginResultCode.USER_PW_RESET_FAIL_LIMIT);
            }

            Map<String, Integer> map = new HashMap<>();
            map.put("max", 5);
            map.put("fail", userStaffLoginEntity.getPwFailCnt());

            throw new BusinessException(LoginResultCode.USER_PW_FAIL, map);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);
        Date lastLoginDt = new Date();

        userStaffLoginEntity.setPwFailCnt(0); // 비밀번호 실패건수 초기화
        userStaffLoginEntity.setLastLoginDt(lastLoginDt); // 마지막 로그인 일자
        userStaffLoginRepo.save(userStaffLoginEntity);

        List<String> roles = Arrays.asList("BASIC", "COMP_ADMIN", "ADMIN");
        List<String> groups = new ArrayList<>();

        List<UserGroupEntity> userGroupList = userGroupRepo.findByUserid(userEntity.getLoginId());

        if (userGroupList != null && !userGroupList.isEmpty()) {
            userGroupList.forEach(entity -> {
                groups.add(entity.getGroupid());
            });
        } else {
            groups.add(("SGG000006")); // 아무 권한도 없을 시 업체 권한으로 표시
        }

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
            .pwExpiredYn("N")
            .build();
        logEntity.setFlag("S");
        return sessionVO;
    }

    public UserEntity findUserDi(String userDi) {
    	return userRepo.findByUserDi(userDi);
    }

    public UserIsolationEntity findIsolationUserDi(String userDi) {
    	return userIsolationRepo.findByUserDi(userDi);
    }

    // 회사계정 유저 리스트
    public List<UserDTO> findByCompCdList(String compCd) {
		return userMapper.findCompUserList(compCd);
	}

	@Transactional
	public void updateRetire(String userCd, SessionVO sessionVO) {
		UserEntity userEntity = userRepo.findById(userCd).orElse(null);
	    if (userEntity == null) {
	        throw new BusinessException("NO_DATA", "데이터를 찾을 수 없습니다.");
	    }
		String rtnUserCd = commonService.updateUserState(userEntity, sessionVO, "RETIRE");
		if(StringUtils.isNotEmpty(rtnUserCd)) {
			//회원 탈퇴 - 문의사항 분리보관
			errorInqService.transferErrorIsolation(userEntity.getUserCd());
			MailForm form = new MailForm();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userNm", userEntity.getUserNm().toString());
			String mailContent 	= form.getMailContent(MailDTO.MAIL_CON9, userEntity.getLoginId(), userEntity.getEmail(), params);
			MailUtil.send("", "", userEntity.getEmail(), null, "회원 탈퇴 메일", mailContent, null);
		}
	}

	@Transactional
	public void dormantUser() {
		List<UserEntity> list = userRepo.findByDelYnAndLastLoginDt(12);
		if(list != null) {
			//휴면 회원 자체가 많지 않을 꺼라서 단건으로 처리
			list.forEach((entity) -> {
				try {

					UserIsolationEntity saveEntity = userIsolationRepo.save(UserIsolationEntity.builder()
						.userCd(entity.getUserCd())
						.userNm(entity.getUserNm())
						.userDi(entity.getUserDi())
						.compCd(entity.getCompCd())
						.email(entity.getEmail())
						.loginId(entity.getLoginId())
						.deptNm(entity.getDeptNm())
						.mobileNo(entity.getMobileNo())
						.loginPw(entity.getLoginPw())
						.userCompDeptNm(entity.getUserCompDeptNm())
						.regUserId("batch")
						.build()
					);

					if(saveEntity != null) {
						//회원 변경
						commonService.updateUserState(entity, SessionVO.builder().userCd("batch").build(),"REST");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	@Transactional
	public void dormantUserAlarm() {
		List<UserEntity> list = userRepo.findByDelYnAndLastLoginDt(11);
		if(list != null) {
			MailForm form = new MailForm();
			String mailTile = "휴면 안내 메일";
			HashMap<String, String> params = new HashMap<String, String>();
			list.forEach((entity) -> {
				try {
					params.put("userNm", entity.getUserNm().toString());
					params.put("lastLoginDt", entity.getLastLoginDt().toString());
					String mailContent 	= form.getMailContent(MailDTO.MAIL_CON7, entity.getLoginId(), entity.getEmail(), params);
					MailUtil.send("", "", entity.getEmail(), null, mailTile, mailContent, null);
				} catch (Exception e) {
					e.printStackTrace();
				}

			});
		}
	}


	@Transactional
	public void threeYeardormantUser() {
		List<UserIsolationEntity> list = userIsolationRepo.findByRegDtm(36);
		if(list != null) {
			list.forEach(obj -> {
				//휴면회원 전환 후, 3년이 지난 계정 및 분리보관된 오류 문의 완전파기 처리되도록 구현
				userIsolationRepo.deleteById(obj.getUserCd());
				errorInqService.deleteErrorIsolation(obj.getUserCd());
			});
		}

		//탈퇴 후, 3년이 지난 분리보관된 오류 문의 완전파기 처리되도록 구현
		List<UserEntity> userList = userRepo.findByDelYnAndUpdDtm(36);
		if(userList != null) {
			userList.forEach(obj -> {
				//탈퇴 후, 3년이 분리보관된 오류 문의 완전파기 처리되도록 구현
				errorInqService.deleteErrorIsolation(obj.getUserCd());
			});
		}
	}

    public void executeExpiredTokenDelete() {
        long pwExpiredDt = new Date().getTime();
        userTokenRepo.deleteByExpireDtmLessThan(new Timestamp(pwExpiredDt));
    }

    public String findUserDiLog(String userDi) {
        return userMapper.findUserDiLog(userDi);
    }

    public String findTiumGroupEmail(String groupId) {
    	return userMapper.findTiumGroupEmail(groupId);
    }
}