<?php
/**
 * PUblication table class
 * 
 * @package    Joomla.Tutorials
 * @pubpackage Components
 * @link http://docs.joomla.org/Developing_a_Model-View-Controller_Component_-_Part_4
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
class TableDelivery extends JTable
{
	/**
	 * Primary Key
	 *
	 * @var int
	 */
	var $delivery_id = null;

	/**
	 * @var string
	 */
	var $delivery_date = null;
	var $delivery_title = null;	
	var $delivery_issue = null;
	var $delivery_copies = null;
	var $delivered_by = null;
	var $received_by = null;
	

	/**
	 * Constructor
	 *
	 * @param object Database connector object
	 */
	function TableDelivery(& $db) {
		parent::__construct('sms_deliveries', 'delivery_id', $db);
	}
}
