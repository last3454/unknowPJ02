package com.aplab.apsite.controller;

import java.util.List;

import com.aplab.apsite.config.SecurityUser;
import com.aplab.apsite.model.vo.ResponseVO;
import com.aplab.apsite.service.CommonService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class ApiCodeController {

    private final CommonService commonService;

    @GetMapping("/group-maps")
    public Object findGroupMaps(
        @RequestParam(value="arrGroupCd") List<String> arrGroupCd,
        @RequestParam(value="buffer1", required = false) String buffer1,
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        if (log.isDebugEnabled()) {
            log.debug("ApiCodeController.findGroupMaps => params : { arrGroupCd: {} }", arrGroupCd);
        }

        // try {
        //     Thread.sleep(3 * 1000);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        ResponseVO responseVO = new ResponseVO();
        if (StringUtils.isNotEmpty(buffer1)) {
            responseVO.setOk(commonService.findCodeListMap(arrGroupCd, buffer1, securityUser.getSessionVO().getLangCd()));
        } else {
            responseVO.setOk(commonService.findCodeListMap(arrGroupCd, securityUser.getSessionVO().getLangCd()));
        }
        return responseVO;
    }

    @GetMapping("/tium-group-maps")
    public Object findTiumGroupMaps(
    		@RequestParam(value="arrGroupCd") List<String> arrGroupCd,
            @AuthenticationPrincipal SecurityUser securityUser
    		) {
    	ResponseVO responseVO = new ResponseVO();
        responseVO.setOk(commonService.findTiumCodeListMap(arrGroupCd, securityUser.getSessionVO().getLangCd()));
        return responseVO;
    }
}