-- phpMyAdmin SQL Dump
-- version 3.3.2deb1ubuntu1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 27, 2012 at 06:48 PM
-- Server version: 5.1.61
-- PHP Version: 5.3.2-1ubuntu4.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `subsys`
--

-- --------------------------------------------------------

--
-- Table structure for table `sms_categories`
--

CREATE TABLE IF NOT EXISTS `sms_categories` (
  `cat_id` int(10) NOT NULL AUTO_INCREMENT,
  `cat_code` int(10) DEFAULT NULL,
  `cat_name` varchar(100) DEFAULT NULL,
  `cat_desc` text,
  `cdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NULL DEFAULT NULL,
  `state` int(10) DEFAULT NULL,
  PRIMARY KEY (`cat_id`),
  UNIQUE KEY `cat_code` (`cat_code`) USING BTREE,
  UNIQUE KEY `cat_name` (`cat_name`) USING BTREE,
  KEY `cat_id` (`cat_id`) USING BTREE,
  KEY `state` (`state`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25 ;

--
-- Triggers `sms_categories`
--
DROP TRIGGER IF EXISTS `sms_trgr_cat_mdate`;
DELIMITER //
CREATE TRIGGER `sms_trgr_cat_mdate` BEFORE UPDATE ON `sms_categories`
 FOR EACH ROW begin
set new.mdate = now()
;
end
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `sms_deliveries`
--

CREATE TABLE IF NOT EXISTS `sms_deliveries` (
  `delivery_id` int(10) NOT NULL AUTO_INCREMENT,
  `subscription_id` int(10) DEFAULT NULL,
  `delivery_date` timestamp NULL DEFAULT NULL,
  `delivery_title` varchar(100) DEFAULT NULL,
  `order_code` varchar(16) DEFAULT NULL,
  `sub_code` varchar(6) DEFAULT NULL,
  `pub_code` varchar(4) DEFAULT NULL,
  `delivery_issue` varchar(20) DEFAULT NULL,
  `delivery_issuedt` date NOT NULL DEFAULT '0000-00-00' COMMENT 'issue date',
  `delivery_copies` int(10) DEFAULT NULL,
  `delivery_done` bit(1) NOT NULL,
  `delivered_by` varchar(30) DEFAULT NULL,
  `received_by` varchar(30) DEFAULT NULL,
  `cdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NULL DEFAULT NULL,
  `state` int(10) DEFAULT NULL,
  PRIMARY KEY (`delivery_id`),
  KEY `delivered_by` (`delivered_by`) USING BTREE,
  KEY `delivery_date` (`delivery_date`) USING BTREE,
  KEY `delivery_done` (`delivery_done`) USING BTREE,
  KEY `delivery_id` (`delivery_id`) USING BTREE,
  KEY `pub_code` (`pub_code`) USING BTREE,
  KEY `received_by` (`received_by`) USING BTREE,
  KEY `sms_deliveriesorder_code` (`order_code`) USING BTREE,
  KEY `state` (`state`) USING BTREE,
  KEY `sub_code` (`sub_code`) USING BTREE,
  KEY `subscription_id` (`subscription_id`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25514 ;

--
-- Triggers `sms_deliveries`
--
DROP TRIGGER IF EXISTS `sms_trgr_del_mdate`;
DELIMITER //
CREATE TRIGGER `sms_trgr_del_mdate` BEFORE UPDATE ON `sms_deliveries`
 FOR EACH ROW begin set new.mdate = now() ; end
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `sms_orders`
--

CREATE TABLE IF NOT EXISTS `sms_orders` (
  `order_id` int(10) NOT NULL AUTO_INCREMENT,
  `order_code` varchar(16) DEFAULT NULL,
  `order_date` timestamp NULL DEFAULT NULL,
  `order_title` varchar(100) DEFAULT NULL,
  `sub_code` varchar(6) DEFAULT NULL,
  `order_invno` varchar(16) DEFAULT NULL,
  `order_invamt` decimal(19,4) DEFAULT NULL,
  `order_paid` varchar(5) DEFAULT NULL,
  `cdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NULL DEFAULT NULL,
  `state` int(10) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_code` (`order_code`) USING BTREE,
  KEY `order_date` (`order_date`) USING BTREE,
  KEY `order_id` (`order_id`) USING BTREE,
  KEY `order_paid` (`order_paid`) USING BTREE,
  KEY `sms_orderssub_code` (`sub_code`) USING BTREE,
  KEY `sms_subscriberssms_orders` (`sub_code`) USING BTREE,
  KEY `state` (`state`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1077 ;

--
-- Triggers `sms_orders`
--
DROP TRIGGER IF EXISTS `sms_trgr_ord_mdate`;
DELIMITER //
CREATE TRIGGER `sms_trgr_ord_mdate` BEFORE UPDATE ON `sms_orders`
 FOR EACH ROW begin set new.mdate = now() ; end
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `sms_publications`
--

CREATE TABLE IF NOT EXISTS `sms_publications` (
  `pub_id` int(10) NOT NULL AUTO_INCREMENT,
  `pub_code` varchar(10) DEFAULT NULL,
  `pub_name` varchar(200) DEFAULT NULL,
  `pub_principal` varchar(100) DEFAULT NULL,
  `pub_category` int(10) DEFAULT NULL,
  `pub_frequency` varchar(20) DEFAULT NULL,
  `pub_numissues` int(10) DEFAULT NULL,
  `pub_currency` varchar(20) DEFAULT NULL,
  `pub_rate1` decimal(19,4) DEFAULT NULL,
  `pub_rate2` decimal(19,4) DEFAULT NULL,
  `pub_rate3` decimal(19,4) DEFAULT NULL,
  `pub_rate4` decimal(19,4) DEFAULT NULL,
  `cdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NULL DEFAULT NULL,
  `state` int(10) DEFAULT NULL,
  PRIMARY KEY (`pub_id`),
  UNIQUE KEY `pub_code` (`pub_code`) USING BTREE,
  UNIQUE KEY `pub_name` (`pub_name`) USING BTREE,
  KEY `pub_category` (`pub_category`) USING BTREE,
  KEY `pub_currency` (`pub_currency`) USING BTREE,
  KEY `pub_frequency` (`pub_frequency`) USING BTREE,
  KEY `pub_id` (`pub_id`) USING BTREE,
  KEY `state` (`state`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=283 ;

--
-- Triggers `sms_publications`
--
DROP TRIGGER IF EXISTS `sms_trgr_pub_mdate`;
DELIMITER //
CREATE TRIGGER `sms_trgr_pub_mdate` BEFORE UPDATE ON `sms_publications`
 FOR EACH ROW begin set new.mdate = now() ; end
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `sms_subscribers`
--

CREATE TABLE IF NOT EXISTS `sms_subscribers` (
  `sub_id` int(10) NOT NULL AUTO_INCREMENT,
  `sub_code` varchar(6) DEFAULT NULL,
  `sub_name` varchar(100) DEFAULT NULL,
  `sub_category` varchar(50) DEFAULT NULL,
  `sub_address` varchar(250) DEFAULT NULL,
  `sub_city` varchar(50) DEFAULT NULL,
  `sub_pobox` varchar(20) DEFAULT NULL,
  `sub_phone` varchar(50) DEFAULT NULL,
  `sub_fax` varchar(50) DEFAULT NULL,
  `sub_email` varchar(100) DEFAULT NULL,
  `sub_web` varchar(100) DEFAULT NULL,
  `sub_cp` varchar(100) DEFAULT NULL,
  `sub_cpd` varchar(50) DEFAULT NULL,
  `cdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NULL DEFAULT NULL,
  `state` int(10) DEFAULT NULL,
  `customer_id` varchar(30) NOT NULL COMMENT 'holds global customer id provided by vendors like TIME etc',
  PRIMARY KEY (`sub_id`),
  UNIQUE KEY `sub_code` (`sub_code`) USING BTREE,
  UNIQUE KEY `sub_name` (`sub_name`) USING BTREE,
  KEY `state` (`state`) USING BTREE,
  KEY `sub_category` (`sub_category`) USING BTREE,
  KEY `sub_city` (`sub_city`) USING BTREE,
  KEY `sub_cp` (`sub_cp`) USING BTREE,
  KEY `sub_email` (`sub_email`) USING BTREE,
  KEY `sub_id` (`sub_id`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=775 ;

--
-- Triggers `sms_subscribers`
--
DROP TRIGGER IF EXISTS `sms_trgr_sub_mdate`;
DELIMITER //
CREATE TRIGGER `sms_trgr_sub_mdate` BEFORE UPDATE ON `sms_subscribers`
 FOR EACH ROW begin set new.mdate = now() ; end
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `sms_subscriptions`
--

CREATE TABLE IF NOT EXISTS `sms_subscriptions` (
  `subscription_id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` int(10) DEFAULT NULL,
  `subscription_title` varchar(100) DEFAULT NULL,
  `order_code` varchar(16) DEFAULT NULL,
  `sub_code` varchar(6) DEFAULT NULL,
  `pub_code` varchar(4) DEFAULT NULL,
  `num_issues` int(10) DEFAULT NULL,
  `num_copies` int(10) DEFAULT NULL,
  `issue_from` varchar(10) DEFAULT NULL,
  `issue_to` varchar(10) DEFAULT NULL,
  `start_date` timestamp NULL DEFAULT NULL,
  `exp_date` timestamp NULL DEFAULT NULL,
  `mdate` timestamp NULL DEFAULT NULL,
  `cdate` timestamp NULL DEFAULT NULL,
  `state` int(10) DEFAULT NULL,
  PRIMARY KEY (`subscription_id`),
  KEY `exp_date` (`exp_date`) USING BTREE,
  KEY `order_id` (`subscription_id`) USING BTREE,
  KEY `order_id1` (`order_id`) USING BTREE,
  KEY `pub_code` (`pub_code`) USING BTREE,
  KEY `sms_publicationssms_subscriptions` (`pub_code`) USING BTREE,
  KEY `sms_subscriptionsorder_code` (`order_code`) USING BTREE,
  KEY `start_date` (`start_date`) USING BTREE,
  KEY `state` (`state`) USING BTREE,
  KEY `sub_code` (`sub_code`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1519 ;

--
-- Triggers `sms_subscriptions`
--
DROP TRIGGER IF EXISTS `sms_trgr_subscription_mdate`;
DELIMITER //
CREATE TRIGGER `sms_trgr_subscription_mdate` BEFORE UPDATE ON `sms_subscriptions`
 FOR EACH ROW begin set new.mdate = now() ; end
//
DELIMITER ;
