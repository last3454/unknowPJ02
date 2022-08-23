package com.aplab.apsite.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aplab.apsite.config.SecurityUser;
import com.aplab.apsite.model.dto.comm.CommImageDTO;
import com.aplab.apsite.model.dto.comm.CommUploadDTO;
import com.aplab.apsite.model.enums.CommonResultCode;
import com.aplab.apsite.model.vo.ResponseVO;
import com.aplab.apsite.service.AWSService;
import com.aplab.apsite.service.CommonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final CommonService commonService;
    
    private final AWSService awsService;
    
    /*
    @PostMapping(value="/files", consumes = {"multipart/form-data"})
    public Object uploadFiles(
        @RequestPart(value="files", required=false) List<MultipartFile> files,
        @AuthenticationPrincipal SecurityUser securityUser
    ) throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("UploadController.uploadFiles : files size : {}", files != null ? files.size() : 0);
        }

        List<CommUploadTempDTO> list = commonService.saveCommUploadTemp(files, securityUser.getSessionVO());

        ResponseVO respVo = new ResponseVO();
        respVo.setOk(list);

        return ResponseEntity.ok(respVo);
    }
    */

    @PostMapping(value="/image-files")
    public Object uploadImageFiles(@RequestPart(value="files", required=false) List<MultipartFile> files,@RequestParam (value="uploadCd") String uploadCd,@AuthenticationPrincipal SecurityUser securityUser) throws IOException {
    	ResponseVO responseVO = new ResponseVO();
    	List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    	for(MultipartFile rvo : files) {
    		ByteArrayOutputStream out = new ByteArrayOutputStream();
    		try {
	    		HashMap<String, Object> map = new HashMap<String, Object>();
				int bytesRead;				
				InputStream in = rvo.getInputStream();
				byte[] buffer = new byte[in.available()];				
				//byte[] buffer = new byte[1024];
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				map.put("fileExt", rvo.getOriginalFilename().substring(rvo.getOriginalFilename().lastIndexOf(".") + 1));
				map.put("fileBytea", out.toByteArray());
				list.add(map);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				out.flush();
				out.close();				
			}
    	}   	
    	responseVO.setOk(list);
    	return ResponseEntity.ok(responseVO);
    }
    
    @PostMapping(value="/files", consumes = {"multipart/form-data"})
    public Object uploadFiles(
    		@RequestPart(value="files", required=false) List<MultipartFile> files,
    		@RequestParam (value="uploadCd") String uploadCd,
    		@RequestParam (value="fileType") String fileType,
    		@AuthenticationPrincipal SecurityUser securityUser
    		) throws IOException {
    	ResponseVO responseVO = new ResponseVO();
    	
    	String availableFileExt = ".png|.gif|.jpg|.jpeg|.xlsx|.xls|.docx|.doc|.pptx|.ppt|.pdf";
    	String fileName = "";
    	int uploadFailCnt = 0;
    	for (MultipartFile file : files) {
    	    fileName = file.getOriginalFilename();
    	    
    	    if (availableFileExt.indexOf(fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase()) == -1) {
    	        uploadFailCnt++;
    	    }
    	}
    	
    	if (uploadFailCnt > 0) {
    	    responseVO.setOk(CommonResultCode.FAIL_EXT, null);
    	} else {
    	    List<CommUploadDTO> result = awsService.upload(files, uploadCd, fileType);
            responseVO.setOk(result);
    	}
    	
    	return ResponseEntity.ok(responseVO);
    }
    
    /*
    @PostMapping(value="/images", consumes = {"multipart/form-data"})
    public Object uploadImages(
        @RequestPart(value="files", required=false) List<MultipartFile> files,
        @AuthenticationPrincipal SecurityUser securityUser
    ) throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("UploadController.uploadImages : files size : {}", files != null ? files.size() : 0);
        }

        List<CommUploadTempDTO> list = commonService.saveCommUploadTemp(files, securityUser.getSessionVO());

        ResponseVO respVo = new ResponseVO();
        respVo.setOk(list);

        return ResponseEntity.ok(respVo);
    }
    */
    
    @PostMapping("/files/delete")
    public Object uploadFiles(
            @RequestBody @Valid CommUploadDTO params,
    		@AuthenticationPrincipal SecurityUser securityUser
    		) {
    	ResponseVO responseVO = new ResponseVO();
    	
    	String result = commonService.deleteFile(params);
    	
    	if ("SUCC".equals(result)) {
    	    awsService.delete(params.getKeyPath());
    	}
    	return ResponseEntity.ok(responseVO);
    }

    @GetMapping(value="/images/{seq}")
    @ResponseBody
    public Object findImage(
        @PathVariable(name = "seq") long seq
    ) throws IOException {
        CommImageDTO dto = commonService.findImage(seq);
        
        return ResponseEntity.ok()
            .contentLength(dto.getFileSize())
            .contentType(MediaType.parseMediaType(dto.getFileType()))
            .body(dto.getFileBytea());
    }
}
