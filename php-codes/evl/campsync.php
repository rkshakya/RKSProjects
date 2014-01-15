<?php
include "./config.php";
include "./class.phpmailer.php";

/*
* Script to sync campaigns from IC to Backoffice MySQL
* Please enter configuration information in config.php 
* Author - Ravi K Shakya
*/

//$logFile = "campsync_".date('Y_m_d_H_i').".log";
//$lg = fopen($logFile, 'w') or die("Can't open log file");

//get mysql connection
$dbhandle = mysqli_connect($MYSQL_SERVER, $MYSQL_USER, $MYSQL_PASS, $MYSQL_DB)
  or die("Couldn't connect to MySQL Server");
  
$sqlmisc = "SELECT `key`, `value` from evl_miscsettings";
$resultmisc = mysqli_query($dbhandle, $sqlmisc);

$miscsettings = array();

while ($rowmisc = mysqli_fetch_assoc($resultmisc)) {
    $miscsettings[$rowmisc['key']] = $rowmisc['value'];
}
  
$sqlkeys = "SELECT `api_url`, `api_key` from evl_managedaccounts where is_deleted = 0";
$resultkeys = mysqli_query($dbhandle, $sqlkeys);

$keysettings = array();

while ($rowkeys = mysqli_fetch_assoc($resultkeys)) {
    $keysettings[$rowkeys['api_key']] = $rowkeys['api_url'];
}


foreach ($keysettings as $kik => $volvo){
    //do campaign sync from IC to MySQL
    $curl = curl_init();
    curl_setopt_array($curl, array(
	CURLOPT_RETURNTRANSFER => 1,
	CURLOPT_URL => $volvo."/?req=api&api_key=".$kik."&mode=list_campaigns"
    ));
    //fwrite($lg, "INFO: URL for campaign sync: ".$volvo."/?req=api&api_key=".$kik."&mode=list_campaigns\n");
    
    $resp = curl_exec($curl);
    curl_close($curl);
    $xml = simplexml_load_string($resp);
    
    $processobjects = $xml -> items;
    
    //get campaign id and campaign list from MySQL DB  
    $sqlcam = "SELECT campaign_id, campaign_name from evl_dbmappings";
    $resultcam = mysqli_query($dbhandle, $sqlcam);
    
    $frmDB = array();
    
    while ($rowcam = mysqli_fetch_assoc($resultcam)) {
	$frmDB[$rowcam['campaign_id']] = $rowcam['campaign_name'];
    }
    
    //gather from API into an assoc array
    $frmAPI = array();
    foreach($processobjects->item as $k => $v){
	$frmAPI["$v->id"] = $v->series_name;	
     }
      
     //get the diff
     $dif = array_diff_key($frmAPI, $frmDB);
     $msgCam = "";
     
     foreach($dif as $ky => $va){
	//fwrite($lg, "INFO: New Campaigns: ".$ky."->".$va."\n");
	$msgCam .= $ky ."->".$va. "\n";
	
	$insqry = "INSERT INTO evl_dbmappings(campaign_id, campaign_name, apkey) VALUES ('$ky', '$va', '$kik')";
	$insres = mysqli_query($dbhandle, $insqry);
	//fwrite($lg, "ERROR: ".mysqli_error($dbhandle)."\n");
	if(!$insres){
	    print "Failed inserting record";
	}
	
     }
     
     //update campaign names
     foreach($frmAPI as $ck => $cv){
	$selcname = "SELECT `campaign_name` FROM evl_dbmappings WHERE `campaign_id` = ".$ck." AND campaign_name <> '".$cv ."'";
	$resultcname = mysqli_query($dbhandle, $selcname);
    
	if($rowcname = mysqli_fetch_assoc($resultcname)) {
	    $oldname = $rowcname['campaign_name'];
	    
	    mysqli_autocommit($dbhandle, false);
	    $xnflag = true;
	    
	    //populate history
	    $inshist = "INSERT INTO evl_camphistory(camp_id, oldname, newname) VALUES ('$ck', '$oldname', '$cv')";
	    
	    $resinshist = mysqli_query($dbhandle, $inshist);

	    if (!$resinshist) {
		$xnflag = false;
		echo "Error details: " . mysqli_error($dbhandle) . ". ";
	    }
	    
	    //update camp name
	    $udtcname = "UPDATE evl_dbmappings SET `campaign_name` = '".$cv. "' WHERE `campaign_id` = ".$ck." AND campaign_name <> '".$cv."'";
	    $resudtcname = mysqli_query($dbhandle, $udtcname);

	    if (!$resudtcname) {
		$xnflag = false;
		echo "Error details: " . mysqli_error($dbhandle) . ". ";
	    }
	    
	    if ($xnflag) {
		mysqli_commit($dbhandle);
		//echo "All queries were executed successfully";
	    } else {
		mysqli_rollback($dbhandle);
		//echo "All queries were rolled back";  
	    } 
	}
     }
     
     //send out mail if there are new campaigns added
	if(sizeof($dif) > 0){
		    $mail = new PHPMailer();
		    
		    $mail->IsSMTP();
		    $mail->SMTPAuth = true;
		    //$mail->SMTPSecure = "tls";
		    //$mail->SMTPDebug = true;
	    
		    $mail->Host = $miscsettings['SMTP_SERVER'];
		    $mail->Port = $miscsettings['SMTP_PORT'];
		    $mail->Username = $miscsettings['SMTP_USER'];
		    $mail->Password = $miscsettings['SMTP_PASS'];
			    
		    $mail->SetFrom($miscsettings['FROM_EMAIL'], $miscsettings['FROM_NAME']);
		    $mail->AddReplyTo("", "");
		    $mail->Subject = $miscsettings['SUBJECT'];
		    $mail->AddAddress($miscsettings['TO_EMAIL'], $miscsettings['TO_EMAIL']);
			
		    $message = "";
		    
		    //TODO include campaign related message
		    if(count($dif) > 0){
			    $message .= "IMPORTANT! Following new campaigns were added to MySQL DB. Pls populate SQL Server info for them.\n";
			    $message .= $msgCam;
			    
		    }
		    
		    $mail->Body = $message;
		    $mail->Send();
		    //fwrite($lg, "INFO : Mail Sent\n");
	}           

} 
//fclose($lg);
mysqli_close($dbhandle);

?>