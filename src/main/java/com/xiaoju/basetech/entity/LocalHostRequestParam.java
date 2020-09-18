package com.xiaoju.basetech.entity;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LocalHostRequestParam extends CoverBaseRequest {
    /**
     * dump jacoco.exec文件目标地址
     */
    @NotEmpty(message = "Address不能为空")
    private String Address;
    /**
     * dump jacoco.exec文件端口
     */
    @NotNull(message = "port不能为空")
    private Integer port;
    /**
     * 子项目目录名称
     */
    private String subModule;
    /**
     * class文件路径
     */
    @NotEmpty(message = "classFilePath不能为空")
    private String classFilePath;
    /**
     * base代码存储路径
     */
    @NotEmpty(message = "基础代码路径不能为空")
    private String basePath;
    /**
     * 目标代码存储路径
     */
    @NotEmpty(message = "目标代码路径不能为空")
    private String nowPath;

}
