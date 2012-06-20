<?php
/**
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
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysViewCategory extends JView
{
	/**
	 * display method of view
	 * @return void
	 **/
	function display($tpl = null)
	{
		//get the categories
		$cats		=& $this->get('Data');
		//dump($cats->cat_id, "Cat ID");
		$isNew		= ($cats->cat_id < 1);

		$text = $isNew ? JText::_( 'New' ) : JText::_( 'Edit' );
		JToolBarHelper::title(   JText::_( 'Category' ).': <small><small>[ ' . $text.' ]</small></small>' );
		JToolBarHelper::save();
		if ($isNew)  {
			JToolBarHelper::cancel();
		} else {
			// for existing items the button is renamed `close`
			JToolBarHelper::cancel( 'cancel', 'Close' );
		}

		$this->assignRef('category',		$cats);

		parent::display($tpl);
	}
}
