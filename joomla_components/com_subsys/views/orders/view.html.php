<?php
/**
 *  Component
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
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysViewOrders extends JView
{
	/**
	 * Hellos view display method
	 * @return void
	 **/
	function display($tpl = null)
	{
		JToolBarHelper::title(   JText::_( 'Orders Information' ), 'generic.png' );
		JToolBarHelper::deleteList();
		JToolBarHelper::editListX();
		JToolBarHelper::addNewX();
		SubMenuHelper::addSubmenu();		

		// Get data from the model
		$items		= & $this->get( 'Data');
		$pagination =& $this->get('Pagination');
		$subscribers =& $this->get('Subscribers');
		$lists =& $this->get('Lists');
		$summary =& $this->get('Summary');
		//dump($lists, 'Lists');

		$this->assignRef('items',		$items);
		$this->assignRef('pagination', $pagination);
		$this->assignRef('subscribers', $subscribers);
		$this->assignRef('lists', $lists);
		$this->assignRef('summary', $summary);

		parent::display($tpl);
	}
}
