<?php
include './xmlrpc.inc';

/*
 *openerp.php -collection of basic operations on openERP using PHP
 *dependency - xmlrpc.inc
 *
 *Author - Ravi Kishor Shakya
 */

//openERP info 
$oerpuser = 'xxxx';
$oerppwd = 'xxxxx';
$dbname = 'xxxxxxx';
$server_url = 'xxxxxx';

//libraries to execute various openERP functions
//login to openERP
function connect() {
global $oerpuser, $oerppwd, $dbname, $server_url, $client;
if(isset($_COOKIE["user_id"]) == true) {
	if($_COOKIE["user_id"]>0) {
	return $_COOKIE["user_id"];
	}
}
try{
	$sock = new xmlrpc_client($server_url.'common');
	$msg = new xmlrpcmsg('login');
	$msg->addParam(new xmlrpcval($dbname, "string"));
	$msg->addParam(new xmlrpcval($oerpuser, "string"));
	$msg->addParam(new xmlrpcval($oerppwd, "string"));
	$resp = $sock->send($msg);
	$val = $resp->value();
	$id = $val->scalarval();
	}catch(Exception $ex){
		print "Unable to authenticate to openERP ".$ex->getMessage()."\n";
	}
	setcookie("user_id",$id,time()+3600);
	if($id > 0) {
		return $id;
	}else{
		return -1;
	}
}

function getClient(){
	global $server_url;
	try{
		$client = new xmlrpc_client($server_url."object");
	}catch(Exception $err){
		print "Error initializing RPC client ".$err->getMessage()."\n";
	}
	return $client;
}

//function to create objects
function execute($model, $action, $arr){
global $oerpuser, $oerppwd, $dbname, $server_url, $client;
	$retval = -1;
	if($uid <= 0){
		$uid = connect();
	}	
	if(!isset($client)){
		$client = getClient();
	}
	$client = getClient();
	//print "\nUID: ".$uid."DB: ".$dbname."PWD: ".$oerppwd."MODEL : ".$model."ACTION: ".$action;
	
	try{
		$msg = new xmlrpcmsg('execute');
		$msg->addParam(new xmlrpcval($dbname, "string"));
		$msg->addParam(new xmlrpcval($uid, "int"));
		$msg->addParam(new xmlrpcval($oerppwd, "string"));
		$msg->addParam(new xmlrpcval($model, "string"));
		$msg->addParam(new xmlrpcval($action, "string"));
		$msg->addParam(new xmlrpcval($arr, "struct"));
		$resp = $client->send($msg);
		}catch(Exception $terr){
			print "Error setting up exe message ".$terr->getMessage()."\n";
		}
if ($resp->faultCode())
    echo 'Error here: '.$resp->faultCode();
else
    echo '\n'.$model.'object '.$resp->value()->scalarval().' created !';
	$retval = $resp->value()->scalarval();
	return $retval;
}

//function to do searches on openERP
function search($model,$searcharr) {
     global $oerpuser, $oerppwd, $dbname, $server_url, $client;
     /*$key = array(new xmlrpcval(array(new xmlrpcval($attribute , "string"),new xmlrpcval($operator,"string"),new xmlrpcval($keys,"string")),"array"),);
	*/	

     if($userId <= 0) {
		$uid = connect();
     }
	 if(!isset($client)){
		$client = getClient();
	 }
	try{
     $msg = new xmlrpcmsg('execute');
     $msg->addParam(new xmlrpcval($dbname, "string"));
     $msg->addParam(new xmlrpcval($uid, "int"));
     $msg->addParam(new xmlrpcval($oerppwd, "string"));
     $msg->addParam(new xmlrpcval($model, "string"));
     $msg->addParam(new xmlrpcval("search", "string"));
     $msg->addParam(new xmlrpcval($searcharr, "array"));
     $resp = $client->send($msg);	 
     $val = $resp->value();	
     $ids = $val->scalarval();
	 //print "Response: ".php_xmlrpc_decode($resp);
	 
	 }catch(Exception $merr){
		print "Error setting up search message ".$merr->getMessage()."\n";
	 }
     return $ids;
}
?>
