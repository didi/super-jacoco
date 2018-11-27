package com.xiaoju.hallowmas.entity;


public class TestCaseObject {
    /**
     *
     */
    private Integer id;
    /**
     * 用例名称
     */
    private String caseName;
    /**
     * 状态 0:有效 1:无效
     */
    private Integer status;
    /**
     *
     */
    private String gmtModified;
    /**
     *
     */
    private String gmtCreate;
    /**
     * 0:正常，1：删除
     */
    private boolean deleted;
    /**
     *
     */
    private String ownner;
    /**
     *
     */
    private String explain;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName == null ? null : caseName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getOwnner() {
        return ownner;
    }

    public void setOwnner(String ownner) {
        this.ownner = ownner == null ? null : ownner.trim();
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain == null ? null : explain.trim();
    }
}