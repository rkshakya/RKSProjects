<?php
/**
 * Hello Model for Hello World Component
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
 * Hello Hello Model
 *
 * @package    Joomla.Tutorials
 * @subpackage Components
 */
class SubsysModelPublication extends JModel
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
dump(JRequest::getVar('cid'), "CID");
		$array = JRequest::getVar('cid',  0, '', 'array');
		$this->setId((int)$array[0]);
	}

	/**
	 * Method to set the hello identifier
	 *
	 * @access	public
	 * @param	int Hello identifier
	 * @return	void
	 */
	function setId($id)
	{
		// Set id and wipe data
		$this->_id		= $id;
		dump($id, "ID passed");
		$this->_data	= null;
		$this->_miscdata = null;
		$this->_alldata = null;  //holds array of publications objects as first element, array of category elements as second
	}

	/**
	 * Method to get a hello
	 * @return object with data
	 */
	function &getData()
	{
		// Load the data
		if (empty( $this->_data )) {
			$query = ' SELECT * FROM sms_publications '.
					'  WHERE pub_id = '.$this->_id;
			$this->_db->setQuery( $query );
			$this->_data = $this->_db->loadObject();
			$this->_alldata[0] = $this->_data;			
		}
		if(empty($this->_miscdata)){
	  //now get values of cat_code and cat_name, needed to populate dropdown
			$queryCat = 'SELECT cat_code, cat_name FROM sms_categories ORDER BY cat_code';
			$this->_db->setQuery( $queryCat );
			$this->_miscdata = $this->_db->loadObjectList( );
			$this->_alldata[1] = $this->_miscdata;		
		}
		if (!$this->_data) {
			$this->_data = new stdClass();
			$this->_data->pub_id = 0;
			$this->_data->pub_code = null;
			$this->_data->pub_name = null;
			$this->_data->pub_principal = null;
			$this->_data->pub_category = null;		
			$this->_data->pub_frequency = null;
			$this->_data->pub_numissues = null;
			$this->_data->pub_currency = null;
			$this->_data->pub_rate1 = null;
			$this->_data->pub_rate2 = null;
			$this->_data->pub_rate3 = null;
			$this->_data->pub_rate4 = null;
			$this->_data->pub_cdate = null;
			$this->_data->pub_mdate = null;					
		}
		if(!$this->_miscdata){
		 $this->_miscdata = new stdClass();
			$this->_miscdata->cat_code = null;
			$this->_miscdata->cat_name = null;
		}
		if(!$this->_alldata){
		  $this->_alldata = null;
		}
		return $this->_alldata;
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
