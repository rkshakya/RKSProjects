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


class SubsysControllerDelivery extends SubsysController
{
	/**
	 * constructor (registers additional tasks to methods)
	 * @return void
	 */
	function __construct()
	{
		parent::__construct();

		// Register Extra tasks
		//$this->registerTask( 'add'  , 	'edit' );
		$tut = 90;
		dump($tut, "Tut");
	}

	/**
	 * display the edit form
	 * @return void
	 */
	function edit()
	{
		JRequest::setVar( 'view', 'delivery' );
		JRequest::setVar( 'layout', 'editdelivery'  );
		JRequest::setVar('hidemainmenu', 1);
		parent::display();
	}
	
	function add()
	{
		JRequest::setVar( 'view', 'delivera' );
		JRequest::setVar( 'layout', 'default'  );
		JRequest::setVar('hidemainmenu', 1);
dump("Jamali", "Tutu");
		parent::display();
	}
	
	function display(){
    JRequest::setVar( 'view', 'deliveries' );
		  JRequest::setVar( 'layout', 'default'  );
		  parent::display();
	}

	/**
	 * save a record (and redirect to main page)
	 * @return void
	 */
	function save()
	{
		$model = $this->getModel('delivery');
dump($model, "Model");
dump($post, "POSTAL");
$deliveryid = JRequest::getVar('delivery_id',  0, '', 'int');
if($deliveryid > 0){
		  if ($model->store($post)) {
			  $msg = JText::_( 'Delivery Information Updated!' );
			  dump("success", "Update");
		  } else {
			  $msg = JText::_( 'Error Updating Delivery Information' );
			  dump("fail", "Update");
		  }
		}else{
		  if ($model->storeIns($post)) {
			    $msg = JText::_( 'Delivery Information Added!' );
			    dump("success", "Add");
		    } else {
			    $msg = JText::_( 'Error Adding Delivery Information' );
			    dump("fail", "Add");
		        }
		}

		// Check the table in so it can be edited.... we are done with it anyway
		$link = 'index.php?option=com_subsys&controller=delivery';
		$this->setRedirect($link, $msg);
	}

	/**
	 * remove record(s)
	 * @return void
	 */
	function remove()
	{
		$model = $this->getModel('delivery');
		if(!$model->delete()) {
			$msg = JText::_( 'Error: One or More delivery information could not be deleted' );
		} else {
			$msg = JText::_( 'Delivery information Deleted' );
		}

		$this->setRedirect( 'index.php?option=com_subsys&controller=delivery', $msg );
	}

	/**
	 * cancel editing a record
	 * @return void
	 */
	function cancel()
	{
		$msg = JText::_( 'Operation Cancelled' );
		$this->setRedirect( 'index.php?option=com_subsys&controller=delivery', $msg );
	}
}
