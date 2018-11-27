package com.xiaoju.hallowmas.mapper;

import com.xiaoju.hallowmas.entity.TestCaseObject;

import java.util.List;

public interface TestCaseMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TestCaseObject record);

    int insertSelective(TestCaseObject record);

    TestCaseObject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TestCaseObject record);

    int updateByPrimaryKeyWithBLOBs(TestCaseObject record);

    int updateByPrimaryKey(TestCaseObject record);

    List<TestCaseObject> listTestCase(TestCaseObject record);
}