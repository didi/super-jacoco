package com.xiaoju.basetech.entity;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: charlynegaoweiwei
 * @time: 2020/4/26 7:52 PM
 */
@Data
public class UnitCoverRequest extends CoverBaseRequest{

    /**
     * profile，只有单元测试需要，在命令行会加-Pstable、-Ptest等
     */
    private String envType;

}