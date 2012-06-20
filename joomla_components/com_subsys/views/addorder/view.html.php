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
class SubsysViewAddorder extends JView
{
	/**
	 * display method 
	 * @return void
	 **/
	function display($tpl = null)
	{
	//get the sub_code and order_id
	$subcode = JRequest::getVar('sub_code',  0, '', 'int');
	
	//dump($subcode, "Sub Code");
	
		//get the order info
		$subscribers =& $this->get('Subscribers');
		$publications =& $this->get('Publications');

		$text =  JText::_( 'New' );
		JToolBarHelper::title(   JText::_( 'Orders' ).': <small><small>[ ' . $text.' ]</small></small>' );
		JToolBarHelper::save();
		JToolBarHelper::cancel();
	
		
		//to add datepicker
  $document = &JFactory::getDocument();  
  $document->addScript("includes/js/joomla.javascript.js");    
  JHTML::_('behavior.calendar');
  
  //dump($publications, "PUBLICATIONS"); 
  //dump($subscribers, "SUBSCRIBERS");

		$this->assignRef('subscribers',	$subscribers);
		$this->assignRef('publications',	$publications);
		$this->assignRef('subcode',	$subcode);

		parent::display($tpl);
	}
}
