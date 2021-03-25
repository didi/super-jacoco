package com.xiaoju.basetech.job;

import com.xiaoju.basetech.dao.CoverageReportDao;
import com.xiaoju.basetech.entity.CoverageReportEntity;
import com.xiaoju.basetech.service.CodeCovService;
import com.xiaoju.basetech.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: charlynegaoweiwei
 * @time: 2020/4/26 7:45 PM
 */
@Slf4j
@Component
public class CodeCoverageScheduleJob {

    @Autowired
    private CoverageReportDao coverageReportDao;

    @Autowired
    private CodeCovService codeCovService;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

    private static AtomicInteger counter = new AtomicInteger(0);


    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 5 * 60, TimeUnit.SECONDS,
            new SynchronousQueue<>(), r -> new Thread(r, "Code-Coverage-Thread-pool" + counter.getAndIncrement()));


    /**
     * clone代码定时任务
     * <p>
     * 查询状态是Constants.JobStatus.INITIAL的任务
     */
    @Scheduled(fixedDelay = 10_000L, initialDelay = 10_000L)
    public void codeCloneJob() {
        // 1. 查询需要diff的数据
        List<CoverageReportEntity> resList = coverageReportDao.queryCoverByStatus(Constants.JobStatus.INITIAL.val(),
                Constants.CoverageFrom.UNIT.val(), 1);
        log.info("查询需要diff的数据{}条", resList.size());
        resList.forEach(o -> {
            try {
                int num = coverageReportDao.casUpdateByStatus(Constants.JobStatus.INITIAL.val(),
                        Constants.JobStatus.WAITING.val(), o.getUuid());
                if (num > 0) {
                    executor.execute(() -> codeCovService.calculateUnitCover(o));
                } else {
                    log.info("others execute task :{}", o.getUuid());
                }
            } catch (Exception e) {
                coverageReportDao.casUpdateByStatus(Constants.JobStatus.WAITING.val(),
                        Constants.JobStatus.INITIAL.val(), o.getUuid());
            }
        });
    }


    /**
     * 未执行完的任务， 超过120分钟时间任务状态未更新，将任务状态设置未初始化,status=0
     */
    @Scheduled(fixedDelay = 600_000L, initialDelay = 10_000L)
    public void resetJobStatus() {
        try {
            log.info("重置任务状态开始执行............");
            long currentTime = System.currentTimeMillis();
            Date date = new Date(currentTime - 120 * 60 * 1000);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String expireTime = df.format(date);
            coverageReportDao.casUpdateStatusByExpireTime(expireTime);
        } catch (Exception e) {
            log.error("重置任务执行失败");
        }
    }

    /**
     * 每五分钟从项目机器上拉取exec执行文件，计算环境的增量方法覆盖率
     */
    @Scheduled(fixedDelay = 300_000L, initialDelay = 300_000L)
    public void calculateEnvCov() {
        List<CoverageReportEntity> resList = coverageReportDao.queryCoverByStatus(Constants.JobStatus.SUCCESS.val(),
                Constants.CoverageFrom.ENV.val(), 10);
        log.info("查询需要拉取exec文件的数据{}条", resList.size());
        resList.forEach(o -> {
            try {
                int num = coverageReportDao.casUpdateByStatus(Constants.JobStatus.SUCCESS.val(),
                        Constants.JobStatus.WAITING.val(), o.getUuid());
                if (num > 0) {
                    // 代码目录不存在说明代码不在这一台机器上，这里会重新下载代码编译，此时若代码有更新，会出现统计代码和本地class不一致
                    // 建议使用commitID替换branch来避免这个问题
                    if (!new File(o.getNowLocalPath()).exists()) {
                        codeCovService.cloneAndCompileCode(o);
                        if (o.getRequestStatus() != Constants.JobStatus.COMPILE_DONE.val()) {
                            log.info("{}计算覆盖率具体步骤...编译失败uuid={}", Thread.currentThread().getName(), o.getUuid());
                            return;
                        }
                    }
                    log.info("others execute exec task uuid={}", o.getUuid());
                    if (o.getType() == Constants.ReportType.DIFF.val() && StringUtils.isEmpty(o.getDiffMethod())) {
                        codeCovService.calculateDeployDiffMethods(o);
                        if (o.getRequestStatus() != Constants.JobStatus.DIFF_METHOD_DONE.val()) {
                            log.info("{}计算覆盖率具体步骤...计算增量代码失败，uuid={}", Thread.currentThread().getName(), o.getUuid());
                            return;
                        }
                    }
                    codeCovService.calculateEnvCov(o);
                    log.info("任务执行结束，uuid={}", o.getUuid());
                } else {
                    log.info("任务已被领取，uuid={}", o.getUuid());
                    return;
                }

            } catch (Exception e) {
                log.error("uuid={}拉取exec文件异常", o.getUuid(), e);
            }
        });
    }

}