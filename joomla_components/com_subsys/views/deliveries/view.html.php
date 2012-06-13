<?php
/**
 * Hellos View for Hello World Component
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
 * Hellos View
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysViewDeliveries extends JView
{
	/**
	 * Hellos view display method
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
		dump($lists, 'Lists');

		$this->assignRef('items',		$items);
		$this->assignRef('pagination', $pagination);
		$this->assignRef('lists', $lists);
		$this->assignRef('subscribers', $subscribers);
		$this->assignRef('publications', $publications);

		parent::display($tpl);
	}
}
