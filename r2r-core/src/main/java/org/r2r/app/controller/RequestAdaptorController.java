package org.r2r.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.r2r.app.annotation.apiversion.ApiVersion;
import org.r2r.app.model.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RequestAdaptorController {

    //@IgnoreApiLog
    //@IgnoreResponseBoxing
    @ApiVersion("v1")
    @PostMapping("/adaptor/request")
    public String sendRequest(@RequestBody ApiParam apiParam) {
        return "123test";
    }

}
