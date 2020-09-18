package com.xiaoju.basetech.entity;

/**
 * @description:
 * @author: gaoweiwei_v
 * @time: 2019/11/27 1:57 PM
 */
public class DeployInfoEntity {
    private String uuid;
    private String address;
    private int port;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public String getChildModules() {
        return childModules;
    }

    public void setChildModules(String childModules) {
        this.childModules = childModules;
    }

    private String codePath;
    private String childModules;

    public DeployInfoEntity() {
    }
}