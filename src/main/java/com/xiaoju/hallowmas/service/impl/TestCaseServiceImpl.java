package com.xiaoju.hallowmas.service.impl;

import com.google.common.base.Strings;
import com.xiaoju.hallowmas.common.Preconditions;
import com.xiaoju.hallowmas.common.ResponseException;
import com.xiaoju.hallowmas.entity.TestCaseObject;
import com.xiaoju.hallowmas.enumType.ErrorCode;
import com.xiaoju.hallowmas.mapper.TestCaseMapper;
import com.xiaoju.hallowmas.service.TestCaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yehonggang on 18/11/11.
 */
@Service
public class TestCaseServiceImpl implements TestCaseService {
    private static final Logger LOG = LoggerFactory.getLogger(TestCaseServiceImpl.class);

    @Autowired
    private TestCaseMapper testCaseMapper;

    public void createTestCase(TestCaseObject testCaseObject) {
        verifyTestCase(testCaseObject);
        try {
            testCaseMapper.insert(testCaseObject);
            LOG.info("Save testCaseObject successfully with testCaseId:" + testCaseObject.getId());
        } catch (Exception e) {
            LOG.error("Failed to save testCaseObject ", e);
            throw new ResponseException(ErrorCode.SERVER_ERROR, "Failed to save testCaseObject : " + e.getMessage());
        }
    }


    public void modifyTestCase(TestCaseObject testCaseObject) {
        verifyTestCase(testCaseObject);
        try {
            testCaseMapper.updateByPrimaryKey(testCaseObject);
            LOG.info("update testCaseObject successfully with testCaseId:" + testCaseObject.getId());
        } catch (Exception e) {
            LOG.error("Failed to update testCaseObject ", e);
            throw new ResponseException(ErrorCode.SERVER_ERROR, "Failed to update testCaseObject : " + e.getMessage());
        }
    }

    public TestCaseObject getTestCaseById(int id) {
        TestCaseObject testCaseObject = null;
        try {
            testCaseObject = testCaseMapper.selectByPrimaryKey(id);
            LOG.info("get testCaseObject successfully with testCaseId:" + id);
        } catch (Exception e) {
            LOG.error("Failed to get testCaseObject ", e);
            throw new ResponseException(ErrorCode.SERVER_ERROR, "Failed to get testCaseObject : " + e.getMessage());
        }
        return testCaseObject;
    }

    public List<TestCaseObject> listTestCase() {
        List<TestCaseObject> testCaseObjects;
        try {
            testCaseObjects = testCaseMapper.listTestCase(new TestCaseObject());
            LOG.info("list testCase successfully");
        } catch (Exception e) {
            LOG.error("Failed to list testCase ", e);
            throw new ResponseException(ErrorCode.SERVER_ERROR, "Failed to list testCase : " + e.getMessage());
        }
        return testCaseObjects;
    }


    public void deleteTestCase(int id) {
        try {
            testCaseMapper.deleteByPrimaryKey(id);
            LOG.info("delete testCase successfully, testCase id is " + id);
        } catch (Exception e) {
            LOG.error("Failed to delete testCase ", e);
            throw new ResponseException(ErrorCode.SERVER_ERROR, "Failed to delete testCase : " + e.getMessage());
        }
    }

    private void verifyTestCase(TestCaseObject testCaseObject) {
        Preconditions.checkArgument(testCaseObject != null, "testCaseObject object is null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(testCaseObject.getCaseName()), ErrorCode.NAME_IS_NOT_EXIST);
    }


}
