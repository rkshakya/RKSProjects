<?php
/**
 * 
 * 
 * @package    Joomla.Tutorials
 * @subpackage Components
 * @link 
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
class SubsysModelAddOrder extends JModel
{
	/**
	 * Constructor that retrieves the Sub code from the request
	 *
	 * @access	public
	 * @return	void
	 */
	function __construct()
	{
		parent::__construct();		
  $subcode = JRequest::getVar('sub_code',  0, '', 'int');		
		$this->setId($subcode);
	}

	/**
	 * Method to set the sub code
	 *
	 * @access	public
	 * @param	int Sub code
	 * @return	void
	 */
	function setId($scode)
	{
		// Set sub code and wipe data
		$this->_subcode		= $scode;
		//dump($scode, "SCODE passed in Addorder");
	 $this->_subscribers = null;
	 $this->_publications = null;
	}
	
	//get subscribers info for dropdowns
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
	
	//get publications info for dropdown
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
		
		//check if order_code exists
		$db =& JFactory::getDBO();
		$qryCount = "SELECT order_id FROM sms_orders WHERE order_code = ".$data['order_code'];
		$db->setQuery($qryCount);
  $order_id = $db->loadResult();
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
   
    $ret = $db->insertObject('sms_orders', $ord, 'order_id');
 
    if (!$ret) {
	      $this->setError($db->getErrorMsg());
	      return false;
        }
 
    //Get the new record id
    $order_id = (int)$db->insertid();
  }
 //add subscriptions with this order_id
    for($i = 0; $i < 2; $i++){
      if(len($data['pub_code'.$i]) > 0){
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
                   
          $ret1 = $db->insertObject('sms_subscriptions', $sub,'subscription_id'); 
          if (!$ret1) {
	             $this->setError($db->getErrorMsg());
	              return false;
                    }                    
        } 
    }
    
		  return true;
	}

}
