package com.xiaoju.basetech.entity;

import org.dom4j.Element;

public class ModuleInfo {
    private boolean flag;//moduleInfo是否是有效的，groupId，artifactId，version，都存在flag才是1
    private String parentVersion;
    private String parentGroupId;
    private String parentArtifactId;
    private ModuleInfo parent;
    private String artifactId;
    private String groupId;
    private String version;
    private Element properties;//父pom的properties可能存储了version信息
    private String packaging;//如果是pom类型需要递归查找，只要找jar类型的

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getParentVersion() {
        return parentVersion;
    }

    public void setParentVersion(String parentVersion) {
        this.parentVersion = parentVersion;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public String getParentArtifactId() {
        return parentArtifactId;
    }

    public void setParentArtifactId(String parentArtifactId) {
        this.parentArtifactId = parentArtifactId;
    }

    public ModuleInfo getParent() {
        return parent;
    }

    public void setParent(ModuleInfo parent) {
        this.parent = parent;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Element getProperties() {
        return properties;
    }

    public void setProperties(Element properties) {
        this.properties = properties;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }
}
