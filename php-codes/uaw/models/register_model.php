<?php
class Register_model extends CI_Model {

	public function __construct()
	{
		$this->load->database();
	}
	
	public function insert_user(){	    
    //generate random hash
    $hash = md5( rand(0,1000) );
    $userdata = array(
		        'email' => $this->input->post('email'),
		        'pwd' => $this->input->post('password'),
		        'fname' => $this->input->post('fname'),
		        'lname' => $this->input->post('lname'),
		        'typeid' => $this->input->post('artist'),
		        'activist' => $this->input->post('activist'),
		        'ahash' => $hash
	                );
	       $causeworking = $this->input->post('causeworking');   
	       $causeobj = array();      
    //insert user - do in transaction
    $this->db->trans_start();
    $this->db->insert('m_user', $userdata);         
    
    //get last insert id    
    $userid = $this->db->insert_id();
    
    //insert into usercause mapping
    foreach ($causeworking as $cause_working):
              $ce = array('userid' => $userid, 'causeid' => $cause_working);
              array_push($causeobj, $ce);
    endforeach;
    $this->db->insert_batch('m_usercause_mapping', $causeobj); 
    $this->db->trans_complete();
        // return email and hash
    return array($this->input->post('email'), $hash,  $this->input->post('fname'));
	}
	
	
	  public function get_types(){
  $this->db->select('id, description');
  $query = $this->db->get('l_type');
  return $query->result_array();
  }
  
   public function get_causes(){
  $this->db->select('id, status');
  $query = $this->db->get('l_cause');
  return $query->result_array();
  }
}
