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
class SubsysModelDeliveries extends JModel
{
	/**
	 * @var array
	 */
	var $_data;
	var $_subscribers;
	var $_publications;
	var $_issues;
	
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
  var $_list = null;
  var $_subcode = null;
  var $_pubcode = null;
  var $_deliveryissue = null;
  var $_timeperiod = null;
  

function __construct()
  {
 	  parent::__construct();
   $db =& JFactory::getDBO();
  	$mainframe = JFactory::getApplication();
  	global $option;
	
	  $this->_subcode = JRequest::getVar('sub_code', 0, '', 'int');
	  $this->_pubcode = JRequest::getVar('pub_code', 0, '', 'int');
	  $this->_deliveryissue = JRequest::getVar('delivery_issue', 'DEFAULT', '', 'string');
	  $this->_timeperiod = JRequest::getVar('time_period', 0, '', 'int');
 
 //dump($this->_subcode, "SUBCODE");

	// Get pagination request variables
	$limit = $mainframe->getUserStateFromRequest('global.list.limit', 'limit', $mainframe->getCfg('list_limit'), 'int');
	$limitstart = JRequest::getVar('limitstart', 0, '', 'int');
	
	// Get the user state
  $filter_order = $mainframe->getUserStateFromRequest($option.'filter_order','filter_order', 'delivery_date');
  $filter_order_Dir = $mainframe->getUserStateFromRequest($option.'filter_order_Dir','filter_order_Dir', 'DESC');
	
	// In case limit has been changed, adjust it
	$limitstart = ($limit != 0 ? (floor($limitstart / $limit) * $limit) : 0);
 
	$this->setState('limit', $limit);
	$this->setState('limitstart', $limitstart);
	
	 // Build the list array for use in the layout
  $lists['order'] = $filter_order;
  $lists['order_Dir'] = $filter_order_Dir;
	
	$lists['subcode']= $this->_subcode; 
	$lists['pubcode']= $this->_pubcode; 
	$lists['deliveryissue']= $this->_deliveryissue; 
	$lists['timeperiod']= $this->_timeperiod; 
	$this->_lists = $lists;
  }


	/**
	 * Returns the query
	 * @return string The query to be used to retrieve the rows from the database
	 */
	function _buildQuery()
	{
		$query = 'SELECT D.*, S.sub_name, P.pub_name FROM  '
        . ' sms_deliveries D, sms_subscribers S, sms_publications P'
        . ' WHERE D.sub_code = S.sub_code AND P.pub_code = D.pub_code';
        if($this->_subcode){ $query = $query. ' AND D.sub_code ='. $this->_subcode; }
        if($this->_pubcode){ $query = $query. ' AND D.pub_code ='. $this->_pubcode;}
        if($this->_deliveryissue && $this->_deliveryissue != 'DEFAULT'){ $query = $query. ' AND D.delivery_issue ='. $this->_db->quote($this->_deliveryissue);}
        if($this->_timeperiod){ $query = $query. ' AND DATE_SUB( CURDATE( ) , INTERVAL '.$this->_timeperiod. ' DAY ) <= delivery_date';}
        $query .= $this-> _buildQueryOrderBy();
//dump($this->_subcode, "SUB");
//print $query;
//dump($query, "QUERY");
		return $query;
	}
	
	function _buildQueryOrderBy()
{
  global $mainframe, $option;
  // Array of allowable order fields
  $orders = array('delivery_date', 'order_code', 'sub_name', 'pub_name', 'delivery_issue', 'delivery_issuedt');

  // Get the order field and direction, default order field
  // is 'ordering', default direction is ascending
  $filter_order = $mainframe->getUserStateFromRequest($option.'filter_order', 'filter_order', 'delivery_date');
  $filter_order_Dir = strtoupper($mainframe->getUserStateFromRequest($option.'filter_order_Dir', 'filter_order_Dir', 'DESC'));

  // Validate the order direction, must be ASC or DESC
  if ($filter_order_Dir != 'ASC' && $filter_order_Dir != 'DESC')
  {
    $filter_order_Dir = 'ASC';
  }

  // If order column is unknown use the default
  if (!in_array($filter_order, $orders))
  {
    $filter_order = 'delivery_date';
  }
  $orderby = ' ORDER BY '.$filter_order.' '.$filter_order_Dir;
  if ($filter_order != 'delivery_date')
  {
    $orderby .= ' , delivery_date ';
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
		}

//dump($this->_data, "RESULTS");
		return $this->_data;
	}
	
	function getLists(){  		
    return $this->_lists;
	}
	
	function getSubscribers(){  
		 $querySub = ' SELECT sub_code, sub_name FROM sms_subscribers ORDER BY sub_name';			
		 $this->_subscribers = $this->_getList($querySub);
   return $this->_subscribers;
	}
	
	function getIssues(){  
		 $queryIssues = ' SELECT distinct delivery_issue FROM sms_deliveries ORDER BY delivery_issue';			
		 $this->_issues = $this->_getList($queryIssues);
   return $this->_issues;
	}
	
	function &getPublications(){
	 if(empty($this->_publications)){
            $qryPublication = 'SELECT pub_code, pub_name FROM sms_publications ORDER BY pub_name';
            $this->_publications = $this->_getList($qryPublication);		
    }
    if (!$this->_publications) {
			$this->_publications = new stdClass();
			$this->_publications->pub_code = 0;
			$this->_publications->pub_name = 0;
					
		}
		return $this->_publications;
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
