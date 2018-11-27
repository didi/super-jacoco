-- Create syntax for TABLE 'case'
DROP TABLE IF EXISTS `test_case`;
CREATE TABLE `test_case` (
  `id` int(11) NOT NULL  PRIMARY KEY AUTO_INCREMENT COMMENT "用例id",
  `case_name` varchar(30) DEFAULT NULL COMMENT "用例名称",
  `case_explain` TEXT DEFAULT NULL COMMENT "说明",
  `status` int(11) NOT NULL COMMENT "状态 0:有效 1:无效",
  `gmt_modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "修改时间",
  `gmt_create` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
  `deleted` TINYINT(4) NOT NULL DEFAULT '0',
  `ownner` varchar(255) DEFAULT NULL COMMENT "修改人"
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `test_case` COMMENT='测试用例表';
