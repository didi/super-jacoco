package com.xiaoju.basetech.util;

import com.xiaoju.basetech.entity.CoverageReportEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.xiaoju.basetech.util.Constants.REPORT_PATH;

/**
 * @description:
 * @author: charlynegaoweiwei
 * @time: 2020/4/28 5:38 下午
 */
@Slf4j
@Component
public class ReportCopyExecutor {

    public void copyReport(CoverageReportEntity coverageReport) {

        //复制report报告
        String[] cpCmd = new String[]{"cp -rf " + new File(coverageReport.getReportFile()).getParent() + "/ " + REPORT_PATH + coverageReport.getUuid()};
        int cpExitCode;
        try {
            cpExitCode = CmdExecutor.executeCmd(cpCmd, 600000L);
            if (cpExitCode == 0) {
                coverageReport.setReportUrl(LocalIpUtils.getTomcatBaseUrl() + coverageReport.getUuid() + "/index.html");
                coverageReport.setRequestStatus(Constants.JobStatus.COPYREPORT_DONE.val());
                return;
            } else {
                coverageReport.setRequestStatus(Constants.JobStatus.COPYREPORT_FAIL.val());
                if (cpExitCode == 143) {
                    coverageReport.setErrMsg("复制报告超时");
                }else {
                    coverageReport.setErrMsg("复制报告异常");
                }
            }
        } catch (Exception e) {
            log.error("uuid={}复制报告异常",coverageReport.getUuid(), e);
            coverageReport.setRequestStatus(Constants.JobStatus.COPYREPORT_FAIL.val());
            return;
        }
    }
}