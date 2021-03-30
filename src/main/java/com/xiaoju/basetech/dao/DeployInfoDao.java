package com.xiaoju.basetech.dao;

import com.xiaoju.basetech.entity.DeployInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeployInfoDao {

    /**
     * 只是环境用
     *
     * @param uuid
     * @param address
     * @param port
     * @return
     */
    int insertDeployId(@Param("uuid") String uuid, @Param("address") String address, @Param("port") int port, @Param("masterUuid") String masterUuid);

    /**
     * 只是环境用
     *
     * @param uuid
     * @return
     */
    DeployInfoEntity queryDeployId(@Param("uuid") String uuid);

    /**
     * 通过masterUuid查询所有部署任务
     * @param masterUuid
     * @return
     */
    List<DeployInfoEntity> queryDeployIdByMaster(@Param("masterUuid") String masterUuid);

    /**
     * @param deployInfoEntity
     * @return
     */
    int updateDeployInfo(@Param("deployInfoEntity") DeployInfoEntity deployInfoEntity);


}
