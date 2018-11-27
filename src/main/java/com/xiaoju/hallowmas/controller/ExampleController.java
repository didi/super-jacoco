package com.xiaoju.hallowmas.controller;

import com.xiaoju.hallowmas.entity.TestCaseObject;
import com.xiaoju.hallowmas.service.TestCaseService;
import com.xiaoju.huhang.common.response.Response;
import com.xiaoju.huhang.common.response.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yehonggang on 18/11/11.
 */
@RestController
@RequestMapping(value = "testcase")
public class ExampleController {
    @Autowired
    TestCaseService testCaseService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Response<TestCaseObject> createTestCase(@RequestBody TestCaseObject testCaseObject) {
        testCaseService.createTestCase(testCaseObject);
        return ResponseUtils.build(testCaseObject);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Response<TestCaseObject> updateTestCase(@RequestBody TestCaseObject testCaseObject) {
        testCaseService.modifyTestCase(testCaseObject);
        return ResponseUtils.build();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Response<?> deleteTestCaseById(@RequestParam(value = "id") Integer id) {
        testCaseService.deleteTestCase(id);
        return ResponseUtils.build();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Response<TestCaseObject> getTestCaseById(@RequestParam(value = "id") Integer id) {
        TestCaseObject testCaseObject = testCaseService.getTestCaseById(id);
        return ResponseUtils.build(testCaseObject);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Response<Object> listTestCase() {
        List<TestCaseObject> testCaseObjects = testCaseService.listTestCase();
        return ResponseUtils.successResult(testCaseObjects);
    }
}
