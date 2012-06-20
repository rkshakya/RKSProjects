<?php
/**
 * PUblication table class
 * 
 * @author Ravi Shakya
 * @package    Joomla.Tutorials
 * @pubpackage Components
 * @license		GNU/GPL
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );

/**
 *  Table class
 *
 * @package    Joomla.Tutorials
 * @pubpackage Components
 */
class TablePublication extends JTable
{
	/**
	 * Primary Key
	 *
	 * @var int
	 */
	var $pub_id = null;

	/**
	 * @var string
	 */
	var $pub_code = null;
	var $pub_name = null;
	var $pub_principal = null;
	var $pub_category = null;
	var $pub_frequency = null;
	var $pub_numissues = null;
	var $pub_currency = null;
	var $pub_rate1 = null;
	var $pub_rate2 = null;
	var $pub_rate3 = null;
	var $pub_rate4 = null;
	

	/**
	 * Constructor
	 *
	 * @param object Database connector object
	 */
	function TablePublication(& $db) {
		parent::__construct('sms_publications', 'pub_id', $db);
	}
}
