package com.aplab.apsite.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aplab.apsite.config.SecurityUser;
import com.aplab.apsite.dbmst.entity.comm.CodeSubEntity;
import com.aplab.apsite.model.dto.comm.CommUploadDTO;
import com.aplab.apsite.model.enums.CommonResultCode;
import com.aplab.apsite.model.vo.ResponseVO;
import com.aplab.apsite.model.vo.SessionVO;
import com.aplab.apsite.service.AWSService;
import com.aplab.apsite.service.CommonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class ApiFileController {

    private final CommonService commonService;
    
    private final AWSService awsService;
    
    @GetMapping()
    public Object findCommUploadList(
        @RequestParam(value="targetKey") String targetKey,
        @RequestParam(value="uploadCd") String uploadCd,
        @RequestParam(value="ver", defaultValue = "0") int ver
    ) {
        if (log.isDebugEnabled()) {
            log.debug("ApiFileController.findCommUploadList => params : { targetKey: {}, uploadCd: {} }", targetKey, uploadCd);
        }

        ResponseVO responseVO = new ResponseVO();
        responseVO.setOk(commonService.findCommUploadList(targetKey, uploadCd, ver));
        return responseVO;
    }
    
    @GetMapping(value="/{seq}")
    @ResponseBody
    @CrossOrigin(value={"*"}, exposedHeaders = { HttpHeaders.CONTENT_DISPOSITION, "filename" })
    public Object findFile(
        @PathVariable(name = "seq") long seq
    ) throws IOException {
    	CommUploadDTO dto = commonService.findCommUpload(seq);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(dto.getFileNm(), "UTF-8"));
        headers.add("filename", dto.getFileNm());
        
        String keyName = "";
        
        if (StringUtils.isNotEmpty(dto.getKeyPath())) {
        	keyName = dto.getKeyPath();
        }
        
        dto.setFileBytea(awsService.download(keyName));
        
        return ResponseEntity.ok()
            .contentLength(dto.getFileBytea().length)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .headers(headers)
            .body(dto.getFileBytea());
    }

    @GetMapping(value="/history")
    public Object findCommUploadHisList(
        @RequestParam(value="targetKey") String targetKey,
        @RequestParam(value="uploadCd") String uploadCd,
        @RequestParam(value="preRecordCd") long preRecordCd,
        @RequestParam(value="reviewFlag", defaultValue="N") String reviewFlag
    ) {
        if (log.isDebugEnabled()) {
            log.debug("ApiFileController.findCommUploadHisList => params : { targetKey: {}, uploadCd: {} }", targetKey, uploadCd);
        }

        ResponseVO responseVO = new ResponseVO();
        
        
        responseVO.setOk(commonService.findCommUploadHisList(targetKey, uploadCd, preRecordCd, reviewFlag));
        return responseVO;
    }
    
    @PostMapping("/fix-file")
    public Object updateFixFileInfo (
            @RequestBody @Valid List<CommUploadDTO> fixFileList,
            @AuthenticationPrincipal SecurityUser securityUser
            ) {
        ResponseVO responseVO = new ResponseVO();
        SessionVO sessionVO = securityUser.getSessionVO();
        List<String> groups = sessionVO.getGroups();
        List<String> authGroups = new ArrayList<String>();
        
        // START : RMQC 권한체크
        List<CodeSubEntity> codeList = commonService.findCodeListByGroupCd("RAW_RMQC_TYPE", sessionVO.getLangCd());
        
        codeList.forEach(entity -> {
            authGroups.add(entity.getCode());
        });
        
        boolean isAuth = false;
        for (String groupCode : authGroups) {
            if (groups.contains(groupCode)) {
                isAuth = true;
            }
        }
        
        if (!isAuth) { //RMQC 권한이 없으면 실패
            responseVO.setOk(CommonResultCode.NO_AUTH, null);
            return responseVO;
        }
        // END : RMQC 권한체크
        
        if (fixFileList.size() == 0) {
            responseVO.setOk(CommonResultCode.NO_ESSENTIAL_DATA, null);
            return responseVO;
        }
        
        commonService.updateFixFileInfo(fixFileList, sessionVO);
        responseVO.setCreateOk("SUCC");
        
        return responseVO;
    }
}
