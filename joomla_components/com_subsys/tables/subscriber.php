<?php
/**
 * Hello World table class
 * 
 * @package    Joomla.Tutorials
 * @subpackage Components
 * @link http://docs.joomla.org/Developing_a_Model-View-Controller_Component_-_Part_4
 * @license		GNU/GPL
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );

/**
 * Hello Table class
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
	var $sub_address = null;

	/**
	 * Constructor
	 *
	 * @param object Database connector object
	 */
	function TableSubscriber(& $db) {
		parent::__construct('sms_subscribers', 'sub_id', $db);
	}
}
