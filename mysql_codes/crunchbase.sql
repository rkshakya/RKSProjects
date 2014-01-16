-- phpMyAdmin SQL Dump
-- version 3.3.2deb1ubuntu1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 16, 2014 at 05:13 PM
-- Server version: 5.1.61
-- PHP Version: 5.3.2-1ubuntu4.22

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `crunchbase`
--

-- --------------------------------------------------------

--
-- Table structure for table `cb_acquisition`
--

CREATE TABLE IF NOT EXISTS `cb_acquisition` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `company_id` int(8) NOT NULL COMMENT 'fk to id of cb_companies',
  `price_amount` double DEFAULT NULL,
  `price_currency_code` varchar(30) DEFAULT NULL,
  `term_code` varchar(70) DEFAULT NULL,
  `source_url` varchar(260) DEFAULT NULL,
  `source_description` varchar(200) DEFAULT NULL,
  `acquired_year` int(5) DEFAULT NULL,
  `acquired_month` int(3) DEFAULT NULL,
  `acquired_day` int(3) DEFAULT NULL,
  `acquiring_company` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds info about the acquirer company' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `cb_acquisitions`
--

CREATE TABLE IF NOT EXISTS `cb_acquisitions` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `company_id` int(8) NOT NULL COMMENT 'fk to id of cb_companies',
  `price_amount` double DEFAULT NULL,
  `price_currency_code` varchar(30) DEFAULT NULL,
  `term_code` varchar(100) DEFAULT NULL,
  `source_url` varchar(260) DEFAULT NULL,
  `source_description` varchar(260) DEFAULT NULL,
  `acquired_year` int(5) DEFAULT NULL,
  `acquired_month` int(5) DEFAULT NULL,
  `acquired_day` int(5) DEFAULT NULL,
  `company` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds information about the companies acquired' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `cb_companies`
--

CREATE TABLE IF NOT EXISTS `cb_companies` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `nam` varchar(250) NOT NULL,
  `permalink` varchar(250) NOT NULL,
  `category_code` varchar(50) NOT NULL,
  `number_of_employees` int(5) NOT NULL,
  `total_money_raised` varchar(30) NOT NULL,
  `is_processed` tinyint(1) NOT NULL DEFAULT '0',
  `crdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `homepage_url` varchar(250) DEFAULT NULL,
  `founded_year` int(5) DEFAULT NULL,
  `founded_month` int(3) DEFAULT NULL,
  `founded_day` int(3) DEFAULT NULL,
  `deadpooled_year` int(5) DEFAULT NULL,
  `deadpooled_month` int(3) DEFAULT NULL,
  `deadpooled_day` int(3) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `overview` varchar(260) DEFAULT NULL,
  `catcode` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='holds basic information about companies' AUTO_INCREMENT=201632 ;

-- --------------------------------------------------------

--
-- Table structure for table `cb_fundingrounds`
--

CREATE TABLE IF NOT EXISTS `cb_fundingrounds` (
  `frid` int(5) NOT NULL AUTO_INCREMENT,
  `company_id` int(4) NOT NULL,
  `id` int(10) NOT NULL,
  `round_code` varchar(40) NOT NULL,
  `source_description` varchar(250) NOT NULL,
  `raised_amount` double NOT NULL,
  `raised_currency_code` varchar(10) NOT NULL,
  `funded_year` int(5) NOT NULL,
  `funded_month` int(3) NOT NULL,
  `funded_day` int(3) NOT NULL,
  `crdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`frid`),
  KEY `companyid` (`company_id`,`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='holds information about funding rounds for companies' AUTO_INCREMENT=346 ;

-- --------------------------------------------------------

--
-- Table structure for table `cb_investments`
--

CREATE TABLE IF NOT EXISTS `cb_investments` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `company_id` int(8) NOT NULL,
  `round_code` varchar(50) DEFAULT NULL,
  `source_url` varchar(260) DEFAULT NULL,
  `source_description` varchar(260) DEFAULT NULL,
  `raised_amount` double DEFAULT NULL,
  `raised_currency_code` varchar(30) DEFAULT NULL,
  `funded_year` int(5) DEFAULT NULL,
  `funded_month` int(3) DEFAULT NULL,
  `funded_day` int(3) DEFAULT NULL,
  `company` varchar(250) DEFAULT NULL,
  `permalink` varchar(260) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds info about investments made by the company' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `cb_investors`
--

CREATE TABLE IF NOT EXISTS `cb_investors` (
  `id` int(7) NOT NULL AUTO_INCREMENT,
  `companyid` int(5) NOT NULL,
  `roundid` int(7) NOT NULL,
  `category` varchar(20) NOT NULL,
  `nam` varchar(200) DEFAULT NULL,
  `fname` varchar(100) DEFAULT NULL,
  `lname` varchar(100) DEFAULT NULL,
  `permalink` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `companyid` (`companyid`,`roundid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='holds information on investors who invested in the company' AUTO_INCREMENT=739 ;

-- --------------------------------------------------------

--
-- Table structure for table `cb_ipo`
--

CREATE TABLE IF NOT EXISTS `cb_ipo` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `company_id` int(8) NOT NULL COMMENT 'fk to id of cb_companies',
  `valuation_amount` varchar(200) DEFAULT NULL,
  `valuation_currency_code` varchar(30) DEFAULT NULL,
  `pub_year` int(5) DEFAULT NULL,
  `pub_month` int(3) DEFAULT NULL,
  `pub_day` int(3) DEFAULT NULL,
  `stock_symbol` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='contains IPO info for a company' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `cb_offices`
--

CREATE TABLE IF NOT EXISTS `cb_offices` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `company_id` int(8) NOT NULL COMMENT 'fk to id of cb_companies',
  `description` varchar(260) DEFAULT NULL,
  `address1` varchar(250) DEFAULT NULL,
  `address2` varchar(250) DEFAULT NULL,
  `zip_code` varchar(50) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state_code` varchar(60) DEFAULT NULL,
  `country_code` varchar(200) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds info about various office locs of company' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `cb_relationships`
--

CREATE TABLE IF NOT EXISTS `cb_relationships` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `company_id` int(8) NOT NULL COMMENT 'fk_to id of cb_companies',
  `is_past` varchar(10) NOT NULL,
  `title` varchar(200) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `crdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='holds relationships info from crunchbase company data' AUTO_INCREMENT=1 ;
