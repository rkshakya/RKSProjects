<?php
/**
 * Hellos Model for Hello World Component
 * 
 * @package    Joomla.Tutorials
 * @subpackage Components
 * @link http://docs.joomla.org/Developing_a_Model-View-Controller_Component_-_Part_4
 * @license		GNU/GPL
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );

jimport( 'joomla.application.component.model' );

/**
 * Hello Model
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysModelOrders extends JModel
{
	/**
	 * Hellos data array
	 *
	 * @var array
	 */
	var $_data;
	var $_subscribers;
	
	/**
   * Items total
   * @var integer
   */
  var $_total = null;
 
  /**
   * Pagination object
   * @var object
   */
  var $_pagination = null;
  var $_count = null;
  var $_list = null;
  var $_subcode = null;
  

function __construct()
  {
 	  parent::__construct();
   $db =& JFactory::getDBO();
  	$mainframe = JFactory::getApplication();
	
	 $this->_subcode = JRequest::getVar('sub_code', 0, '', 'int');
 
 dump($this->_subcode, "SUBCODE");

	// Get pagination request variables
	$limit = $mainframe->getUserStateFromRequest('global.list.limit', 'limit', $mainframe->getCfg('list_limit'), 'int');
	$limitstart = JRequest::getVar('limitstart', 0, '', 'int');
	
	// In case limit has been changed, adjust it
	$limitstart = ($limit != 0 ? (floor($limitstart / $limit) * $limit) : 0);
 
	$this->setState('limit', $limit);
	$this->setState('limitstart', $limitstart);
	$lists['subcode']= $this->_subcode; 
	$this->_lists = $lists;
  }


	/**
	 * Returns the query
	 * @return string The query to be used to retrieve the rows from the database
	 */
	function _buildQuery()
	{
		$query = ' select O.order_id, O.order_code, O.order_date, O.order_title, O.order_invno, O.order_invamt, O.order_paid, S.num_issues, S.num_copies,'
		 . ' S.pub_code, P.pub_code, P.pub_name, S.subscription_title, S.issue_from, S.issue_to, S.start_date, S.exp_date, O.cdate, O.mdate '
  . ' from sms_orders O, sms_subscriptions S, sms_publications P'
  . ' where O.order_code = S.order_code'
  . ' and O.order_id = S.order_id'
  . ' and O.sub_code = S.sub_code'
  . ' and P.pub_code = S.pub_code'
  . ' and O.sub_code = '
    . $this->_subcode
    . ' ORDER BY O.order_code '
		;
dump($this->_subcode, "SUB");
dump($query, "QUERY");
		return $query;
	}

	/**
	 * Retrieves the hello data
	 * @return array Array of objects containing the data from the database
	 */
	function getData()
	{
		// Lets load the data if it doesn't already exist
		if (empty( $this->_data ))
		{
			$query = $this->_buildQuery();	
			$this->_data = $this->_getList( $query, $this->getState('limitstart'), $this->getState('limit') );
			$this->_lists['subcount'] = $this->_getListCount($query);
		}

dump($this->_data, "RESULTS");
		return $this->_data;
	}
	
	function getLists(){  		
    return $this->_lists;
	}
	
	function getSubscribers(){  
		 $querySub = ' SELECT sub_code, sub_name FROM sms_subscribers order by sub_name';			
		 $this->_subscribers = $this->_getList($querySub);
    return $this->_subscribers;
	}
	
	
	function getSummary(){ 
	  $querySummary = ' SELECT distinct order_code FROM sms_orders WHERE sub_code = '. $this->_subcode;			
		 $this->_count = $this->_getListCount($querySummary);
    return $this->_count;      
	}
	
	function getTotal()
  {
 	// Load the content if it doesn't already exist
 	if (empty($this->_total)) {
 	    $query = $this->_buildQuery();
 	    $this->_total = $this->_getListCount($query);	
 	}
 	return $this->_total;
  }

function getPagination()
  {
 	// Load the content if it doesn't already exist
 	if (empty($this->_pagination)) {
 	    jimport('joomla.html.pagination');
 	    $this->_pagination = new JPagination($this->getTotal(), $this->getState('limitstart'), $this->getState('limit') );
 	}
 	return $this->_pagination;
  }
}
