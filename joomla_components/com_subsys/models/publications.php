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
class SubsysModelPublications extends JModel
{
	/**
	 * Hellos data array
	 *
	 * @var array
	 */
	var $_data;
	
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
  var $_where = null;
  var $_list = null;
  

function __construct()
  {
 	  parent::__construct();
   $db =& JFactory::getDBO();
  	$mainframe = JFactory::getApplication();
	
	  $search = $mainframe-> getUserStateFromRequest( $option.'search','search','','string' );
	    $origSearch = $search;
   $search = JString::strtolower( $search );
 
 dump($search, "SEARCH");
 
	// Get pagination request variables
	$limit = $mainframe->getUserStateFromRequest('global.list.limit', 'limit', $mainframe->getCfg('list_limit'), 'int');
	$limitstart = JRequest::getVar('limitstart', 0, '', 'int');
	$where = array();
    if ( $search ) {
        $where[] = 'pub.pub_name LIKE "%'.$db->getEscaped($search).'%"';
    }   
    $where      = ( count( $where ) ? ' WHERE ' . implode( ' AND ', $where ) : '' );
 
	// In case limit has been changed, adjust it
	$limitstart = ($limit != 0 ? (floor($limitstart / $limit) * $limit) : 0);
 
	$this->setState('limit', $limit);
	$this->setState('limitstart', $limitstart);
	$lists['search']= $origSearch; 
	$this->_where = $where; 
	$this->_lists = $lists;
  }


	/**
	 * Returns the query
	 * @return string The query to be used to retrieve the rows from the database
	 */
	function _buildQuery()
	{
		$query = ' SELECT pub.*, cat.cat_name '
			. ' FROM sms_publications pub LEFT JOIN sms_categories cat ON pub.pub_category = cat.cat_code '	
			. $this->_where
		;
dump($this->_where, "WHERE");
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
		}

		return $this->_data;
	}
	
	function getLists(){   
    return $this->_lists;
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
