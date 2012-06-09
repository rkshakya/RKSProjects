<?php
/**
 * 
 * 
 * @package    Joomla.Tutorials
 * @subpackage Components
 * @link http://docs.joomla.org/Developing_a_Model-View-Controller_Component_-_Part_4
 * @license		GNU/GPL
 */

// No direct access
defined( '_JEXEC' ) or die( 'Restricted access' );

jimport('joomla.application.component.model');

/**
 * 
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysModelOrder extends JModel
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
  $subcode = JRequest::getVar('sub_code',  0, '', 'int');
		//$orderid = JRequest::getVar('order_id',  0, '', 'array');
		$array = JRequest::getVar('cid',  0, '', 'array');
		//$this->setId((int)$array[0]);
		$this->setId($subcode, (int)$array[0]);
	}

	/**
	 * Method to set the hello identifier
	 *
	 * @access	public
	 * @param	int Hello identifier
	 * @return	void
	 */
	function setId($scode, $oid)
	{
		// Set id and wipe data
		$this->_subcode		= $scode;
		$this->_orderid = $oid;
		dump($scode, "SCODE passed");
		dump($oid, "OID passed");
		$this->_data	= null;	
		$this->_subsdata = null;
	 $this->_subscribers = null;
	 $this->_publications = null;
	}
	
	function &getSubscribers(){
	   if(empty($this->_subscribers)){
              $qrySubscriber = 'SELECT sub_code, sub_name FROM sms_subscribers ORDER BY sub_name';         
			           $this->_subscribers = $this->_getList($qrySubscriber);		
      }
    
   
      if (!$this->_subscribers) {
			  $this->_subscribers = new stdClass();
			  $this->_subscribers->sub_code = 0;
			  $this->_subscribers->sub_name = null;
					
		  }
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
	
	function &getSubscriptions(){
    if(empty($this->_subsdata)){
            $qrySub = 'SELECT * FROM sms_subscriptions WHERE order_id = '.$this->_orderid.' AND sub_code = '.$this->_subcode. ' ORDER BY order_id DESC';
                        dump($qrySub, "QURIL");
            $this->_subsdata = $this->_getList($qrySub);			
    }
    
    if (!$this->_subsdata) {
			$this->_subsdata = new stdClass();
			$this->_subsdata->subscription_id = 0;
			$this->_subsdata->order_id = null;
			$this->_subsdata->subscription_title = null;
			$this->_subsdata->order_code = null;
			$this->_subsdata->sub_code = null;		
			$this->_subsdata->pub_code = null;
			$this->_subsdata->num_issues = null;
			$this->_subsdata->num_copies = null;
			$this->_subsdata->issue_from = null;
			$this->_subsdata->issue_to = null;
			$this->_subsdata->start_date = null;
			$this->_subsdata->exp_date = null;
			$this->_subsdata->cdate = null;
			$this->_subsdata->mdate = null;					
		}
		return $this->_subsdata;
	}

	/**
	 * Method to get a hello
	 * @return object with data
	 */
	function &getData()
	{
		// Load the data
		if (empty( $this->_data )) {
			$query = ' SELECT * FROM sms_orders '.
					'  WHERE order_id = '.$this->_orderid;
			$this->_db->setQuery( $query );
			$this->_data = $this->_db->loadObject();		
		}
		
		if (!$this->_data) {
			$this->_data = new stdClass();
			$this->_data->order_id = 0;
			$this->_data->order_code = null;
			$this->_data->order_date = null;
			$this->_data->order_title = null;
			$this->_data->order_invno = null;		
			$this->_data->order_invamt = null;
			$this->_data->order_paid = null;
			$this->_data->cdate = null;
			$this->_data->mdate = null;					
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
		dump($row, "TABLEDATA");

		$data = JRequest::get( 'post' );
		dump($data, "POSTDATA");

		// Bind the form fields to the hello table
		if (!$row->bind($data)) {
			$this->setError($this->_db->getErrorMsg());
			dump($this->_db->getErrorMsg(), "BINDERROR");
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
				dump($row->getErrorMsg() , "DBERROR");
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
		
		dump($cids, "CIDS");

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
