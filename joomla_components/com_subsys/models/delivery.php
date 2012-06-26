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

jimport('joomla.application.component.model');

/**
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysModelDelivery extends JModel
{
	/**
	 * Constructor that retrieves the ID from the request
	 *
	 * @access	public
	 * @return	void
	 */
	function __construct()
	{
		parent::__construct();
//dump(JRequest::getVar('cid'), "CID");
		$array = JRequest::getVar('cid',  0, '', 'array');
		$this->setId((int)$array[0]);
	}

	/**
	 *
	 * @access	public
	 * @param	int Hello identifier
	 * @return	void
	 */
	function setId($id)
	{
		// Set id and wipe data
		$this->_id		= $id;
	//	dump($id, "ID passed");
		$this->_data	= null;	
	}

	/**
	 * @return object with data
	 */
	function &getData()
	{
		// Load the data
		if (empty( $this->_data )) {
			$query = ' SELECT D.*, S.sub_name, P.pub_name FROM sms_deliveries D, sms_subscribers S, sms_publications P '.
			    '  WHERE D.sub_code = S.sub_code AND D.pub_code = P.pub_code '.
					'  AND D.delivery_id = '.$this->_id;
		$this->_db->setQuery( $query );
		$this->_data = $this->_db->loadObject();			
		}
		
		if (!$this->_data) {
			$this->_data = new stdClass();
			$this->_data->delivery_id = 0;
			$this->_data->subscription_id = null;
			$this->_data->delivery_date = null;
			$this->_data->delivery_title = null;
			$this->_data->order_code = null;		
			$this->_data->sub_name = null;
			$this->_data->pub_name = null;
			$this->_data->delivery_issue = null;
			$this->_data->delivery_issuedt = null;
			$this->_data->delivery_copies = null;
			$this->_data->delivered_by = null;
			$this->_data->received_by = null;
		}
	
		return $this->_data;
	}

	/**
	 * Method to store a record
	 *
	 * @access	public
	 * @return	boolean	True on success
	 */
	function store()
	{	
		$row =& $this->getTable();
	//	dump($row, "TABLEDATA");

		$data = JRequest::get( 'post' );
	//	dump($data, "POSTDATA");

		// Bind the form fields to the table
		if (!$row->bind($data)) {
			$this->setError($this->_db->getErrorMsg());
			//dump($this->_db->getErrorMsg(), "BINDERROR");
			return false;
		}

		// Make sure the hello record is valid
		if (!$row->check()) {
			$this->setError($this->_db->getErrorMsg());
			dump($this->_db->getErrorMsg() , "VALIDITYERROR");
			return false;
		}

		// Store the web link table to the database
		if (!$row->store()) {
			$this->setError( $row->getErrorMsg() );
		//		dump($row->getErrorMsg() , "DBERROR");
			return false;
		}

		return true;
	}
	
	function storeIns()
	{	
		
		$data = JRequest::get( 'post' );
		//dump($data, "POSTDATA");
		
		//check if order_code exists
		$db =& JFactory::getDBO();
		//get the subscriptiion_id
		$qrySub = "SELECT subscription_id FROM sms_subscriptions WHERE order_code = ".$data['order_code']." AND pub_code = ".$data['pub_code']. " AND sub_code = ".$data['sub_code'];
		//dump($qrySub, "QRYSUB");
		$db->setQuery($qrySub);
  $subscription_id = $db->loadResult();

  //add del info
    $del = new JObject();
    $del->subscription_id = $subscription_id;
    $del->delivery_date = $data['delivery_date'];
    $del->delivery_title = $data['delivery_title'];
    $del->order_code = $data['order_code'];
    $del->sub_code = $data['sub_code'];
    $del->pub_code = $data['pub_code'];
    $del->delivery_issue = $data['delivery_issue'];
    $del->delivery_issuedt = $data['delivery_issuedt'];
    $del->delivery_copies = $data['delivery_copies'];
    $del->delivered_by = $data['delivered_by'];
    $del->received_by = $data['received_by'];
   
    $ret = $db->insertObject('sms_deliveries', $del, 'delivery_id');
 
    if (!$ret) {
	      $this->setError($db->getErrorMsg());
	      return false;
        }
    
		  return true;
	}

	/**
	 * Method to delete record(s)
	 *
	 * @access	public
	 * @return	boolean	True on success
	 */
	function delete()
	{
		$cids = JRequest::getVar( 'cid', array(0), 'post', 'array' );

		$row =& $this->getTable();
		
	//	dump($cids, "CIDS");

		if (count( $cids )) {
			foreach($cids as $cid) {
				if (!$row->delete( $cid )) {
					$this->setError( $row->getErrorMsg() );
					return false;
				}
			}
		}
		return true;
	}

}
