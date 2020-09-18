package com.xiaoju.basetech.util;

import com.xiaoju.basetech.entity.CoverageReportEntity;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @description:
 * @author: charlynegaoweiwei
 * @time: 2020/4/27 4:16 PM
 */
@Component
@Slf4j
public class ReportParser {
    public boolean parseReport(CoverageReportEntity coverageReport) {
        File reportFile = new File(coverageReport.getReportFile());
        try {
            if (reportFile.exists()) {
                //解析并获取覆盖率
                Document doc = Jsoup.parse(reportFile.getAbsoluteFile(), "UTF-8", "");
                Elements bars = doc.getElementById("coveragetable").getElementsByTag("tfoot").first().getElementsByClass("bar");
                Elements lineCtr1 = doc.getElementById("coveragetable").getElementsByTag("tfoot").first().getElementsByClass("ctr1");
                Elements lineCtr2 = doc.getElementById("coveragetable").getElementsByTag("tfoot").first().getElementsByClass("ctr2");
                double lineCoverage = 100;
                double branchCoverage = 100;
                if (bars != null) {
                    float lineNumerator = Float.valueOf(lineCtr1.get(1).text().replace(",", ""));
                    float lineDenominator = Float.valueOf(lineCtr2.get(3).text().replace(",", ""));
                    lineCoverage = (lineDenominator - lineNumerator) / lineDenominator * 100;
                    String[] branch = bars.get(1).text().split(" of ");
                    float branchNumerator = Float.valueOf(branch[0].replace(",", ""));
                    float branchDenominator = Float.valueOf(branch[1].replace(",", ""));
                    if (branchDenominator <= 0.0) {
                        branchCoverage = 100;
                    } else {
                        branchCoverage = (branchDenominator - branchNumerator) / branchDenominator * 100;
                    }
                }
                coverageReport.setBranchCoverage(branchCoverage);
                coverageReport.setLineCoverage(lineCoverage);
                coverageReport.setRequestStatus(Constants.JobStatus.PARSEREPORT_DONE.val());
                return true;
            } else {
                coverageReport.setRequestStatus(Constants.JobStatus.FAILPARSEREPOAT.val());
                log.error("uuid={} 报告不存在{}",coverageReport.getUuid(),coverageReport.getReportFile());
                coverageReport.setErrMsg("项目没有单元测试case");
                return false;
            }
        } catch (Exception e) {
            log.error("解析报告发生异常:{}", coverageReport.getUuid(), e);
            coverageReport.setErrMsg("解析报告发生异常:" + e.getMessage());
            coverageReport.setRequestStatus(Constants.JobStatus.FAILPARSEREPOAT.val());
            return false;
        }
    }

}