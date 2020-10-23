package com.xiaoju.basetech.controller;


import com.xiaoju.basetech.entity.*;
import com.xiaoju.basetech.service.CodeCovService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author guojinqiong
 */
@RestController
@RequestMapping(value = "/cov")
public class CodeCovController {

    @Autowired
    private CodeCovService codeCovService;

    /**
     * 触发单元测试diff覆盖率
     *
     * @param unitCoverRequest
     * @return
     */
    @PostMapping(value = "/triggerUnitCover")
    public HttpResult<Boolean> triggerUnitCover(@RequestBody @Validated UnitCoverRequest unitCoverRequest) {
        codeCovService.triggerUnitCov(unitCoverRequest);
        return HttpResult.success();
    }


    /**
     * 返回单元测试覆盖率报告或者任务执行状态
     *
     * @param uuid，触发时携带的UUID
     * @return coverStatus：-1、失败;1、成功；0、进行中
     */
    @GetMapping(value = "/getUnitCoverResult")
    @ResponseBody
    public HttpResult<CoverResult> getCoverResult(@RequestParam(value = "uuid") String uuid) {
        return HttpResult.success(codeCovService.getCoverResult(uuid));
    }

    /**
     *
     * @param envCoverRequest
     * @return
     */
    @RequestMapping(value = "/triggerEnvCov", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public HttpResult<Boolean> triggerEnvCov(@RequestBody @Validated EnvCoverRequest envCoverRequest) {
        codeCovService.triggerEnvCov(envCoverRequest);
        return HttpResult.success();

    }

    /**
     * 获取功能测试增量代码覆盖率
     *
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/getEnvCoverResult", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public HttpResult<CoverResult> getEnvCoverResult(@RequestParam(value = "uuid") String uuid) {
        return HttpResult.success(codeCovService.getCoverResult(uuid));

    }

    /**
     * 手动获取env增量代码覆盖率，代码部署和覆盖率服务在同一机器上，可直接读取本机源码和本机class文件
     *
     * @return
     */
    @RequestMapping(value = "/getLocalCoverResult", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public HttpResult<CoverResult> getEnvLocalCoverResult(@RequestBody @Valid LocalHostRequestParam localHostRequestParam) {

        return HttpResult.success(codeCovService.getLocalCoverResult(localHostRequestParam));

    }
}
