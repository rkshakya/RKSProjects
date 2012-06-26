<?php
/**
 * Subscriber table class
 * 
 * @package    Joomla.Tutorials
 * @subpackage Components
 * @license		GNU/GPL
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );

/**
 * Subscriber Table class
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class TableSubscriber extends JTable
{
	/**
	 * Primary Key
	 *
	 * @var int
	 */
	var $sub_id = null;

	/**
	 * @var string
	 */
	var $sub_code = null;
	var $sub_name = null;
	var $sub_category = null;
	var $sub_address = null;
	var $sub_city = null;
	var $sub_pobox = null;
	var $sub_phone = null;
	var $sub_fax = null;
	var $sub_email = null;
	var $sub_web = null;
	var $sub_cp = null;
	var $sub_cpd = null;
	var $sub_cdate = null;
 var $customer_id = null;
	

	/**
	 * Constructor
	 *
	 * @param object Database connector object
	 */
	function TableSubscriber(& $db) {
		parent::__construct('sms_subscribers', 'sub_id', $db);
	}
}
