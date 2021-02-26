CREATE DATABASE `super-jacoco` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
CREATE TABLE `diff_coverage_report` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `job_record_uuid` varchar(80) NOT NULL COMMENT '请求唯一标识码',
  `request_status` int(10) NOT NULL COMMENT '请求执行状态,1=下载代码成功,2=生成diffmethod成功，3=生成报告成功,-1=执行出错',
  `giturl` varchar(80) NOT NULL COMMENT 'git 地址',
  `now_version` varchar(80) NOT NULL COMMENT '本次提交的commidId',
  `base_version` varchar(80) NOT NULL COMMENT '比较的基准commitId',
  `diffmethod` mediumtext COMMENT '增量代码的diff方法集合',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '2=增量代码覆盖率,1=全量覆盖率',
  `report_url` varchar(300) NOT NULL DEFAULT '' COMMENT '覆盖率报告url',
  `line_coverage` double(5,2) NOT NULL DEFAULT '-1.00' COMMENT '行覆盖率',
  `branch_coverage` double(5,2) NOT NULL DEFAULT '-1.00' COMMENT '分支覆盖率',
  `err_msg` varchar(1000) NOT NULL DEFAULT '' COMMENT '错误信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `sub_module` varchar(255) NOT NULL DEFAULT '' COMMENT '子项目目录名称',
  `from` int(10) NOT NULL DEFAULT '0' COMMENT '1=单元测试，2=环境部署1=单元测试，2=hu',
  `now_local_path` varchar(500) NOT NULL DEFAULT '',
  `base_local_path` varchar(500) NOT NULL DEFAULT '',
  `log_file` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`job_record_uuid`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='增量代码覆盖率';

CREATE TABLE `diff_deploy_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `job_record_uuid` varchar(80) NOT NULL COMMENT '请求唯一标识码',
  `address` varchar(15) NOT NULL COMMENT 'HOST',
  `port` int(10) NOT NULL COMMENT '端口',
  `code_path` varchar(1000) NOT NULL DEFAULT '' COMMENT 'nowVersion代码目录',
  `child_modules` varchar(1000) NOT NULL DEFAULT '' COMMENT '项目子模块名称',
  PRIMARY KEY (`job_record_uuid`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='服务部署地址';



