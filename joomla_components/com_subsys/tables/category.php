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
class TableCategory extends JTable
{
	/**
	 * Primary Key
	 *
	 * @var int
	 */
	var $cat_id = null;

	/**
	 * @var string
	 */
	var $cat_code = null;
	var $cat_name = null;
	var $cat_desc = null;
	

	/**
	 * Constructor
	 *
	 * @param object Database connector object
	 */
	function TableCategory(& $db) {
		parent::__construct('sms_categories', 'cat_id', $db);
	}
}
