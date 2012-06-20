<?php
/**
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
class SubsysViewPublication extends JView
{
	/**
	 * display method of view
	 * @return void
	 **/
	function display($tpl = null)
	{
		//get the categories
		$pubs		=& $this->get('Data');
		//dump($pubs, "Pub ID");
		$isNew		= ($pubs[0]->pub_id < 1);

		$text = $isNew ? JText::_( 'New' ) : JText::_( 'Edit' );
		JToolBarHelper::title(   JText::_( 'Publications' ).': <small><small>[ ' . $text.' ]</small></small>' );
		JToolBarHelper::save();
		if ($isNew)  {
			JToolBarHelper::cancel();
		} else {
			// for existing items the button is renamed `close`
			JToolBarHelper::cancel( 'cancel', 'Close' );
		}

		$this->assignRef('publication',		$pubs[0]);
		$this->assignRef('categories',		$pubs[1]);

		parent::display($tpl);
	}
}
