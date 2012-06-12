<?php
/**
 * 
 * 
 * @package    Joomla.Tutorials
 * @subpackage Components
 * @link http://docs.joomla.org/Developing_a_Model-View-Controller_Component_-_Part_4
 * @license		GNU/GPL
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );

jimport( 'joomla.application.component.view' );

/**
 * 
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysViewDelivera extends JView
{
	/**
	 * display method 
	 * @return void
	 **/
	function display($tpl = null)
	{
	//get the sub_code and pub_code
	$subcode = JRequest::getVar('sub_code',  0, '', 'int');
	$pubcode = JRequest::getVar('pub_code',  0, '', 'int');
	
	dump($subcode, "Sub Code");
	
		$subscribers =& $this->get('Subscribers');
		$publications =& $this->get('Publications');
  $orders =& $this->get('Orders');
  
		$text =  JText::_( 'New' );
		JToolBarHelper::title(   JText::_( 'Deliveries' ).': <small><small>[ ' . $text.' ]</small></small>' );
		JToolBarHelper::save();
		JToolBarHelper::cancel();
	
		
		//to add datepicker
  $document = &JFactory::getDocument();  
  $document->addScript("includes/js/joomla.javascript.js");    
  JHTML::_('behavior.calendar');
  
  dump($publications, "PUBLICATIONS"); 
  dump($subscribers, "SUBSCRIBERS");
  dump($orders, "ORDERS");

		$this->assignRef('subscribers',	$subscribers);
		$this->assignRef('publications',	$publications);
		$this->assignRef('orders',	$orders);
		$this->assignRef('subcode',	$subcode);
		$this->assignRef('pubcode',	$pubcode);

		parent::display($tpl);
	}
}
