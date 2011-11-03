-- phpMyAdmin SQL Dump
-- version 2.9.0.2
-- http://www.phpmyadmin.net
-- 
-- Host: sosdev.sdp.stratify.com:3306
-- Generation Time: Jun 13, 2008 at 03:54 AM
-- Server version: 5.0.27
-- PHP Version: 5.1.6
-- 
-- Database: `vms_dev`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_action_info`
-- 

CREATE TABLE `vms_action_info` (
  `GID` int(10) NOT NULL auto_increment COMMENT 'primary key',
  `ACTION` enum('insert','update','delete','rename','move') NOT NULL COMMENT 'action type',
  PRIMARY KEY  (`GID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds info about action type';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_cases`
-- 

CREATE TABLE `vms_cases` (
  `GID` int(11) NOT NULL auto_increment,
  `CASENAME` varchar(50) NOT NULL,
  `HIERARCHY_GID` int(11) default NULL,
  PRIMARY KEY  (`GID`),
  UNIQUE KEY `indx_cases_unq` (`CASENAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds all the casenames';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_caseserver_mapping`
-- 

CREATE TABLE `vms_caseserver_mapping` (
  `GID` int(11) NOT NULL auto_increment,
  `CASE_GID` int(11) NOT NULL COMMENT 'foreign key to str_cases',
  `SERVER_GID` int(11) NOT NULL COMMENT 'foreign key to str_servers',
  PRIMARY KEY  (`GID`),
  KEY `im_case_cascade_ibfk_1` (`CASE_GID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds component case server info mapping';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_compatibility_rules`
-- 

CREATE TABLE `vms_compatibility_rules` (
  `GID` int(11) NOT NULL auto_increment,
  `RULE` int(11) NOT NULL,
  `COMP_VER_GID` int(11) NOT NULL COMMENT 'foreign key to GID of vms_compo_version',
  `MODIFIED_AT` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT 'timestamp of rule modified',
  `MODIFIED_BY` int(11) NOT NULL default '1' COMMENT 'user id of modifier',
  `IS_DELETED` tinyint(2) NOT NULL default '0' COMMENT 'flag to indicate if rule is deleted',
  PRIMARY KEY  (`GID`),
  KEY `IS_DELETED` (`IS_DELETED`),
  KEY `ct_fk_vcr_case` (`COMP_VER_GID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds component compatibility rules';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_compo_version`
-- 

CREATE TABLE `vms_compo_version` (
  `GID` int(11) NOT NULL auto_increment,
  `COMP_GID` int(11) NOT NULL COMMENT 'foreign key to str_components',
  `MAJOR` int(11) NOT NULL,
  `MINOR` int(11) NOT NULL,
  `BUILD` int(11) NOT NULL,
  `PATCH` int(11) NOT NULL,
  PRIMARY KEY  (`GID`),
  KEY `ct_fk_vcv_component` (`COMP_GID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds component version info';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_component_mapping`
-- 

CREATE TABLE `vms_component_mapping` (
  `GID` int(11) NOT NULL auto_increment,
  `CASE_SERVER_GID` int(11) NOT NULL,
  `COMPO_VER_GID` int(11) NOT NULL,
  `IS_DELETED` tinyint(4) NOT NULL default '0',
  `CREATED_DATE` timestamp NULL default CURRENT_TIMESTAMP,
  `MODIFIED_DATE` timestamp NULL default '0000-00-00 00:00:00',
  `PORT` varchar(50) default NULL,
  PRIMARY KEY  (`GID`),
  UNIQUE KEY `CASE_SERVER_GID` (`CASE_SERVER_GID`,`COMPO_VER_GID`,`PORT`),
  KEY `IS_DELETED` (`IS_DELETED`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_components`
-- 

CREATE TABLE `vms_components` (
  `GID` int(11) NOT NULL auto_increment,
  `COMPONENT` varchar(300) NOT NULL,
  `BE` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`GID`),
  UNIQUE KEY `str_compo_unq1` (`COMPONENT`),
  KEY `COMPONENT` (`COMPONENT`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='component names ';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_crawl_deviation_history`
-- 

CREATE TABLE `vms_crawl_deviation_history` (
  `GID` int(100) NOT NULL auto_increment COMMENT 'primary key',
  `CRAWL_DATE` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT 'datetime when crawl was run',
  `DEVIATION_TYPE` enum('FATAL','ERROR','CRITICAL','NONFATAL') NOT NULL COMMENT 'Type of Deviation',
  `DEVIATION_MESSAGE` varchar(1000) NOT NULL COMMENT 'Deviation message',
  `CASE_GID` int(11) NOT NULL COMMENT 'key to GID from vms_cases',
  PRIMARY KEY  (`GID`),
  KEY `vms_indx_crawl_deviation` (`CASE_GID`,`CRAWL_DATE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='stores crawl deviation history';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_mailer_history`
-- 

CREATE TABLE `vms_mailer_history` (
  `GID` bigint(225) NOT NULL auto_increment COMMENT 'primary key',
  `EVENT_DATE` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT 'date of event',
  `CASE_GID` int(100) default NULL COMMENT 'gid of case from vms_cases',
  `SERVER_GID` int(100) default NULL COMMENT 'gid from vms_servers',
  `COMPONENT_GID` int(100) default NULL COMMENT 'gid from vms_components',
  `ERROR_MESSAGE` varchar(1000) NOT NULL COMMENT 'error message logged',
  PRIMARY KEY  (`GID`),
  KEY `vms_indx_mailer` (`CASE_GID`,`EVENT_DATE`),
  KEY `ct_fk1_server` (`SERVER_GID`),
  KEY `ct_fk1_component` (`COMPONENT_GID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='table recording mailer events hiestory';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_rules_info`
-- 

CREATE TABLE `vms_rules_info` (
  `GID` int(100) NOT NULL auto_increment COMMENT 'primary key',
  `NAME` varchar(500) default NULL COMMENT 'rule name',
  `DESCRIPTION` varchar(2000) default NULL COMMENT 'rule description',
  `RULE` int(10) NOT NULL COMMENT 'rule number',
  `CREATED_BY` int(10) NOT NULL default '1' COMMENT 'user id',
  `CREATED_AT` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `MODIFIED_BY` int(10) default NULL COMMENT 'user id',
  `MODIFIED_AT` timestamp NULL default NULL,
  PRIMARY KEY  (`GID`),
  UNIQUE KEY `RULE_NUM_2` (`NAME`),
  KEY `ct_fk1_user` (`CREATED_BY`),
  KEY `ct_fk1_user1` (`MODIFIED_BY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds additional info about a rule';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_rules_info_history`
-- 

CREATE TABLE `vms_rules_info_history` (
  `GID` int(10) NOT NULL auto_increment COMMENT 'primary key',
  `RULE` int(10) NOT NULL COMMENT 'rule number',
  `NAME` varchar(500) NOT NULL COMMENT 'rule name',
  `DESCRIPTION` varchar(2000) NOT NULL COMMENT 'rule description',
  `CREATED_BY` int(10) NOT NULL COMMENT 'user id',
  `CREATED_AT` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT 'datetime',
  `ACTION_GID` int(10) NOT NULL COMMENT 'action gid',
  PRIMARY KEY  (`GID`),
  KEY `ct_fk_action` (`ACTION_GID`),
  KEY `ct_fk_user` (`CREATED_BY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds rules info history ';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_servers`
-- 

CREATE TABLE `vms_servers` (
  `GID` int(11) NOT NULL auto_increment,
  `SERVER` varchar(200) NOT NULL,
  `DOMAIN` varchar(5) NOT NULL,
  PRIMARY KEY  (`GID`),
  UNIQUE KEY `SERVER_2` (`SERVER`,`DOMAIN`),
  KEY `SERVER` (`SERVER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds all the server names';

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_temp_invalid_components`
-- 

CREATE TABLE `vms_temp_invalid_components` (
  `CASE_GID` int(100) NOT NULL,
  `COMPONENT` varchar(200) NOT NULL,
  `VERSION` varchar(50) NOT NULL,
  `SERVER` varchar(100) NOT NULL,
  `PORTDETAILS` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

-- 
-- Table structure for table `vms_users`
-- 

CREATE TABLE `vms_users` (
  `GID` int(10) NOT NULL auto_increment COMMENT 'primary key',
  `USERNAME` varchar(200) NOT NULL COMMENT 'username',
  PRIMARY KEY  (`GID`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds user info';

-- 
-- Constraints for dumped tables
-- 

-- 
-- Constraints for table `vms_caseserver_mapping`
-- 
ALTER TABLE `vms_caseserver_mapping`
  ADD CONSTRAINT `ct_fk_vcm_case` FOREIGN KEY (`CASE_GID`) REFERENCES `vms_cases` (`GID`);

-- 
-- Constraints for table `vms_compatibility_rules`
-- 
ALTER TABLE `vms_compatibility_rules`
  ADD CONSTRAINT `ct_fk_vcr_case` FOREIGN KEY (`COMP_VER_GID`) REFERENCES `vms_compo_version` (`GID`);

-- 
-- Constraints for table `vms_compo_version`
-- 
ALTER TABLE `vms_compo_version`
  ADD CONSTRAINT `ct_fk_vcv_component` FOREIGN KEY (`COMP_GID`) REFERENCES `vms_components` (`GID`);

-- 
-- Constraints for table `vms_crawl_deviation_history`
-- 
ALTER TABLE `vms_crawl_deviation_history`
  ADD CONSTRAINT `ct_fk_vcdh_case` FOREIGN KEY (`CASE_GID`) REFERENCES `vms_cases` (`GID`);

-- 
-- Constraints for table `vms_mailer_history`
-- 
ALTER TABLE `vms_mailer_history`
  ADD CONSTRAINT `ct_fk1_component` FOREIGN KEY (`COMPONENT_GID`) REFERENCES `vms_components` (`GID`),
ALTER TABLE `vms_mailer_history`
  ADD CONSTRAINT `ct_fk1_component` FOREIGN KEY (`COMPONENT_GID`) REFERENCES `vms_components` (`GID`),  ADD CONSTRAINT `ct_fk1_case` FOREIGN KEY (`CASE_GID`) REFERENCES `vms_cases` (`GID`),
ALTER TABLE `vms_mailer_history`
  ADD CONSTRAINT `ct_fk1_component` FOREIGN KEY (`COMPONENT_GID`) REFERENCES `vms_components` (`GID`),  ADD CONSTRAINT `ct_fk1_case` FOREIGN KEY (`CASE_GID`) REFERENCES `vms_cases` (`GID`),  ADD CONSTRAINT `ct_fk1_server` FOREIGN KEY (`SERVER_GID`) REFERENCES `vms_servers` (`GID`);

-- 
-- Constraints for table `vms_rules_info`
-- 
ALTER TABLE `vms_rules_info`
  ADD CONSTRAINT `ct_fk1_user1` FOREIGN KEY (`MODIFIED_BY`) REFERENCES `vms_users` (`GID`),
ALTER TABLE `vms_rules_info`
  ADD CONSTRAINT `ct_fk1_user1` FOREIGN KEY (`MODIFIED_BY`) REFERENCES `vms_users` (`GID`),  ADD CONSTRAINT `ct_fk1_user` FOREIGN KEY (`CREATED_BY`) REFERENCES `vms_users` (`GID`);

-- 
-- Constraints for table `vms_rules_info_history`
-- 
ALTER TABLE `vms_rules_info_history`
  ADD CONSTRAINT `ct_fk_user` FOREIGN KEY (`CREATED_BY`) REFERENCES `vms_users` (`GID`),
ALTER TABLE `vms_rules_info_history`
  ADD CONSTRAINT `ct_fk_user` FOREIGN KEY (`CREATED_BY`) REFERENCES `vms_users` (`GID`),  ADD CONSTRAINT `ct_fk_action` FOREIGN KEY (`ACTION_GID`) REFERENCES `vms_action_info` (`GID`);

-- 
-- Procedures
-- 
-- DELIMITER $$
-- 
CREATE DEFINER=`DBADMIN`@`%` PROCEDURE `VMS_DETECT_MULTIPLE_INSTALLATIONS`()
BEGIN

SELECT CAS.GID AS CASEGID, CAS.CASENAME, SERV.GID AS SERVERGID, SERV.SERVER, COMPO.GID,COMPO.COMPONENT,
CONCAT_WS('.', VERS.MAJOR, VERS.MINOR, VERS.BUILD, VERS.PATCH) AS VERSION, COMPOMAP.PORT   FROM
vms_component_mapping COMPOMAP, vms_caseserver_mapping CASEMAP,
vms_compo_version VERS, vms_components COMPO, vms_servers SERV,
vms_cases CAS
WHERE COMPOMAP.GID IN(
SELECT DISTINCT MAP.GID
FROM vms_component_mapping MAP, vms_component_mapping MAP1
WHERE MAP.CASE_SERVER_GID = MAP1.CASE_SERVER_GID AND
MAP.COMPO_VER_GID = MAP1.COMPO_VER_GID AND
MAP.GID <> MAP1.GID) AND
COMPOMAP.IS_DELETED = 0 AND
COMPOMAP.CASE_SERVER_GID = CASEMAP.GID AND
CASEMAP.CASE_GID = CAS.GID AND
CASEMAP.SERVER_GID = SERV.GID AND
COMPOMAP.COMPO_VER_GID = VERS.GID AND
VERS.COMP_GID = COMPO.GID
ORDER BY CAS.GID;

END$$

CREATE DEFINER=`DBADMIN`@`%` PROCEDURE `VMS_POPULATE_INVALID_COMPOS`()
BEGIN
DECLARE case_gid  INT ;
DECLARE  error_condition INT;
DECLARE CASE_CURSOR CURSOR FOR SELECT DISTINCT GID FROM vms_cases;
DECLARE CONTINUE HANDLER FOR NOT FOUND
SET error_condition = 1;


DROP TABLE  IF EXISTS vms_temp_invalid_components;

#create table
CREATE TABLE `vms_temp_invalid_components` (
`CASE_GID` INT( 100 ) NOT NULL ,
`COMPONENT` VARCHAR( 200 ) NOT NULL ,
`VERSION` VARCHAR( 50 ) NOT NULL ,
`SERVER` VARCHAR( 100 ) NOT NULL ,
`PORTDETAILS` VARCHAR( 100 ) NOT NULL
);

#SELECT 'vms_temp_invalid_components TABLE CREATED';



OPEN CASE_CURSOR;
  REPEAT
  FETCH CASE_CURSOR INTO case_gid;
  #SELECT CONCAT_WS('+','POPULATING FOR CASEGID: ',case_gid);
  #insert the results into table
  INSERT INTO vms_temp_invalid_components
SELECT C.GID, CO.COMPONENT, CONCAT_WS('.',CV.MAJOR, CV.MINOR , CV.BUILD ,CV.PATCH), S.SERVER, CM.PORT
FROM vms_cases C, vms_servers S, vms_caseserver_mapping CSM, vms_component_mapping CM, vms_compo_version CV, vms_components CO
WHERE S.GID = CSM.SERVER_GID
AND CSM.GID = CM.CASE_SERVER_GID
AND CM.COMPO_VER_GID = CV.GID
AND CV.COMP_GID = CO.GID
AND CM.IS_DELETED = 0
AND CM.COMPO_VER_GID IN (
SELECT DISTINCT CM.COMPO_VER_GID
FROM vms_component_mapping CM, vms_caseserver_mapping CS
WHERE CS.GID = CM.CASE_SERVER_GID
AND CS.CASE_GID = case_gid
AND COMPO_VER_GID NOT IN (
SELECT COMP_VER_GID FROM VMS_COMPATIBILITY_RULES
WHERE RULE =
(
SELECT RULE FROM VMS_COMPATIBILITY_RULES
WHERE COMP_VER_GID IN (
SELECT CM.COMPO_VER_GID
FROM vms_component_mapping CM, vms_caseserver_mapping CS
WHERE CS.GID = CM.CASE_SERVER_GID
AND CS.CASE_GID = case_gid
AND CM.IS_DELETED = 0
)
AND IS_DELETED = 0
GROUP BY RULE
HAVING COUNT(GID) > 1
ORDER BY COUNT(GID) DESC
LIMIT 0,1
)
AND IS_DELETED = 0
)
AND CM.IS_DELETED = 0
)
AND C.GID = CSM.CASE_GID
AND C.GID = case_gid
ORDER BY CO.BE, S.DOMAIN, CO.COMPONENT ;

  #SELECT CONCAT_WS('+','ROWS INSERTED: ',ROW_COUNT());

  UNTIL error_condition = 1
  END REPEAT;
CLOSE CASE_CURSOR;

SELECT TMP.CASE_GID,C.CASENAME, TMP.COMPONENT, TMP.VERSION, TMP.SERVER, TMP.PORTDETAILS
FROM vms_temp_invalid_components TMP, vms_cases C
WHERE TMP.CASE_GID = C.GID
ORDER BY TMP.CASE_GID;

END$$

-- 
-- DELIMITER ;
-- 
