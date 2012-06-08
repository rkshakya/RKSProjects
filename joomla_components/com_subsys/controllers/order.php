<?php
/**
 *  Controller for Subsys Component
 * 
 * @package    
 * @subpackage Components
 * @link 
 * @license		GNU/GPL
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );


class SubsysControllerOrder extends SubsysController
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
		$tut = 90;
		dump($tut, "Tut");
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
dump("Jamali", "Tutu");
		parent::display();
	}
	
	function display(){
    JRequest::setVar( 'view', 'orders' );
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
dump($model, "Model");
dump($post, "POSTAL");
		if ($model->store($post)) {
			$msg = JText::_( 'Publication Information Saved!' );
			dump("success", "Update");
		} else {
			$msg = JText::_( 'Error Saving Publication Information' );
			dump("fail", "Update");
		}

		// Check the table in so it can be edited.... we are done with it anyway
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
