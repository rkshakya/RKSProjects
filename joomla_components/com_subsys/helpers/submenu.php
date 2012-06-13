<?php
// No direct access to this file
defined('_JEXEC') or die;
 

abstract class SubMenuHelper
{
	/**
	 * Configure the Linkbar.
	 */
	public static function addSubmenu() 
	{
		JSubMenuHelper::addEntry('Subscribers', 'index.php?option=com_subsys',true);
		JSubMenuHelper::addEntry('Publications', 'index.php?option=com_subsys&controller=publication',true);
		JSubMenuHelper::addEntry('Categories', 'index.php?option=com_subsys&controller=category',true);
		JSubMenuHelper::addEntry('Orders', 'index.php?option=com_subsys&controller=order',true);
		JSubMenuHelper::addEntry('Deliveries', 'index.php?option=com_subsys&controller=delivery',true);
	}
	
}

