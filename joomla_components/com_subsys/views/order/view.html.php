<?php
/**
 * 
 * @author Ravi Shakya
 * @package    Joomla.Tutorials
 * @subpackage Components
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
class SubsysViewOrder extends JView
{
	/**
	 * display method 
	 * @return void
	 **/
	function display($tpl = null)
	{
	//get the sub_code and order_id
	$subcode = JRequest::getVar('sub_code',  0, '', 'int');
  $array = JRequest::getVar('cid',  0, '', 'array');
  $orderId = (int)$array[0];
	
	//dump($subcode, "Sub Code");
	//dump($orderId, "Order ID");
	
		//get the order info
		$ords		=& $this->get('Data');
		$subs =& $this->get('Subscriptions');
		$subscribers =& $this->get('Subscribers');
		$publications =& $this->get('Publications');
	
		//dump($ords, "Ords");
		$isNew		= ($ords->order_code < 1);

		$text = $isNew ? JText::_( 'New' ) : JText::_( 'Edit' );
		JToolBarHelper::title(   JText::_( 'Orders' ).': <small><small>[ ' . $text.' ]</small></small>' );
		JToolBarHelper::save();
		if ($isNew)  {
			JToolBarHelper::cancel();
		} else {
			// for existing items the button is renamed `close`
			JToolBarHelper::cancel( 'cancel', 'Close' );
		}
		
		//to add datepicker
  $document = &JFactory::getDocument();  
  $document->addScript("includes/js/joomla.javascript.js");    
  JHTML::_('behavior.calendar');
  
 // dump($subs, "PAKHND"); 
 // dump($subscribers, "SUBSCRIBERS");

		$this->assignRef('order',	$ords);
		$this->assignRef('subscriptions',	$subs);
		$this->assignRef('subscribers',	$subscribers);
		$this->assignRef('publications',	$publications);
		$this->assignRef('subcode',	$subcode);

		parent::display($tpl);
	}
}
