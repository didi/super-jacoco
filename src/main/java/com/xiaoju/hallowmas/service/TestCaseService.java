package com.xiaoju.hallowmas.service;

import com.xiaoju.hallowmas.entity.TestCaseObject;

import java.util.List;

/**
 * Created by yehonggang on 18/11/11.
 */
public interface TestCaseService {

    /**
     * 创建testCase
     *
     * @param testCaseObject
     */
    void createTestCase(TestCaseObject testCaseObject);


    /**
     * 修改testCase
     *
     * @param testCaseObject
     */
    void modifyTestCase(TestCaseObject testCaseObject);

    /**
     * 根据ID获取testCase信息
     *
     * @param id
     * @return
     */
    TestCaseObject getTestCaseById(int id);

    /**
     * 根据id删除testCase
     *
     * @param id
     */
    void deleteTestCase(int id);

    /**
     * 获取testCase列表
     *
     * @return
     */
    List<TestCaseObject> listTestCase();

}
