<?php
class Login extends CI_Controller {

	public function __construct()
	{
		parent::__construct();
		error_reporting();
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
Please click this link to activate your account: 
 
http://www.uglyartworld.com/verify?email='.$data['newuser'][0].'&hash='.$data['newuser'][1].' 
');	

  $this->email->send();

		
		//redirect to success view
		  $this->load->view('uaw/success');
		}
		
		
		 
		
}

function checkcaptcha( $string )
    {
     //echo 'User captcha response:'.$string;
        $user_answer = md5(strtolower(trim($string)));
        $answers = $this->session->userdata('captcha_answers');

        if( in_array($user_answer, $answers) )
        {
            return TRUE;
        }
        else
        {
            $this->form_validation->set_message('checkcaptcha', 'Your answer to the challenge was incorrect!');
            return FALSE;
        }
    }

}
