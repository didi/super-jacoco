package com.xiaoju.basetech.entity;

import lombok.Data;

/**
 * @description:
 * @author: gaoweiwei_v
 * @time: 2019/9/23 10:16 AM
 */
@Data
public class CoverResult {

    /**
     * -1、失败;1、成功；0、进行中
     */
    private int coverStatus;
    public String reportUrl;
    private double lineCoverage;
    private double branchCoverage;
    private String errMsg;
    private String logFile;

}