<?php
/**
 * 
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
		$array = JRequest::getVar('cid',  0, '', 'array');
		$this->setId($subcode, (int)$array[0]);
	}

	/**
	 *
	 * @access	public
	 * @param	int identifier
	 * @return	void
	 */
	function setId($scode, $oid)
	{
		// Set id and wipe data
		$this->_subcode		= $scode;
		$this->_orderid = $oid;
	//	dump($scode, "SCODE passed");
	//	dump($oid, "OID passed");
		$this->_data	= null;	
		$this->_subsdata = null;
	 $this->_subscribers = null;
	 $this->_publications = null;
	}
	
	function &getSubscribers(){
	   if(empty($this->_subscribers)){
              $qrySubscriber = 'SELECT sub_code, sub_name FROM sms_subscribers WHERE sub_code = ' . $this->_subcode;         
			           $this->_db->setQuery( $qrySubscriber );
			           $this->_subscribers = $this->_db->loadObject();			
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
          //dump($qrySub, "QURIL");
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
	 * Method to get records
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
		
		$data = JRequest::get( 'post' );
		//dump($data, "POSTDATA");
		//dump($data["order_id"], "ULLUDATA");

  //update order information, this will be same all the time
  $dbnew =& JFactory::getDBO();
  $ord = new JObject();
  $ord->order_id = $data['order_id'];
  $ord->order_code = $data['order_code'];
  $ord->order_date = $data['order_date'];
  $ord->order_title = $data['order_title'];
  $ord->sub_code = $data['sub_code'];
  $ord->order_invno = $data['order_invno'];
  $ord->order_invamt = $data['order_invamt'];
  $ord->order_paid = $data['order_paid'];

  //Update the record. Third parameter is table id field that will be used to update.
  $ret = $dbnew->updateObject('sms_orders', $ord,'order_id');
  
  //update order_code in sms_subscritions table
  $qryUdt = "UPDATE sms_subscriptions SET order_code = '".$data['order_code']."' WHERE order_id = ".$data['order_id'];
  $dbnew->setQuery($qryUdt);
  $retUdt = $dbnew->query();
    
    //now deal with all valid Subscription parts
    for($i = 0; $i < $data['countsubscription']; $i++){
      if($data['action'.$i] == 'Update'){
          $sub = new JObject();
          $sub->subscription_id = $data['subscription_id'.$i];
          $sub->order_id = $data['order_id'];
          $sub->subscription_title = $data['subscription_title'.$i];
          $sub->order_code = $data['order_code'];
          $sub->sub_code = $data['sub_code'];
          $sub->pub_code = $data['pub_code'.$i];
          $sub->num_issues = $data['num_issues'.$i];
          $sub->num_copies = $data['num_copies'.$i];
          $sub->issue_from = $data['issue_from'.$i];
          $sub->issue_to = $data['issue_to'.$i];
          $sub->start_date = $data['start_date'.$i];
          $sub->exp_date = $data['exp_date'.$i];
                   
          $ret1 = $dbnew->updateObject('sms_subscriptions', $sub,'subscription_id'); 
                    
        } else if($data['action'.$i] == 'Delete'){
                    $qry = "DELETE FROM sms_subscriptions WHERE subscription_id = ".$data['subscription_id'.$i];
                    $dbnew->setQuery($qry);
                    $ret2 = $dbnew->query();
        }
    }
    if($ret and $retUdt){
		  return true;
		  }
	}
	
	function storeIns()
	{	
		
		$data = JRequest::get( 'post' );
		//dump($data, "POSTDATA");
		
		//check if order_code exists
		$db =& JFactory::getDBO();
		$qryCount = "SELECT order_id FROM sms_orders WHERE order_code = ".$data['order_code'];
	//	dump($qryCount, "QRYCOUNT");
		$db->setQuery($qryCount);
  $order_id = $db->loadResult();
 // dump($orderid, "ORDERID");
  if (!$order_id){
  //add order info
    $ord = new JObject();
    $ord->order_code = $data['order_code'];
    $ord->order_date = $data['order_date'];
    $ord->order_title = $data['order_title'];
    $ord->sub_code = $data['sub_code'];
    $ord->order_invno = $data['order_invno'];
    $ord->order_invamt = $data['order_invamt'];
    $ord->order_paid = $data['order_paid'];
  // dump("here", "HERER");
   
    $ret = $db->insertObject('sms_orders', $ord, 'order_id');
 
    if (!$ret) {
	      $this->setError($db->getErrorMsg());
	      return false;
        }
 
    //Get the new record id
    $order_id = (int)$db->insertid();
    // dump($order_id, "ORDER ID");
    }
    
   // dump($order_id, "ORDER IIIID");
 //add subscriptions with this order_id
    for($i = 0; $i < 2; $i++){
    //dump($order_id, "ORDER CRAP");
        // dump($data['pub_code'.$i], "ullu");
      if($data['pub_code'.$i] != ""){
          $sub = new JObject();         
          $sub->order_id = $order_id;
          $sub->subscription_title = $data['subscription_title'.$i];
          $sub->order_code = $data['order_code'];
          $sub->sub_code = $data['sub_code'];
          $sub->pub_code = $data['pub_code'.$i];
          $sub->num_issues = $data['num_issues'.$i];
          $sub->num_copies = $data['num_copies'.$i];
          $sub->issue_from = $data['issue_from'.$i];
          $sub->issue_to = $data['issue_to'.$i];
          $sub->start_date = $data['start_date'.$i];
          $sub->exp_date = $data['exp_date'.$i];
          //dump($order_id, "ORDER BITRA ID");
                   
          $ret1 = $db->insertObject('sms_subscriptions', $sub,'subscription_id'); 
          if (!$ret1) {
	             $this->setError($db->getErrorMsg());
	              return false;
                    }                    
        } 
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

		//$row =& $this->getTable();
		
		//dump(implode(",", $cids), "CIDS");
    $qryDelete = "DELETE FROM sms_orders WHERE order_id IN (".implode(",", $cids).")";
    $db =& JFactory::getDBO();
    $db->setQuery($qryDelete);
    $result = $db->query();
    if (!$result) {
	             $this->setError($db->getErrorMsg());
	              return false;
                    }        
		
		return true;
	}

}
