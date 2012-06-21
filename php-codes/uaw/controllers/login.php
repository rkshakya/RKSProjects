<?php
session_start();
class Login extends CI_Controller {

	public function __construct()
	{
		parent::__construct();
		error_reporting();
	}
	
		public function index()
{
	$this->load->helper(array('form', 'url'));
	
	$data['title'] = 'Login';
		
		$this->load->view('templates/uawheader', $data);	
		$this->load->view('uaw/login');
		$this->load->view('templates/uawfooter');	
}

public function verifylogin(){
      $this->load->model('register_model');
      $this->load->helper(array('form', 'url'));
	     $this->load->library(array('form_validation', 'session'));
	     
	     $this->form_validation->set_rules('email', 'Email', 'trim|required|xss_clean|valid_email');
      $this->form_validation->set_rules('password', 'Password', 'trim|required|xss_clean|md5|callback_check_database');
 
    if($this->form_validation->run($this) == FALSE){
     //Field validation failed. User redirected to login page
     $this->load->view('uaw/login');
   }else{
     //Go to private area
     redirect('home', 'refresh');
   }
      
}

public function home(){
  $this->load->helper(array('form', 'url'));
  $this->load->library('session');
 if($this->session->userdata('logged_in')){
     $session_data = $this->session->userdata('logged_in');
     $data['username'] = $session_data['fname'].' '.$session_data['lname'];
     $this->load->view('uaw/home', $data);
   } else   {
     //If no session, redirect to login page
     redirect('uaw/login', 'refresh');
   }
}

function logout(){
  $this->load->helper(array('form', 'url'));
   $this->load->library('session');
   $this->session->unset_userdata('logged_in');
   session_destroy();
   redirect('uaw/login', 'refresh');
 }

function check_database($password) {
   //Field validation succeeded.Validate against database
   $email = $this->input->post('email');

   //query the database
   $result = $this->register_model->login($email, $password);

   if($result) {
     $sess_array = array();
     foreach($result as $row){
       $sess_array = array(
         'userid' => $row->userid,
         'fname' => $row->fname,
         'lname' => $row->lname
       );
       $this->session->set_userdata('logged_in', $sess_array);
     }
     return TRUE;
   }else{
     $this->form_validation->set_message('check_database', 'Invalid username or password');
     return FALSE;
   }
 }

//function to verify and activate the newly created accounts
public function verify(){
    $this->load->model('register_model');
    //get the GET params
      $email = $this->input->get('email', TRUE);
      $hash = $this->input->get('hash', TRUE);
      if($email and $hash){
                  //chk if the entry exists in DB
                  $countuser = $this->register_model->count_user();
                 
                  if($countuser > 0){
                   //if yes update 
                                $this->register_model->update_user();
                                $msg = 'Your account has been activated. Please log-in below.';
                  }else{
                  //if no, mesg
                                $msg = 'Either your account is already active or the URL you entered for verification is invalid!';
                  }
      }else{
      $msg = 'Invalid way! Please use the link sent in the mail to activate account.';
      }
      $data['mailmesg'] = $msg;
      $this->load->view('uaw/login', $data);
}

public function register(){
    //to populate drop downs
  $this->load->model('register_model');
  $data['types'] = $this->register_model->get_types();
  $data['causes'] = $this->register_model->get_causes();
  
    //load helpers and libs
  $this->load->helper(array('form', 'url'));
	 $this->load->library(array('form_validation', 'session'));
	 
	 $data['title'] = 'Register';
	 
	    //form validation rules 
	 	$this->form_validation->set_rules('email', 'email', 'trim|required|valid_email');
	  $this->form_validation->set_rules('password', 'password', 'trim|required|min_length[6]|matches[repassword]|md5');
	  $this->form_validation->set_rules('repassword', 'retyped password', 'trim|required');
	  $this->form_validation->set_rules('fname', 'first name', 'trim|required|alpha');
	  $this->form_validation->set_rules('lname', 'last name', 'trim|required|alpha');
	  $this->form_validation->set_rules('artist', 'artist', 'required');
	  $this->form_validation->set_rules('activist', 'activist', 'required');
	  $this->form_validation->set_rules('captcha', 'captcha', 'required|callback_checkcaptcha');
	 	  	  
	if ($this->form_validation->run($this) === FALSE){
	
	      // setup textCAPTCHA
        try {
            $xml = @new SimpleXMLElement('http://textcaptcha.com/api/demo', NULL, TRUE);
        } catch ( Exception $e ) {
            $fallback  = '<captcha>';
            $fallback .= '<question>Is fire hot or cold?</question>';
            $fallback .= '<answer>'.md5('hot').'<answer>';
            $fallback .= '</captcha>';
            $xml = new SimpleXMLElement($fallback);
                }

        // store answers in session for use later
        $answers = array();
        foreach( $xml->answer as $hash ){
            $answers[] = (string)$hash;
                }
        $this->session->set_userdata('captcha_answers', $answers);

        // load vars into view
        $this->load->vars(array( 'captcha' => (string)$xml->question ));
        $this->load->view('templates/uawheader', $data);	
		      $this->load->view('uaw/register', $data);
		      $this->load->view('templates/uawfooter');
		      
		     
		} else {
		//make entry into DB
		$data['newuser'] = $this->register_model->insert_user();
		//send reg confirm mail
		$config['protocol'] = 'smtp'; 

  $this->load->library('email', $config);
  $this->email->set_newline("\r\n");
  
		$this->email->from('admin@uglyartworld.com');
  $this->email->to($data['newuser'][0]); 
  $this->email->subject('UglyArtWorld.com Signup verification');
  $this->email->message('
Hi '. $data['newuser'][2].'
Thanks for registering at UglyArtWorld.com! 
Your account has been created. 
Please click this link to activate your account(If clicking does not work, plz copy the link, paste it in browser address bar and hit enter): 
 
'.site_url().'/verify?email='.$data['newuser'][0].'&hash='.$data['newuser'][1].' 
');	

  $this->email->send();

		
		//redirect to login view with appr message
		$data['mailmesg'] = 'A confirmation cum activation email has been sent to your registered email address. Please activate the account and then log-in below. Thanks';
		  $this->load->view('uaw/login', $data);
		}		
}

function checkcaptcha( $string ) {
     //echo 'User captcha response:'.$string;
        $user_answer = md5(strtolower(trim($string)));
        $answers = $this->session->userdata('captcha_answers');

        if( in_array($user_answer, $answers) ){
            return TRUE;
        }else{
            $this->form_validation->set_message('checkcaptcha', 'Your answer to the challenge was incorrect!');
            return FALSE;
        }
    }

}
