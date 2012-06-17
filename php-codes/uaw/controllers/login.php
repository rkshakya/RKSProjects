<?php
class Login extends CI_Controller {

	public function __construct()
	{
		parent::__construct();
	}
	
		public function index()
{
	$this->load->helper(array('form', 'url'));
	$this->load->library('form_validation');
	
	$data['title'] = 'Login';
	
	$this->form_validation->set_rules('username', 'Username', 'required');
	$this->form_validation->set_rules('password', 'password', 'required');
	
	if ($this->form_validation->run() === FALSE)
	{
		$this->load->view('templates/uawheader', $data);	
		$this->load->view('uaw/login');
		$this->load->view('templates/uawfooter');
		
	}
	else
	{
		//$this->news_model->set_news();
		$this->load->view('uaw/success');
	}
}

public function register(){
  $this->load->helper(array('form', 'url'));
	 $this->load->library('form_validation');
	 $data['title'] = 'Register';
	 	$this->form_validation->set_rules('email', 'email', 'trim|required|valid_email');
	  $this->form_validation->set_rules('password', 'password', 'trim|required|min_length[6]|matches[repassword]|md5');
	  $this->form_validation->set_rules('repassword', 'retyped password', 'trim|required');
	  $this->form_validation->set_rules('fname', 'first name', 'trim|required|alpha');
	  $this->form_validation->set_rules('lname', 'last name', 'trim|required|alpha');
	  $this->form_validation->set_rules('artist', 'artist', 'required');
	  $this->form_validation->set_rules('activist', 'activist', 'required');
	  
	if ($this->form_validation->run() === FALSE){
  $this->load->view('templates/uawheader', $data);	
		$this->load->view('uaw/register');
		$this->load->view('templates/uawfooter');
		} else{
		  $this->load->view('uaw/success');
		}
}

}
