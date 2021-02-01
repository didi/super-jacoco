package com.xiaoju.basetech.util;

import com.xiaoju.basetech.entity.CoverageReportEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeoutException;

import static com.xiaoju.basetech.util.Constants.LOG_PATH;


/**
 * @description:
 * @author: charlynegaoweiwei
 * @time: 2020/4/28 4:59 下午
 */
@Component
public class CodeCompilerExecutor {

    public void compileCode(CoverageReportEntity coverageReport) {
        String logFile = coverageReport.getLogFile().replace(LocalIpUtils.getTomcatBaseUrl()+"logs/", LOG_PATH);
        String[] compileCmd = new String[]{"cd " + coverageReport.getNowLocalPath() + "&&mvn clean compile " +
                (StringUtils.isEmpty(coverageReport.getEnvType()) ? "" : "-P=" + coverageReport.getEnvType()) + ">>" + logFile};
        try {
            int exitCode = CmdExecutor.executeCmd(compileCmd, 600000L);
            if (exitCode != 0) {
                coverageReport.setRequestStatus(Constants.JobStatus.COMPILE_FAIL.val());
                coverageReport.setErrMsg("编译代码出错");
            } else {
                coverageReport.setRequestStatus(Constants.JobStatus.COMPILE_DONE.val());
            }
        } catch (TimeoutException e) {
            coverageReport.setRequestStatus(Constants.JobStatus.COMPILE_FAIL.val());
            coverageReport.setErrMsg("编译代码超过了10分钟");
        } catch (Exception e) {
            coverageReport.setErrMsg("编译代码发生未知错误");
            coverageReport.setRequestStatus(Constants.JobStatus.COMPILE_FAIL.val());
        }
    }

}