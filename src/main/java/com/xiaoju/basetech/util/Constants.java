package com.xiaoju.basetech.util;


/**
 * @description:任务状态描述
 * @author: charlynegaoweiwei
 * @time: 2020/4/27 11:04 AM
 */
public class Constants {

    public static final String CODE_ROOT = System.getProperty("user.home")+"/app/super_jacoco/clonecode/";
    public static final String LOG_PATH = System.getProperty("user.home")+"/report/logs/";
    public static final String REPORT_PATH = System.getProperty("user.home")+"/report/";
    public static final String NO_DIFFCODE_REPORT = LocalIpUtils.getTomcatBaseUrl() + "nodiffcode.html";
    public static final String JACOCO_RESOURE_PATH = "/app/diff_code_coverage/resource/jacoco-resources";

    public enum JobStatus {

        INITIAL(0, "初始数据"),
        WAITING(1, "待执行"),

        CLONING(2, "下载代码中"),
        CLONE_DONE(102, "下载代码成功"),
        CLONE_FAIL(202, "下载代码失败"),

        COMPILING(3, "编译中"),
        COMPILE_DONE(103, "编译成功"),
        COMPILE_FAIL(203, "编译失败"),

        DIFF_METHODS_EXECUTING(4, "计算增量方法中"),
        DIFF_METHOD_DONE(104, "计算增量方法成功"),
        DIFF_METHOD_FAIL(204, "计算增量方法失败"),

        ADDMODULING(5, "添加集成模块中"),
        ADDMODULE_DONE(105, "添加集成模块成功"),
        FAILADDMODULE(205, "添加集成自模块失败"),

        UNITTESTEXECUTING(6, "单元测试执行中"),
        UNITTEST_DONE(106, "执行单元测试成功"),
        UNITTEST_FAIL(206, "执行单元测试失败"),

        REPORTGENERATING(7, "生成报告中"),
        GENERATEREPORT_DONE(107, "生成报告成功"),
        GENERATEREPORT_FAIL(207, "生成报告失败"),

        REPORTPARSING(8, "分析报告中"),
        PARSEREPORT_DONE(108, "分析报告成功"),
        FAILPARSEREPOAT(208, "分析报告失败"),

        REPORTCOPYING(9, "复制报告中"),
        COPYREPORT_DONE(109, "复制报告成功"),
        COPYREPORT_FAIL(209, "复制报告失败"),

        SUCCESS(200, "执行成功"),
        NODIFF(100, "无增量"),
        TIMEOUT(210, "超时"),
        REMOVE_FILE_ING(11, "删除源文件中"),
        REMOVE_FILE_DONE(111, "删除源文件成功"),
        REMOVE_FILE_FAIL(211, "删除源文件失败"),

        ENVREPORT_FAIL(212, "统计功能测试增量覆盖率失败"),
        WAITING_PULL_EXEC(12,"等待统计功能测试增量覆盖率中");



        private int value;
        private String desc;

        JobStatus(int value, String desc) {
            this.value = value;
            this.desc = desc;

        }

        public int val() {
            return this.value;
        }

        public String desc() {
            return desc;
        }

        public static String desc(int value) {
            for (JobStatus type : JobStatus.values()) {
                if (type.val() == value) {
                    return type.desc();
                }
            }
            return "unknown";
        }
    }

    public enum ReportType {
        FULL(1, "全量覆盖率"),
        DIFF(2, "增量覆盖率");
        private int value;
        private String desc;

        ReportType(int value, String desc) {
            this.value = value;
            this.desc = desc;

        }

        public int val() {
            return this.value;
        }

        public String desc() {
            return desc;
        }

        public static String desc(int value) {
            for (JobStatus type : JobStatus.values()) {
                if (type.val() == value) {
                    return type.desc();
                }
            }
            return "unknown";
        }

    }

    public enum CoverageFrom {
        UNIT(1, "单元测试"),
        ENV(2, "环境部署");
        private int value;
        private String desc;

        CoverageFrom(int value, String desc) {
            this.value = value;
            this.desc = desc;

        }

        public int val() {
            return this.value;
        }

        public String desc() {
            return desc;
        }

        public static String desc(int value) {
            for (JobStatus type : JobStatus.values()) {
                if (type.val() == value) {
                    return type.desc();
                }
            }
            return "unknown";
        }

    }

}
