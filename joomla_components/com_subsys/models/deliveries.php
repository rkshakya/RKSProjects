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
  

function __construct()
  {
 	  parent::__construct();
   $db =& JFactory::getDBO();
  	$mainframe = JFactory::getApplication();
	
	  $this->_subcode = JRequest::getVar('sub_code', 0, '', 'int');
	  $this->_pubcode = JRequest::getVar('pub_code', 0, '', 'int');
 
 //dump($this->_subcode, "SUBCODE");

	// Get pagination request variables
	$limit = $mainframe->getUserStateFromRequest('global.list.limit', 'limit', $mainframe->getCfg('list_limit'), 'int');
	$limitstart = JRequest::getVar('limitstart', 0, '', 'int');
	
	// In case limit has been changed, adjust it
	$limitstart = ($limit != 0 ? (floor($limitstart / $limit) * $limit) : 0);
 
	$this->setState('limit', $limit);
	$this->setState('limitstart', $limitstart);
	$lists['subcode']= $this->_subcode; 
	$lists['pubcode']= $this->_pubcode; 
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
        $query = $query . ' ORDER BY delivery_date DESC';
//dump($this->_subcode, "SUB");
//dump($query, "QUERY");
		return $query;
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
		 $querySub = ' SELECT sub_code, sub_name FROM sms_subscribers order by sub_name';			
		 $this->_subscribers = $this->_getList($querySub);
    return $this->_subscribers;
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
