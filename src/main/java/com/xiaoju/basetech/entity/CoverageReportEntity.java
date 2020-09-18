package com.xiaoju.basetech.entity;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: gaoweiwei_v
 * @time: 2019/7/29 2:29 PM
 */

@Data
public class CoverageReportEntity {

    private Integer id;
    private String uuid;
    private String gitUrl;
    private String baseVersion;
    private String nowVersion;
    /**
     * 1=全量覆盖率 2=增量覆盖率
     */
    private Integer type;
    private Integer requestStatus;
    private String diffMethod = "";
    private String errMsg = "";
    private String reportUrl = "";
    private Double lineCoverage = (double) -1;
    private Double branchCoverage = (double) -1;
    private Date createTime;
    private Date updateTime;
    private String nowLocalPath = "";
    private String baseLocalPath = "";
    private String subModule = "";
    private String codePath;
    private String envType = "";
    private String reportFile;
    private Integer from;
    private String logFile = "";
    
}