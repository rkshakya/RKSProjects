<?php
/**
 * Subscriber Controller for Subsys Component
 * 
 * @package    
 * @subpackage Components
 * @link 
 * @license		GNU/GPL
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );


class SubsysControllerSubscriber extends SubsysController
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
		JRequest::setVar( 'view', 'subscriber' );
		JRequest::setVar( 'layout', 'editform'  );
		JRequest::setVar('hidemainmenu', 1);
dump("Jamali", "Tutu");
		parent::display();
	}

	/**
	 * save a record (and redirect to main page)
	 * @return void
	 */
	function save()
	{
		$model = $this->getModel('subscriber');
dump($model, "Model");
dump($post, "POSTAL");
		if ($model->store($post)) {
			$msg = JText::_( 'Subscriber Information Saved!' );
			dump("success", "Update");
		} else {
			$msg = JText::_( 'Error Saving Subscriber Information' );
			dump("fail", "Update");
		}

		// Check the table in so it can be edited.... we are done with it anyway
		$link = 'index.php?option=com_subsys';
		$this->setRedirect($link, $msg);
	}

	/**
	 * remove record(s)
	 * @return void
	 */
	function remove()
	{
		$model = $this->getModel('subscriber');
		if(!$model->delete()) {
			$msg = JText::_( 'Error: One or More Subscribers could not be Deleted' );
		} else {
			$msg = JText::_( 'Subscriber(s) Deleted' );
		}

		$this->setRedirect( 'index.php?option=com_subsys', $msg );
	}

	/**
	 * cancel editing a record
	 * @return void
	 */
	function cancel()
	{
		$msg = JText::_( 'Operation Cancelled' );
		$this->setRedirect( 'index.php?option=com_subsys', $msg );
	}
}
