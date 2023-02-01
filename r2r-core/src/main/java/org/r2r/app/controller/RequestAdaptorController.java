package org.r2r.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.r2r.app.annotation.apiversion.ApiVersion;
import org.r2r.app.model.APIParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO controller入参出参打印日志, 自动封装出参
 * TODO 全局变量里存放logId,使用日志里的traceId
 */
@Slf4j
@RestController
public class RequestAdaptorController {

    //@IgnoreApiLog
    @ApiVersion("v1")
    @PostMapping("/adaptor/request")
    public Object sendRequest(@RequestBody APIParam apiParam) {
        return "test";
    }

}
