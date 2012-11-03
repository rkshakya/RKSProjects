<?php
chdir(dirname(__FILE__));
include "./config.php";
require_once "./lib/nusoap.php";
require_once 'HTTP/Client.php';
require_once 'Zend/Json.php';

/*
 *soapsync.php - a CLI script that reads records from  SOAP interface and populates into Contacts of vtiger
 *configurations should be made in config.php file
 *
 *Requirements: a) nuSOAP library, b) PEAR:HTTP_Client lib, c) Zend: JSON lib, d) properly configured config.php file
 *
 *Usage: php soapsync.php
 *
 *Outputs: 1) records from source will be populated in destination(vtiger), 2) two logs(data dump + operation log) will be generated in the same folder as script for each run
 *
 *Author : Ravi K Shakya
 *
 */

//open log and data dump files
$logFile = "info_".date('Y_m_d_H_i').".log";
$dataFile = "data_".date('Y_m_d_H_i').".txt";
$lg = fopen($logFile, 'w') or die("Can't open log file");
$dat = fopen($dataFile, 'w') or die("Can't open data dump file");

//initialize soap client
try{
    $client = new nusoap_client($srcURL, true);
} catch (Exception $err) {
    fwrite($lg, "Exception initializing SOAP client - Termination ".$err->getMessage()."\n");
    //this is severe, so cleanup and quit
    fclose($dat);
    fclose($lg);
    exit;
}

//connect to destination vtiger web service
//get challenge token - then login using accesskey and challenge token to get session id

//get challenge token
try{
    $httpc = new HTTP_Client();
    $httpc->get("$destURL?operation=getchallenge&username=$destUser");
    $response = $httpc->currentResponse();
    $jsonResponse = Zend_JSON::decode($response['body']);

    if($jsonResponse['success']==false) {
        fwrite($lg, "Error getchallenge for vtiger ".$jsonResponse['error']['errorMsg']."\n");        
    }
    $challengeToken = $jsonResponse['result']['token'];
}catch(Exception $derr){
    fwrite($lg, "Exception initializing HTTP client for vtiger ".$derr->getMessage()."\n");
}

//now login using challenge token and accesskey and get session id
$generatedKey = md5($challengeToken.$destAccessKey);
try{
    $httpc->post("$destURL", array('operation'=>'login', 'username'=>$destUser, 'accessKey'=>$generatedKey), true);
    $response = $httpc->currentResponse();
    $jsonResponse = Zend_JSON::decode($response['body']);
    
    if($jsonResponse['success']==false){
       fwrite($lg, "Error vtiger login failed ".$jsonResponse['error']['errorMsg']."\n"); 
    }
    
    //login successful extract sessionId and userId from LoginResult to it can used for further calls.
    $sessionId = $jsonResponse['result']['sessionName']; 
    $userId = $jsonResponse['result']['userId'];
}catch(Exception $lerr){
    fwrite($lg, "Exception login vtiger ".$lerr->getMessage()."\n");
}

//initialise flags, counters and exception array
$flag = true; //to control iteration
$srccnt = 0; //counter to keep track of records read from source
$destcnt = 0;
$except_array = array();  //holds rec numbers of records that could not be populated into destination
$counter = 1;

while($flag == true){
    //read from source queue
    $result = $client->call('LookupQueue', array('UserName' => $srcUser, 'Password' => $srcPwd, 'EventRef' => $srcEvent, 'QueueIdentifier' => $srcQueueIdentifier, 'Quantity' => $srcBatch));
    if ($client->fault) {
        fwrite($lg, "Error Fault lookupqueue for source ".$result."\n");      
    } else {
        $err = $client->getError();
        if ($err) {
           fwrite($lg, "Error lookupqueue for source ".$err."\n"); 
        } else {
            // process results
            if($result['LookupQueueResult']['StatusCode'] == 'OK' and $result['LookupQueueResult']['StatusDescription'] == 'Records loaded' and empty($result['LookupQueueResult']['Records']) == false){
                
                $processobjects = array(); //holds objects to process
                
                //identify if there is single rec or multiple
                if(array_key_exists('RecordNumber', $result['LookupQueueResult']['Records']['SOAPRecord'])){
                    //single record
                    $processobjects[0] = $result['LookupQueueResult']['Records']['SOAPRecord'];
                } else{
                    //multiple records
                    $processobjects = $result['LookupQueueResult']['Records']['SOAPRecord'];
                }
                                
                fwrite($lg, "Info No of Source objects in iteration ".$counter. " is ". count($processobjects)."\n");
                $srccnt += count($processobjects);
                
                //iterate over objects and process them one by one
                foreach($processobjects as $k=>$v){
                    //dump into data file
                    if($dumpcontrol == true){
                        fwrite($dat, print_r($v, true)."\n");
                    } else{
                        //just rec number
                        fwrite($dat, $v['RecordNumber']."\n");
                    }
                    
                    //populate into dest
                    //create the object
                    $contactData = array('assigned_user_id'=>$userId, 'leadsource' => 'InterchangeComm');
                    foreach($v as $prop=>$attr){
                        if(array_key_exists($prop, $fieldmap)){
                            $contactData[$fieldmap[$prop]] = $attr;
                        }
                        
                        //for analysis codes part
                        if($prop == 'AnalysisCodes'){                                                       
                            $codearrays = array();                            
                             //check if values are empty
                            if(count($attr['SOAPRecordField']) > 0){
                                  //check if single element
                                  if(array_key_exists('Identifier', $attr['SOAPRecordField'])){
                                    $codearrays[0] = $attr['SOAPRecordField'];
                                  }else{
                                    //multiple records
                                    $codearrays = $attr['SOAPRecordField'];
                                  }
                                  
                                  //now process those codes using lookup
                                  foreach($codearrays as $idx => $arr){
                                        //2 step lookup - first lookup caption from analysiscodes map, then lookup from fieldmap
                                        if(array_key_exists($arr['Identifier'], $analysiscodes)){
                                            if(array_key_exists($analysiscodes[$arr['Identifier']]['caption'], $fieldmap)){
                                                $contactData[$fieldmap[$analysiscodes[$arr['Identifier']]['caption']]] = $analysiscodes[$arr['Identifier']][$arr['Value']];
                                            }else{
                                                fwrite($lg, "WARNING lookup not available in fieldmap for ".$arr['Identifier']."\n");
                                            }
                                            
                                        } else {
                                            fwrite($lg, "WARNING lookup not available in analysiscodes for ".$arr['Identifier']."\n");
                                        }
                                  }
                            }                          
                        }
                        
                        //for data protection code part
                        if($prop == 'DataProtectionCodes'){
                            $dp = $attr['SOAPRecordField'];
                            //check if value is empty
                            if(count($dp) > 0){                               
                                        //2 step lookup - first lookup caption from analysiscodes map, then lookup from fieldmap
                                        if(array_key_exists($dp['Identifier'], $analysiscodes)){
                                            if(array_key_exists($analysiscodes[$dp['Identifier']]['caption'], $fieldmap)){
                                                $contactData[$fieldmap[ $analysiscodes[$dp['Identifier']]['caption'] ]] = $analysiscodes[$dp['Identifier']] [$dp['Value']];
                                            } else {
                                                fwrite($lg, "WARNING lookup not available in fieldmap for ".$dp['Identifier']."\n");
                                            }
                                            
                                        }  else {
                                            fwrite($lg, "WARNING lookup not available in analysiscodes for ".$dp['Identifier']."\n");
                                        }
                                            
                            }
                        }
                        
                        //for other fields
                        if($prop == 'OtherFields' ){                           
                             $otherarrays = array();                            
                             //check if values are empty
                            if(count($attr['SOAPRecordField']) > 0){
                                  //check if single element
                                  if(array_key_exists('Identifier', $attr['SOAPRecordField'])){
                                    $otherarrays[0] = $attr['SOAPRecordField'];
                                  }else{
                                    //multiple records
                                    $otherarrays = $attr['SOAPRecordField'];
                                  }
                                  
                                  //now process those codes using lookup
                                  foreach($otherarrays as $lakh => $jhya){
                                        //2 step lookup - first lookup caption from analysiscodes map, then lookup from fieldmap
                                        if(array_key_exists($jhya['Identifier'], $analysiscodes)){
                                            if(array_key_exists($analysiscodes[$jhya['Identifier']]['caption'], $fieldmap)){
                                                $contactData[$fieldmap[$analysiscodes[$jhya['Identifier']]['caption']]] = $jhya['Value'];
                                            } else {
                                                fwrite($lg, "WARNING lookup not available in fieldmap for ".$jhya['Identifier']."\n");
                                            }
                                            
                                        } else {
                                            fwrite($lg, "WARNING lookup not available in analysiscodes for ".$jhya['Identifier']."\n");
                                        }
                                  }
                            }               
                        }
                        
                    }
                    //populate
                    $objectJson = Zend_JSON::encode($contactData);                    
                     
                    //sessionId is obtained from loginResult.
                    $params = array("sessionName"=>$sessionId, "operation"=>'create', 
                        "element"=>$objectJson, "elementType"=>$moduleName);
                    //Create must be POST Request.
                    //$httpc = new HTTP_Client();
                    $httpc->post("$destURL", $params, true);
                    $response = $httpc->currentResponse();
                    $jsonResponse = Zend_JSON::decode($response['body']);
                    
                    //operation was successful get the token from the reponse.
                    if($jsonResponse['success']==false){
                        //collect failed rec nums
                        array_push($except_array, $v['RecordNumber']);
                        fwrite($lg, "Error vtiger contact creation failed for recnum". $v['RecordNumber']. " DETAILS: ". $jsonResponse['error']['errorMsg']."\n");                         
                    }
                    $savedObject = $jsonResponse['result'];                    
                    $id = $savedObject['id'];
                    if($jsonResponse['success']!=false){
                      fwrite($lg, "SUCCESS: Created contact for recnum ". $v['RecordNumber']. " Object ID: ". $id."\n");
                      
                      //increment counter if success
                      $destcnt++;
                    }
                   
                   //TODO : remove this later
                    //$flag = false;
               
                }
                
            
            //markqueue TODO : uncomment this part later
            fwrite($lg, "INFO: Preselect TimeStamp ". $result['LookupQueueResult']['PreSelectTimestamp']."\n");
            $markresult = $client->call('MarkQueue', array('UserName' => $srcUser, 'Password' => $srcPwd, 'EventRef' => $srcEvent, 'QueueIdentifier' => $srcQueueIdentifier, 'PreSelectTimestamp' => $result['LookupQueueResult']['PreSelectTimestamp']));
                if ($client->fault) {
                    fwrite($lg, "Error Fault markqueue for source ".$markresult."\n");      
                } else {
                    $err = $client->getError();
                    if ($err) {
                       fwrite($lg, "Error markqueue for source ".$err."\n"); 
                    } else {
                        if($markresult['MarkQueueResult']['StatusCode'] == 'OK'){
                            fwrite($lg, "INFO Markqueue successful for timestamp ".$result['LookupQueueResult']['PreSelectTimestamp']."\n");
                        }else{
                            fwrite($lg, "INFO Markqueue problem for timestamp ".$result['LookupQueueResult']['PreSelectTimestamp']."\n");
                        }
                    }
                }
            } else{
                //status failed
                $flag = false;
            }
            
        }
    }
    
    $counter++;
}
fwrite($lg, "INFO: Total Source Records Read ". $srccnt."\n");
fwrite($lg, "INFO: Total Destination Records populated ". $destcnt."\n");
if($destcnt < $srccnt){
    fwrite($lg, "INFO: Following records nums need to be repopulated ". print_r($except_array, true)."\n");
} elseif($destcnt == $srccnt){
    fwrite($lg, "INFO: SUCCESS all records from source were populated into destination\n");
}

//logout and invalidate vtiger session
$params = "operation=logout&sessionName=$sessionId";

//logout must be GET Request.
$httpc->get("$destURL?$params");
$response = $httpc->currentResponse();
$jsonResponse = Zend_JSON::decode($response['body']);
//operation was successful get the token from the reponse.
if($jsonResponse['success']==false){
    //handle the failure case.
    fwrite($lg, "Error vtiger session logout failed\n");                         
}
//logout successful session terminated.
fwrite($lg, "INFO vtiger session logout done\n"); 


fclose($dat);
fclose($lg);

?>
