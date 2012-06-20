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
class SubsysViewDeliveries extends JView
{
	/**
	 * @return void
	 **/
	function display($tpl = null)
	{
		JToolBarHelper::title(   JText::_( 'Deliveries Information' ), 'generic.png' );
		JToolBarHelper::deleteList();
		JToolBarHelper::editListX();
		JToolBarHelper::addNewX();
		SubMenuHelper::addSubmenu();		

		// Get data from the model
		$subscribers =& $this->get( 'Subscribers');
		$publications =& $this->get( 'Publications');
		$items		=& $this->get( 'Data');
		$pagination =& $this->get('Pagination');
		$lists =& $this->get('Lists');
		//dump($lists, 'Lists');

		$this->assignRef('items',		$items);
		$this->assignRef('pagination', $pagination);
		$this->assignRef('lists', $lists);
		$this->assignRef('subscribers', $subscribers);
		$this->assignRef('publications', $publications);

		parent::display($tpl);
	}
}
