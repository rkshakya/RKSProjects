<?php
/**
 * 
 * @package    Joomla.Tutorials
 * @subpackage Components
 * @license		GNU/GPL
 * @author Ravi Shakya
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );

jimport( 'joomla.application.component.model' );

/**
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysModelOrders extends JModel
{
	/**
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
  var $_timeperiod = null;
  

function __construct()
  {
 	  parent::__construct();
   $db =& JFactory::getDBO();
  	$mainframe = JFactory::getApplication();
  	global $option;
	
	  $this->_subcode = JRequest::getVar('sub_code', 0, '', 'int');
	  $this->_timeperiod = JRequest::getVar('time_period', 0, '', 'int');
 
 //dump($this->_subcode, "SUBCODE");

	// Get pagination request variables
	$limit = $mainframe->getUserStateFromRequest('global.list.limit', 'limit', $mainframe->getCfg('list_limit'), 'int');
	$limitstart = JRequest::getVar('limitstart', 0, '', 'int');
	
	// Get the user state
    $filter_order = $mainframe->getUserStateFromRequest($option.'filter_order','filter_order', 'order_date');
    $filter_order_Dir = $mainframe->getUserStateFromRequest($option.'filter_order_Dir','filter_order_Dir', 'DESC');
	
	// In case limit has been changed, adjust it
	$limitstart = ($limit != 0 ? (floor($limitstart / $limit) * $limit) : 0);
 
	$this->setState('limit', $limit);
	$this->setState('limitstart', $limitstart);
	
	// Build the list array for use in the layout
   $lists['order'] = $filter_order;
   $lists['order_Dir'] = $filter_order_Dir;
	
	$lists['subcode']= $this->_subcode; 
	$lists['timeperiod']= $this->_timeperiod; 
	$this->_lists = $lists;
  }


	/**
	 * Returns the query
	 * @return string The query to be used to retrieve the rows from the database
	 */
	function _buildQuery()
	{
		$query = ' select O.order_id, O.order_code, O.order_date, O.order_title, O.order_invno, O.order_invamt, O.order_paid, S.num_issues, S.num_copies,'
		 . ' S.pub_code, P.pub_code, P.pub_name, S.subscription_title, S.issue_from, S.issue_to, S.start_date, S.exp_date, O.cdate, O.mdate, SU.sub_name, O.sub_code '
  . ' from sms_orders O, sms_subscriptions S, sms_publications P, sms_subscribers SU'
  . ' where O.order_code = S.order_code'
  . ' and O.order_id = S.order_id'
  . ' and O.sub_code = S.sub_code'
  . ' and P.pub_code = S.pub_code'
  . ' and SU.sub_code = O.sub_code';
  if($this->_subcode){ $query .=  ' AND O.sub_code ='. $this->_subcode; }
  if($this->_timeperiod){ $query .=  ' AND S.exp_date BETWEEN CURDATE() AND DATE_ADD( CURDATE( ) , INTERVAL '. $this->_timeperiod . ' DAY )' ; }
  $query .= $this-> _buildQueryOrderBy();
//print $query;		
//dump($this->_subcode, "SUB");
//dump($query, "QUERY");
		return $query;
	}
	
	function _buildQueryOrderBy()
{
  global $mainframe, $option;
  // Array of allowable order fields
  $orders = array('order_code', 'order_date', 'sub_name', 'order_invno', 'order_invamt', 'pub_name', 'num_copies', 'num_issues', 'start_date', 'exp_date');

  // Get the order field and direction, default order field
  // is 'ordering', default direction is ascending
  $filter_order = $mainframe->getUserStateFromRequest($option.'filter_order', 'filter_order', 'order_date');
  $filter_order_Dir = strtoupper($mainframe->getUserStateFromRequest($option.'filter_order_Dir', 'filter_order_Dir', 'DESC'));

  // Validate the order direction, must be ASC or DESC
  if ($filter_order_Dir != 'ASC' && $filter_order_Dir != 'DESC')
  {
    $filter_order_Dir = 'ASC';
  }

  // If order column is unknown use the default
  if (!in_array($filter_order, $orders))
  {
    $filter_order = 'order_date';
  }
  $orderby = ' ORDER BY '.$filter_order.' '.$filter_order_Dir;
  if ($filter_order != 'order_date')
  {
    $orderby .= ' , order_date ';
  }
  // Return the ORDER BY clause

  return $orderby;
}

	/**
	 * Retrieves the data
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

//dump($this->_data, "RESULTS");
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
