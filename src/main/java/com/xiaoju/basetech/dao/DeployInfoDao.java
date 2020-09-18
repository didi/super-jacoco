package com.xiaoju.basetech.dao;

import com.xiaoju.basetech.entity.DeployInfoEntity;
import org.apache.ibatis.annotations.Param;

public interface DeployInfoDao {

    /**
     * 只是环境用
     * @param uuid
     * @param address
     * @param port
     * @return
     */
    int insertDeployId(@Param("uuid") String uuid, @Param("address") String address, @Param("port") int port);

    /**
     * 只是环境用
     * @param uuid
     * @return
     */
    DeployInfoEntity queryDeployId(@Param("uuid") String uuid);

    /**
     *
     * @param deployInfoEntity
     * @return
     */
    int updateDeployInfo(@Param("deployInfoEntity") DeployInfoEntity deployInfoEntity);


}
