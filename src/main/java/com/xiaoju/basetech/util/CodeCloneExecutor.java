package com.xiaoju.basetech.util;

import com.xiaoju.basetech.entity.CoverageReportEntity;
import jodd.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.xiaoju.basetech.util.Constants.CODE_ROOT;


/**
 * @description:
 * @author: charlynegaoweiwei
 * @time: 2020/4/28 4:52 下午
 */
@Slf4j
@Component
public class CodeCloneExecutor {

    @Autowired
    private GitHandler gitHandler;

    public void cloneCode(CoverageReportEntity coverageReport) {
        if (coverageReport.getType() == Constants.ReportType.DIFF.val() && coverageReport.getNowVersion().equals(coverageReport.getBaseVersion())) {
            coverageReport.setErrMsg("两个commitId一样，无增量代码");
            coverageReport.setRequestStatus(Constants.JobStatus.NODIFF.val());
            coverageReport.setReportUrl(Constants.NO_DIFFCODE_REPORT);
            coverageReport.setBranchCoverage((double) 100);
            coverageReport.setLineCoverage((double) 100);
            return;
        }
        String logFile = LocalIpUtils.getTomcatBaseUrl()+"logs/" + coverageReport.getUuid() + ".log";
        coverageReport.setLogFile(logFile);
        try {
            String uuid = coverageReport.getUuid();
            String nowLocalPath = CODE_ROOT + uuid + "/" + coverageReport.getNowVersion().replace("/", "_");
            coverageReport.setNowLocalPath(nowLocalPath);
            if (new File(CODE_ROOT + uuid + "/").exists()) {
                FileUtil.cleanDir(CODE_ROOT + uuid + "/");
            }
            String gitUrl = coverageReport.getGitUrl();
            log.info("uuid {}开始下载代码...", uuid);
            gitHandler.cloneRepository(gitUrl, nowLocalPath, coverageReport.getNowVersion());
            if (coverageReport.getType() == Constants.ReportType.DIFF.val()) {
                String baseLocalPath = CODE_ROOT + uuid + "/" + coverageReport.getBaseVersion().replace("/", "_");
                coverageReport.setBaseLocalPath(baseLocalPath);
                gitHandler.cloneRepository(gitUrl, baseLocalPath, coverageReport.getBaseVersion());
            }
            log.info("uuid {}完成下载代码...", uuid);
            coverageReport.setRequestStatus(Constants.JobStatus.CLONE_DONE.val());
        } catch (Exception e) {
            log.error("下载代码发生异常:{}", coverageReport.getUuid(), e);
            coverageReport.setErrMsg("下载代码发生异常:" + e.getMessage());
            coverageReport.setRequestStatus(Constants.JobStatus.CLONE_FAIL.val());
        }
    }

}