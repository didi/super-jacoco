package com.xiaoju.basetech.dao;

import com.xiaoju.basetech.entity.CoverageReportEntity;
import com.xiaoju.basetech.entity.DeployInfoEntity;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author didi
 */
public interface CoverageReportDao {

    int insertCoverageReportById(@Param("coverageReportEntity") CoverageReportEntity coverageReportEntity);

    CoverageReportEntity queryCoverageReportByUuid(@Param("uuid") String uuid);

    /**
     * 根据状态查询
     *
     * @param status
     * @return
     */
    List<CoverageReportEntity> queryCoverByStatus(@Param("triggerStatus") Integer status, @Param("from") Integer from,
                                                  @Param("num") Integer num);

    int updateCoverageReportByDiffMethods(@Param("coverageReportEntity") CoverageReportEntity coverageReportEntity);

    int updateCoverageReportByReport(@Param("coverageReportEntity") CoverageReportEntity coverageReportEntity);


    int casUpdateByStatus(@Param("expectStatus") int expectStatus, @Param("newStatus") int newStatus, @Param("uuid") String uuid);

    /**
     * 重置上次更新时间在expireTime之前的未执行完的任务，设置任务状态为待执行
     *
     * @param expireTime
     * @return
     */
    int casUpdateStatusByExpireTime(@Param("expireTime") String expireTime);

    /**
     * 根据ID更新状态
     *
     * @param id
     * @param requestStatus  新状态
     * @param expectedStatus 老状态
     * @return int
     */
    int casUpdateStatusById(Integer id, Integer requestStatus, Integer expectedStatus);
}
