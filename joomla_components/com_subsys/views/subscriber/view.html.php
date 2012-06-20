<?php
/**
 * 
 * @package    Joomla.Tutorials
 * @subpackage Components
 * @license		GNU/GPL
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );

jimport( 'joomla.application.component.view' );

/**
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysViewSubscriber extends JView
{
	/**
	 * @return void
	 **/
	function display($tpl = null)
	{
		//get the hello
		$subs		=& $this->get('Data');
		//dump($subs->sub_id, "Sub ID");
		$isNew		= ($subs->sub_id < 1);

		$text = $isNew ? JText::_( 'New' ) : JText::_( 'Edit' );
		JToolBarHelper::title(   JText::_( 'Subscriber' ).': <small><small>[ ' . $text.' ]</small></small>' );
		JToolBarHelper::save();
		if ($isNew)  {
			JToolBarHelper::cancel();
		} else {
			// for existing items the button is renamed `close`
			JToolBarHelper::cancel( 'cancel', 'Close' );
		}

		$this->assignRef('subscriber',		$subs);

		parent::display($tpl);
	}
}
