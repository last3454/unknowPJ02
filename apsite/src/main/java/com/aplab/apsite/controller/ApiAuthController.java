package com.aplab.apsite.controller;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aplab.apsite.config.BusinessException;
import com.aplab.apsite.config.SecurityUser;
import com.aplab.apsite.dbmst.entity.user.UserEntity;
import com.aplab.apsite.model.dto.comm.CommUploadDTO;
import com.aplab.apsite.model.dto.ingrd.RawMstDTO;
import com.aplab.apsite.model.dto.user.ReqLoginDTO;
import com.aplab.apsite.model.dto.user.ReqSignOkDTO;
import com.aplab.apsite.model.dto.user.RespLoginDTO;
import com.aplab.apsite.model.dto.user.UserDTO;
import com.aplab.apsite.model.enums.LoginResultCode;
import com.aplab.apsite.model.vo.ResponseVO;
import com.aplab.apsite.model.vo.SessionVO;
import com.aplab.apsite.service.AWSService;
import com.aplab.apsite.service.CommonService;
import com.aplab.apsite.service.IngrdService;
import com.aplab.apsite.service.UserService;
import com.aplab.apsite.utils.CommonUtil;
import com.aplab.apsite.utils.ConvertUtil;
import com.aplab.apsite.utils.NiceApiUtil;
import com.aplab.apsite.utils.SignOkUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final IngrdService ingrdService;

    private final AWSService awsService;

    private final CommonService commonService;

    @PostMapping("/signin")
    public ResponseEntity<?> signin (@RequestBody @Valid ReqLoginDTO params, HttpServletRequest request, Errors errors) {
        ResponseVO responseVo = new ResponseVO();
        if (log.isDebugEnabled()) {
            log.debug("AuthController.signin : params = {}", params);
        }

        SessionVO sessionVO = null;

        try {
        	sessionVO = userService.checkLogin(params.getLoginId(), params.getLoginPw(), params.getLoginDupleYn(), request);
        } catch (BusinessException be) {
            responseVo.setBadRequest(be.getCode(), be.getMessage(), be.getMessageKo(), be.getData(), null);
            return ResponseEntity.ok(responseVo);
        }

        String token = userService.convertLoginToken(sessionVO);
        String rememberMeToken = "";

        RespLoginDTO respLoginDto = RespLoginDTO.builder()
            .userCd(sessionVO.getUserCd())
            .userNm(sessionVO.getUserNm())
            .loginId(sessionVO.getLoginId())
            .compCd(sessionVO.getCompCd())
            .roles(sessionVO.getRoles())
            .groups(sessionVO.getGroups())
            .masterFlag(sessionVO.getMasterFlag())
            .token(token)
            .lastLoginDt(sessionVO.getLastLoginDt())
            .ip(CommonUtil.getClientIp(request))
            .rememberMeToken(rememberMeToken)
            .build();

        if ("Y".equals(sessionVO.getPwExpiredYn())) {
        	// 비밀번호 만료
            responseVo.setOk(LoginResultCode.USER_PW_EXPIRED, respLoginDto);
        } else if ("F".equals(sessionVO.getPwExpiredYn())) {
        	// 비밀번호 7일 만료 경고창
            responseVo.setOk(LoginResultCode.USER_PW_SEVEN_EXPIRED, respLoginDto);
        } else {
            responseVo.setOk(respLoginDto);
        }

        return ResponseEntity.ok(responseVo);
    }

    public ResponseEntity<?> signin_bak (@RequestBody @Valid ReqLoginDTO params, HttpServletRequest request, Errors errors) {

        ResponseVO responseVo = new ResponseVO();
        if (log.isDebugEnabled()) {
            log.debug("AuthController.signin : params = {}", params);
        }

        SessionVO sessionVO = null;

        try {

            Authentication auth = null;

            if(params.getLoginId().startsWith("AC") || params.getLoginId().startsWith("AP")) {
                auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("IN_LOGINID_" + params.getLoginId(), params.getLoginPw()));
            }else {
                auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("LOGINID_" + params.getLoginId(), params.getLoginPw()));
            }
            sessionVO = ((SecurityUser)auth.getPrincipal()).getSessionVO();
        } catch (DisabledException e) {
            errors.rejectValue("loginId", "Customize", "회원정보를 찾을수 없습니다. 아이디/비밀번호를 확인해 주세요");
        } catch (BadCredentialsException e) {
            errors.rejectValue("loginId", "Customize", "아이디/비밀번호를 확인해 주세요");
        } catch (Exception e) {
            e.printStackTrace();
            errors.rejectValue("loginId", "Customize", "회원정보를 찾을수 없습니다. 아이디/비밀번호를 확인해 주세요");
        }

        if (errors.hasErrors()) {
            responseVo.setBadRequest("fail", "아이디/비밀번호를 확인해 주세요", errors.getAllErrors());
            return ResponseEntity.ok(responseVo);
        }

        String token = userService.convertLoginToken(sessionVO);
        String rememberMeToken = "";

        if ("Y".equals(params.getRememberMe())) {
            rememberMeToken = userService.convertRememberMeToken(sessionVO);
        }

        RespLoginDTO respLoginDto = RespLoginDTO.builder()
            .userCd(sessionVO.getUserCd())
            .userNm(sessionVO.getUserNm())
            .loginId(sessionVO.getLoginId())
            .compCd(sessionVO.getCompCd())
            .roles(sessionVO.getRoles())
            .groups(sessionVO.getGroups())
            .masterFlag(sessionVO.getMasterFlag())
            .token(token)
            .lastLoginDt(sessionVO.getLastLoginDt())
            .ip(CommonUtil.getClientIp(request))
            .rememberMeToken(rememberMeToken)
            .build();

        responseVo.setOk(respLoginDto);

        return ResponseEntity.ok(responseVo);
    }

    @GetMapping("/signout")
    public ResponseEntity<?> signout (
        @RequestHeader(name="x-session-token", required = false, defaultValue = "") String sessionToken,
        @RequestHeader(name="x-local-token", required = false, defaultValue = "") String localToken
    ) {

        ResponseVO responseVo = new ResponseVO();
        if (log.isDebugEnabled()) {
            log.debug("AuthController.signout : sessionToken = {}, localToken = {}", sessionToken, localToken);
        }

        userService.removeToken(sessionToken, localToken);

        responseVo.setOk(null);

        return ResponseEntity.ok(responseVo);
    }

    @GetMapping("/token-check")
    public ResponseEntity<?> tokenCheck (
        @RequestHeader(name="x-session-token", required = false, defaultValue = "") String sessionToken,
        @RequestHeader(name="x-local-token", required = false, defaultValue = "") String localToken
    ) {
        if (log.isDebugEnabled()) {
            log.debug("AuthController.tokenCheck : sessionToken = {}, localToken = {}", sessionToken, localToken);
        }
        ResponseVO responseVo = null;
        try {
        	responseVo = userService.tokenCheck(sessionToken, localToken);
		} catch (Exception e) {
			responseVo = new ResponseVO();
			responseVo.setBadRequest("C9999", "fail", null);
		}
        return ResponseEntity.ok(responseVo);
    }
}
