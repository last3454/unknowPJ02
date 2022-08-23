package com.aplab.apsite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplab.apsite.config.SecurityUser;
import com.aplab.apsite.model.vo.ResponseVO;
import com.aplab.apsite.service.CommonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class ApiMenuController {

    private final CommonService commonService;

    /**
     * 메뉴 리스트
     * @param params
     * @return
     */
	@GetMapping("")
	public ResponseEntity<ResponseVO> menuList(
	        @AuthenticationPrincipal SecurityUser securityUser
	        ){
		ResponseVO responseVo = new ResponseVO();

		responseVo.setOk(commonService.findMenuLevelList(securityUser.getSessionVO().getGroups()));
		return ResponseEntity.ok(responseVo);
	}
	
	/**
	 * 페이지 리스트 조회
	 * @param params
	 * @return
	 */
	@GetMapping("/page")
	public ResponseEntity<ResponseVO> pageList(
            @AuthenticationPrincipal SecurityUser securityUser
	        ){
		ResponseVO responseVo = new ResponseVO();

		responseVo.setOk(commonService.findPageLevelList(securityUser.getSessionVO().getGroups()));
		return ResponseEntity.ok(responseVo);
	}
}
