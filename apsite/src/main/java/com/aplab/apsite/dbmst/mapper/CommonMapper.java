package com.aplab.apsite.dbmst.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aplab.apsite.config.MstConnMapper;
import com.aplab.apsite.model.dto.comm.CodeSubDTO;
import com.aplab.apsite.model.dto.comm.CommImageDTO;
import com.aplab.apsite.model.dto.comm.CommMenuDTO;
import com.aplab.apsite.model.dto.comm.CommUploadDTO;
import com.aplab.apsite.model.dto.comm.ConInfoDTO;
import com.aplab.apsite.model.dto.comm.LaborInfoDTO;
import com.aplab.apsite.model.dto.ingrd.ReqRawSearchDTO;

@Mapper
@MstConnMapper
public interface CommonMapper {
    
    public void insertCommUpload(CommUploadDTO params);

    public void insertCommUploadCont(CommUploadDTO params);

    public void insertCommImage(CommImageDTO params);

    public void insertCommImageCont(CommImageDTO params);

    public void deleteCommUploadTemp(long seq);

	public List<CodeSubDTO> findTiumCodeList(List<String> arrMstCd, String langCd);

    public List<CommMenuDTO> findMenuLevelList(@Param("arrGroupId") List<String> arrGroupId);
    
    public List<CommMenuDTO> findPageLevelList(@Param("arrGroupId") List<String> arrGroupId);

    public int findUploadVer(@Param("targetKey") String targetKey, @Param("uploadCd") String uploadCd);

    public int findConListCount(String keyword);

    public List<ConInfoDTO> findConList(ReqRawSearchDTO params);

    public void copyFileList(@Param("seq") Long seq, @Param("ver") int ver, @Param("preVer") String preVer);

    public List<LaborInfoDTO> findLaborList(@Param("keyword") String keyword, @Param("langCd") String langCd);

    public List<CommUploadDTO> findCommUploadHisList(@Param("targetKey") String targetKey, @Param("uploadCd") String uploadCd, @Param("preRecordCd")long preRecordCd, @Param("reviewFlag") String reviewFlag);

    public int findPageAuthCount(@Param("arrGroupId") List<String> arrGroupId, @Param("apiUrl") String apiUrl);
}
