<?php
/**
 *  Publication Controller for Subsys Component
 * 
 * @package    
 * @subpackage Components
 * @link 
 * @license		GNU/GPL
 * @author Ravi Shakya
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );


class SubsysControllerPublication extends SubsysController
{
	/**
	 * constructor (registers additional tasks to methods)
	 * @return void
	 */
	function __construct()
	{
		parent::__construct();

		// Register Extra tasks
		$this->registerTask( 'add'  , 	'edit' );	
	}

	/**
	 * display the edit form
	 * @return void
	 */
	function edit()
	{
		JRequest::setVar( 'view', 'publication' );
		JRequest::setVar( 'layout', 'editpublication'  );
		JRequest::setVar('hidemainmenu', 1);
		parent::display();
	}
	
	//default display function for all pubs
	function display(){
    JRequest::setVar( 'view', 'publications' );
		  JRequest::setVar( 'layout', 'default'  );
		  parent::display();
	}

	/**
	 * save a record (and redirect to main page)
	 * @return void
	 */
	function save()
	{
		$model = $this->getModel('publication');

		if ($model->store($post)) {
			$msg = JText::_( 'Publication Information Saved!' );
			//dump("success", "Update");
		} else {
			$msg = JText::_( 'Error Saving Publication Information' );
			//dump("fail", "Update");
		}

		$link = 'index.php?option=com_subsys&controller=publication';
		$this->setRedirect($link, $msg);
	}

	/**
	 * remove record(s)
	 * @return void
	 */
	function remove()
	{
		$model = $this->getModel('publication');
		if(!$model->delete()) {
			$msg = JText::_( 'Error: One or More Publications could not be deleted' );
		} else {
			$msg = JText::_( 'Publication(s) Deleted' );
		}

		$this->setRedirect( 'index.php?option=com_subsys&controller=publication', $msg );
	}

	/**
	 * cancel editing a record
	 * @return void
	 */
	function cancel()
	{
		$msg = JText::_( 'Operation Cancelled' );
		$this->setRedirect( 'index.php?option=com_subsys&controller=publication', $msg );
	}
}
