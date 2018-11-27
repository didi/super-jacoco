package com.xiaoju.hallowmas.service;

import com.xiaoju.hallowmas.entity.TestCaseObject;
import com.xiaoju.hallowmas.util.JsonUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by yehonggang on 18/11/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCaseServiceTest extends TestCase {
    @Autowired
    TestCaseService testCaseService;

    @Test
    public void createTestCase() throws Exception {

        TestCaseObject testCaseObject = new TestCaseObject();
        testCaseObject.setCaseName("test case name");
        testCaseObject.setExplain("test case explain");
        testCaseObject.setOwnner("test case owner");
        testCaseObject.setStatus(0);
        testCaseService.createTestCase(testCaseObject);
        System.out.println(JsonUtil.toJson(testCaseObject));
    }

    @Test
    public void modifyTestCase() throws Exception {

    }

    @Test
    public void getTestCaseById() throws Exception {

    }

    @Test
    public void deleteTestCase() throws Exception {

    }

    @Test
    public void listTestCase() throws Exception {

    }

}