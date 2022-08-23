package com.aplab.apsite.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ParentPagingDTO {

    private int pageSize = 10;

    private int totalCnt;

    private int nowPageNo;

    private int totalPageCnt;

    private int limit;

    private int offset;
    
    private int startRownum;
    
    private int endRownum;

    private int startPage = 1;

    private int endPage = 1;

    private int curBlock = 1;
}
