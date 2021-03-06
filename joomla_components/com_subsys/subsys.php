<?php
/**
* Subsys Main(default) Controller
 * @package    
 * @subpackage Components
 * @link 
 * @license    GNU/GPL
 *@author Ravi Shakya
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );

// Require the base controller
require_once( JPATH_COMPONENT.DS.'controller.php' );
//class for creating sub menus in each pages
JLoader::register('SubMenuHelper', dirname(__FILE__) . DS . 'helpers' . DS . 'submenu.php');

// Require specific controller if requested

if($controller = JRequest::getWord('controller')) {
	$path = JPATH_COMPONENT.DS.'controllers'.DS.$controller.'.php';
	//dump($path, 'CtrlPath');
	if (file_exists($path)) {
		require_once $path;
	} else {
		$controller = '';
	}
}

// Create the controller
$classname	= 'SubsysController'.$controller;

$controller	= new $classname( );

//dump($classname, 'CtrlClass');
//dump(JRequest::getVar( 'task' ), 'Task');

// Perform the Request task
$controller->execute( JRequest::getVar( 'task' ) );

// Redirect if set by the controller
$controller->redirect();
