package com.xiaoju.basetech.service.impl;

import com.xiaoju.basetech.entity.CoverageReportEntity;
import com.xiaoju.basetech.util.JDiffFiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service
public class DiffMethodsCalculator {

    /**
     * 下载代码并计算diff方法
     *
     * @param coverageReport
     * @return
     */
    public void executeDiffMethods(CoverageReportEntity coverageReport) {
        StringBuffer diffFile = new StringBuffer();
        long s = System.currentTimeMillis();
        HashMap map = JDiffFiles.diffMethodsListNew(coverageReport);
        if (!CollectionUtils.isEmpty(map)) {
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                diffFile.append(entry.getKey() + ":" + entry.getValue() + "%");
            }
            coverageReport.setDiffMethod(diffFile.toString());
        }

        log.info("uuid={} 增量计算耗时：{}", coverageReport.getUuid(), (System.currentTimeMillis() - s));
    }

    public String executeDiffMethodsForEnv(String baseVersionPath, String nowVersionPath, String baseVersion, String nowVersion) {
        if (baseVersionPath.equals(nowVersionPath)) {
            return null;
        }
        StringBuffer diffFile = new StringBuffer();
        long ms = System.currentTimeMillis();
        HashMap map = JDiffFiles.diffMethodsListForEnv(baseVersionPath, nowVersionPath, baseVersion, nowVersion);
        if (map != null && !map.isEmpty()) {
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                diffFile.append(entry.getKey() + ":" + entry.getValue() + "%");
            }
            return diffFile.toString();
        }

        log.info("增量计算耗时：{}", (System.currentTimeMillis() - ms));
        return null;
    }
}
