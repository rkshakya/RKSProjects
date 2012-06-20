<?php
/**
 *  Category Controller for Subsys Component
 * 
 * @package    
 * @subpackage Components
 * @link 
 * @license		GNU/GPL
 * @author Ravi Shakya
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );


class SubsysControllerCategory extends SubsysController
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
		JRequest::setVar( 'view', 'category' );
		JRequest::setVar( 'layout', 'editcategory'  );
		JRequest::setVar('hidemainmenu', 1);
		parent::display();
	}
	
	function display(){
    JRequest::setVar( 'view', 'categories' );
		  JRequest::setVar( 'layout', 'default'  );
		  parent::display();
	}

	/**
	 * save a record (and redirect to main page)
	 * @return void
	 */
	function save()
	{
		$model = $this->getModel('category');

		if ($model->store($post)) {
			$msg = JText::_( 'Category Information Saved!' );
//			dump("success", "Update");
		} else {
			$msg = JText::_( 'Error Saving Category Information' );
//			dump("fail", "Update");
		}

		$link = 'index.php?option=com_subsys&controller=category';
		$this->setRedirect($link, $msg);
	}

	/**
	 * remove record(s)
	 * @return void
	 */
	function remove()
	{
		$model = $this->getModel('category');
		if(!$model->delete()) {
			$msg = JText::_( 'Error: One or More Categories could not be deleted' );
		} else {
			$msg = JText::_( 'Category(s) Deleted' );
		}

		$this->setRedirect( 'index.php?option=com_subsys&controller=category', $msg );
	}

	/**
	 * cancel editing a record
	 * @return void
	 */
	function cancel()
	{
		$msg = JText::_( 'Operation Cancelled' );
		$this->setRedirect( 'index.php?option=com_subsys&controller=category', $msg );
	}
}
