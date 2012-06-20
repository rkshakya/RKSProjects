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
class SubsysViewCategories extends JView
{
	/**
	 * view display method
	 * @return void
	 **/
	function display($tpl = null)
	{
		JToolBarHelper::title(   JText::_( 'Category Information' ), 'generic.png' );
		JToolBarHelper::deleteList();
		JToolBarHelper::editListX();
		JToolBarHelper::addNewX();
		SubMenuHelper::addSubmenu();		

		// Get data from the model
		$items		= & $this->get( 'Data');
		$pagination =& $this->get('Pagination');
		$lists =& $this->get('Lists');
		//dump($lists, 'Lists');

		$this->assignRef('items',		$items);
		$this->assignRef('pagination', $pagination);
		$this->assignRef('lists', $lists);

		parent::display($tpl);
	}
}
