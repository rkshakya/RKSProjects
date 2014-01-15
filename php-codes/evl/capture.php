<?php
include "./config.php";
include "./class.phpmailer.php";

/*
* Script to capture entity information from Outgoing API of Instant Customer and populate into SQLServer DB.
* Please enter configuration information in config.php 
* Author - Ravi K Shakya
*/

function preparedata($data){
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    $data = urldecode($data);
    $data = ms_escape_string($data);
    return $data;
}

function ms_escape_string($data) {
        if ( !isset($data) ) return '';
        if ( is_numeric($data) ) return $data;

        $non_displayables = array(
            '/%0[0-8bcef]/',            // url encoded 00-08, 11, 12, 14, 15
            '/%1[0-9a-f]/',             // url encoded 16-31
            '/[\x00-\x08]/',            // 00-08
            '/\x0b/',                   // 11
            '/\x0c/',                   // 12
            '/[\x0e-\x1f]/'             // 14-31
        );
        foreach ( $non_displayables as $regex )
            $data = preg_replace( $regex, '', $data );
        $data = str_replace("'", "''", $data );
        return $data;
    }

//function to add 0 prefix in entity code
function padd($num, $numcode){
	$pref = '';
	for ($cn = 0; $cn < ($numcode - strlen($num)); $cn++){
	    $pref .= "0";
	}

	 return $pref.$num;
}

if( isset($_POST['syncmode']) ){
  $logFile = "maninfo_".date('Y_m_d_H_i').".log";
}else{
  $logFile = "fininfo_".date('Y_m_d_H_i').".log";
}
$lg = fopen($logFile, 'w') or die("Can't open log file");

//gather POST variables, sanitize and populate into assoc array
if ($_SERVER["REQUEST_METHOD"] == "POST"){
   $formData = array();
    //get POST params 
    foreach($_POST as $field => $value){        
        $formData[$field] = preparedata($value);       
    } 
        
} else{
    exit;
}

//load API and MAIL settings into an asso array from evl_miscsettings table

//get mysql connection
$dbhandle = mysql_connect($MYSQL_SERVER, $MYSQL_USER, $MYSQL_PASS)
  or die("Couldn't connect to MySQL Server");

//select a database to work with
$selected = mysql_select_db($MYSQL_DB, $dbhandle)
  or die("Couldn't open database $MYSQL_DB");
  
$sqlmisc = "SELECT `key`, `value` from evl_miscsettings";
$resultmisc = mysql_query($sqlmisc);

$miscsettings = array();

while ($rowmisc = mysql_fetch_assoc($resultmisc)) {
    $miscsettings[$rowmisc['key']] = $rowmisc['value'];
}
 
 //get SQL Server and other settings for this campaign
$qrySQLServ = "SELECT m.*, a.api_url from evl_dbmappings m, evl_managedaccounts a where m.apkey = a.api_key and m.campaign_id =".$formData['campaign_id'];
$resSQLServ = mysql_query($qrySQLServ);

$DEST_DB_SERVER = "";
$DEST_DB_PORT = "";
$DEST_DB = "";
$DEST_DB_USER = "";
$DEST_DB_PASS = "";

$CURRENCY_ID = "";
$CUSTOMER_TYPE_ID = "";
$ENTITY_TYPE_ID = "";
$ENTITY_CREATED_USERID = "";
$ENTITY_MODIFIED_USERID = "";
$COUNTRY_ID = "";
$ENTITY_IS_ACTIVE = "";
$SOURCE_ID = "";
$ENTITY_OKTOCALL = "";
$ENTITY_OKTOFAX = "";
$ENTITY_OKTOEMAIL = "";
$ENTITY_RECSTATUS = "";
$UCARRESELLER_C = "";

$ACTTYPE_ID = "";
$ACTSTATUS_ID = "";
$ACTCATEGORY_ID = "";
$IINCIDENT_TYPEID_C = "";
$IINCIDENT_TYPEGROUPID_C = "";
$ACTIVITY_RECSTATUS = "";
$ACTIVITY_CREATEDUSERID = "";
$ACTIVITY_MODIFIEDUSERID = "";
$ACTIVITY_USERID = "";
$NUM_ENTCODE = "";

$CUST1 = "";
$CUST2 = "";
$CUST3 = "";
$CUST4 = "";
$CUST5 = "";

$MAILER_FLAG = "";
$SMTP_SERVER = "";
$SMTP_PORT = "";
$SMTP_USER = "";
$SMTP_PASS = "";
$FROM_EMAIL = "";
$FROM_NAME = "";
$TO_EMAIL = "";
$CC_EMAIL = "";
$SUBJECT = "";

$API_URL = "";
$API_KEY = "";

$msgNoDB = "";


if ($rowSQLServ = mysql_fetch_assoc($resSQLServ)) {
    $DEST_DB_SERVER = $rowSQLServ['dbserver'];
    $DEST_DB_PORT = $rowSQLServ['dbport'];
    $DEST_DB = $rowSQLServ['dbname'];
    $DEST_DB_USER = $rowSQLServ['dbuser'];
    $DEST_DB_PASS = $rowSQLServ['dbpass'];
    
    $CURRENCY_ID = $rowSQLServ['CurrencyID'];
    $CUSTOMER_TYPE_ID = $rowSQLServ['CustomerTypeID'];
    $ENTITY_TYPE_ID = $rowSQLServ['EntityTypeID'];
    $ENTITY_CREATED_USERID = $rowSQLServ['EntityCreatedUserID'];
    $ENTITY_MODIFIED_USERID = $rowSQLServ['EntityModifiedUserID'];
    $COUNTRY_ID = $rowSQLServ['CountryID'];
    $ENTITY_IS_ACTIVE = $rowSQLServ['EntityIsActive'];
    $SOURCE_ID = $rowSQLServ['SourceID'];
    $ENTITY_OKTOCALL = $rowSQLServ['EntityOkToCall'];
    $ENTITY_OKTOFAX = $rowSQLServ['EntityOkToFax'];
    $ENTITY_OKTOEMAIL = $rowSQLServ['EntityOkToEmail'];
    $ENTITY_RECSTATUS = $rowSQLServ['EntityRecStatus'];
    $UCARRESELLER_C = $rowSQLServ['ucARReseller_C'];
    
    $ACTTYPE_ID = $rowSQLServ['ActTypeID'];
    $ACTSTATUS_ID = $rowSQLServ['ActStatusID'];
    $ACTCATEGORY_ID = $rowSQLServ['ActCategoryID'];
    $IINCIDENT_TYPEID_C = $rowSQLServ['iIncidentTypeID_C'];
    $IINCIDENT_TYPEGROUPID_C =  $rowSQLServ['iIncidentTypeGroupID_C'];
    $ACTIVITY_RECSTATUS = $rowSQLServ['ActivityRecStatus'];
    $ACTIVITY_CREATEDUSERID = $rowSQLServ['ActivityCreatedUserID'];
    $ACTIVITY_MODIFIEDUSERID = $rowSQLServ['ActivityModifiedUserID'];
    $ACTIVITY_USERID = $rowSQLServ['UserID'];
    $NUM_ENTCODE = $rowSQLServ['codedigits'];
    
    $CUST1 = $rowSQLServ['u_custom_1'];
    $CUST2 = $rowSQLServ['u_custom_2'];
    $CUST3 = $rowSQLServ['u_custom_3'];
    $CUST4 = $rowSQLServ['u_custom_4'];
    $CUST5 = $rowSQLServ['u_custom_5'];
    
    $MAILER_FLAG = $rowSQLServ['MAILER_FLAG'];
    $SMTP_SERVER = $rowSQLServ['SMTP_SERVER'];
    $SMTP_PORT = $rowSQLServ['SMTP_PORT'];
    $SMTP_USER = $rowSQLServ['SMTP_USER'];
    $SMTP_PASS = $rowSQLServ['SMTP_PASS'];
    $FROM_EMAIL = $rowSQLServ['FROM_EMAIL'];
    $FROM_NAME = $rowSQLServ['FROM_NAME'];
    $TO_EMAIL = $rowSQLServ['TO_EMAIL'];
    $CC_EMAIL = $rowSQLServ['CC_EMAIL'];
    $SUBJECT = $rowSQLServ['SUBJECT'];
    $API_URL = $rowSQLServ['api_url'];
    $API_KEY = $rowSQLServ['apkey'];
}

if(strlen(trim($DEST_DB)) == 0) {
   $msgNoDB = "ALERT! SQLServer DB info not populated for Campaign ID :".$formData['campaign_id']. " Campaign name : ". $formData['campaign_name']."\n";
}

mysql_close($dbhandle);

fwrite($lg, "INFO: ".$DEST_DB_SERVER."->".$DEST_DB_PORT."->".$DEST_DB."->".$DEST_DB_USER."->".$DEST_DB_PASS."\n");

//get sqlserver connection
$dbhandle = mssql_connect($DEST_DB_SERVER.":".$DEST_DB_PORT, $DEST_DB_USER, $DEST_DB_PASS)
  or die("Couldn't connect to SQL Server on $DEST_DB_SERVER");

//select a database to work with
$selected = mssql_select_db($DEST_DB, $dbhandle)
  or die("Couldn't open database $DEST_DB");
  
	//generate combined name
	$combname = $formData['u_firstname']." ".$formData['u_lastname'];
	
	//if company name - check for company name
	if(isset($formData['u_company']) && strlen(trim($formData['u_company'])) > 0){
		$tempCompany = $formData['u_company'];
	} else{
		//if no company name - check for firstname + lastname
		$tempCompany = $combname;
	}
	
	//decide which type of phone
	if($formData['u_phone_type'] == 'cellular'){
		$cellphone = $formData['u_phone'];
		$landline = '';
	}else{
		$cellphone = '';
		$landline = $formData['u_phone'];
	}
	
	$ENTID = 0;

  //chk if this is update or add request - if ID is present in dest SQLServer DB, it is update request else add request
  $qryMode = "SELECT EntityID FROM RM_Entity WHERE InstantCustomerID_c = '" . $formData['id'] . "'";
  
  fwrite($lg, "INFO: Query for preexist check: ".$qryMode."\n");
	
  $res = mssql_query($qryMode);
  if(mssql_num_rows($res) > 0){
	//rec already exists - do updates
	fwrite($lg, "INFO: Updating record with InstantCustomerID_c = ".$formData['id']."\n");
	$udtmsg = "Record with InstantCustomerID_c = ".$formData['id']." was updated.\n";
	
	$condition = array('unsubscribe_by_sms', 'unsubscribe_by_web');
	fwrite($lg, "INFO: MODE ".$formData['mode']."\n");
	
	//simple update
	$qryUpdate = "UPDATE RM_Entity SET 
		EntityName = '".$tempCompany."',
		EntityAddress1 = '".$formData['u_address']."',
		EntityAddress2 = '".$formData['u_address_2']."',
		EntityCity = '".$formData['u_city']."',
		EntityState = '".$formData['u_state']."',
		EntityPostCode ='".$formData['u_zip']."',
		EntityPhone = '".$landline."',
		Telephone_C = '".$cellphone."',
		EntityEmail = '".$formData['u_email']."',		
		ucARMarketingSource_C = '".$formData['campaign_name']."',
		Contact_Person_C ='".$combname."',
		Addressee_C ='".$formData['u_firstname']."',
		ucARLastName_C ='".$formData['u_lastname']."',";		
		
		if (in_array($formData['mode'], $condition)) {
		    $qryUpdate .= "EntityOkToEmail = 'false',";
		}
		
		if($CUST1 <> "NA"){
		    $qryUpdate .= "$CUST1 = '".$formData['u_custom_1']."',";
		}
		if($CUST2 <> "NA"){
		    $qryUpdate .= "$CUST2 = '".$formData['u_custom_2']."',";
		}
		if($CUST3 <> "NA"){
		    $qryUpdate .= "$CUST3 = '".$formData['u_custom_3']."',";
		}
		if($CUST4 <> "NA"){
		    $qryUpdate .= "$CUST4 = '".$formData['u_custom_4']."',";
		}
		if($CUST5 <> "NA"){
		    $qryUpdate .= "$CUST5 = '".$formData['u_custom_5']."',";
		}
		
	$qryUpdate .= " facebook_C ='".$formData['u_facebook']."',
	LinkedIn_C ='".$formData['u_linkedin']."',
	Twitter_C = '".$formData['u_twitter']."'
	WHERE InstantCustomerID_c = ".$formData['id'];
		
	fwrite($lg, "INFO: Existing doc update query: ".$qryUpdate."\n");
		
	$udtexist = mssql_query($qryUpdate);	
	$udtrowexist = mssql_num_rows($udtexist);
		
	fwrite($lg, "INFO: Existing rec update status: ".$udtrowexist."\n");
	
	//populate Activity entries
	//populate Activity entries
	//get EntityID
	//$resEID = mssql_query("SELECT EntityID FROM RM_Entity WHERE InstantCustomerID_c = '".$formData['id']."'");
	//
	//if($rowEID = mssql_fetch_array($resEID)){
	//    $eid = $rowEID["EntityID"];
	//    fwrite($lg, "INFO: Fetched Entity ID: ".$eid."\n");
	//    $EID = $rowEID["EntityID"];
	//}
	//	
	////get the current activity code
	//$resAct = mssql_query("SELECT LastCodeNumber, LastCodePrefix FROM SYS_LastCode WHERE LastCodePrefix = 'ACT'");
	//
	//if($rwAct = mssql_fetch_array($resAct)){
	//    $lastact = $rwAct["LastCodeNumber"];
	//    fwrite($lg, "INFO: Last Activity Code Number: ".$rwAct["LastCodeNumber"]."\n");
	//    fwrite($lg, "INFO: Last Activity Code Prefix: ".$rwAct["LastCodePrefix"]."\n");
	//}
	//	
	//	//generate new entity code
	//$lastact = $lastact + 1;
	//$padlastact = padd($lastact, $NUM_ENTCODE);
	//	
	////insert into RM_Activity
	//$insqryact = "INSERT INTO RM_Activity (EntityID, ActivityCode, ActivitySubject,ActStatusID, ActCategoryID, iIncidentTypeID_C, iIncidentTypeGroupID_C,
	//ActivityRecStatus, ActivityCreatedUserID, ActivityModifiedUserID, ActTypeID) VALUES
	//(".$eid.", '".$rwAct["LastCodePrefix"]."-".$padlastact."' ,'Instant Customer - Entity Update[".$formData['campaign_name']."]', ".$ACTSTATUS_ID.", ".$ACTCATEGORY_ID.", ".$IINCIDENT_TYPEID_C.", ".$IINCIDENT_TYPEGROUPID_C.", ".$ACTIVITY_RECSTATUS.", ".$ACTIVITY_CREATEDUSERID.", ".$ACTIVITY_MODIFIEDUSERID.", ".$ACTTYPE_ID.")";
	//	
	//fwrite($lg, "INFO: Activity insertion Query: ".$insqryact."\n");
	//	
	//$insresact = mssql_query($insqryact);	
	//$insrwact = mssql_num_rows($insresact);		
	//	
	//fwrite($lg, "INFO: Activity Insertion Status: ".$insrwact."\n");
	//	
	////update the Activity code
	//$udtqryAct = "UPDATE SYS_LastCode SET LastCodeNumber = ".$lastact." WHERE LastCodePrefix = '".$rwAct["LastCodePrefix"]."' AND LastCodeNumber=".$rwAct["LastCodeNumber"];
	//fwrite($lg, "INFO: Activity UPdate Query: ".$udtqryAct."\n");
	//	
	//$udtresAct = mssql_query($udtqryAct);	
	//$udtrwAct = mssql_num_rows($udtresAct);
	//	
	//fwrite($lg, "INFO: ActivityNum Updation Status: ".$udtrwAct."\n");
	//fwrite($lg, "INFO: ActivityNum updated to: ".$lastact."\n");
	
	
	
  }else{
	//rec does not exist - add new entry
	fwrite($lg, "INFO: Adding a new record..\n");
	$dupFlag = 0;
	
	//chk if any dups based on company name or email in ESD DB
	
	
	$qryCompany = "SELECT EntityID FROM RM_Entity WHERE (EntityName = '" . $tempCompany . "' OR EntityEmail = '".trim($formData['u_email']). "')";
	
	fwrite($lg, "INFO: Query for dup detection: ".$qryCompany."\n");
	
	$result = mssql_query($qryCompany);
	if($row = mssql_fetch_array($result)){
		//set dupFlag
		$dupFlag = 1;
		$emlMsg = "ALERT! Skipping population as duplicate record found on Destination SQLServer DB table. EntityID :".$row["EntityID"]."\n";
		
		fwrite($lg, "INFO: Duplicate record found in dest DB: ".$row["EntityID"]."\n");
		
		$EID = $row["EntityID"];
		
		//get the current activity code
		$resultActEml = mssql_query("SELECT LastCodeNumber, LastCodePrefix FROM SYS_LastCode WHERE LastCodePrefix = 'ACT'");
	
		if($rowActEml = mssql_fetch_array($resultActEml)){
			$lastcodeacteml = $rowActEml["LastCodeNumber"];
			fwrite($lg, "INFO: Last Activity Code Number: ".$rowActEml["LastCodeNumber"]."\n");
			fwrite($lg, "INFO: Last Activity Code Prefix: ".$rowActEml["LastCodePrefix"]."\n");
		}
		
		//generate new entity code
		$lastcodeacteml = $lastcodeacteml + 1;
		$padlastcodeacteml = padd($lastcodeacteml, $NUM_ENTCODE);
		
		//start xn here
		mssql_query("BEGIN TRAN");  
		
		//insert into RM_Activity
		$insqueryacteml = "INSERT INTO RM_Activity (EntityID, ActivityCode, ActivitySubject,ActStatusID, ActCategoryID, iIncidentTypeID_C, iIncidentTypeGroupID_C,
		ActivityRecStatus, ActivityCreatedUserID, ActivityModifiedUserID, UserID, ActTypeID) VALUES
		(".$row["EntityID"].", '".$rowActEml["LastCodePrefix"]."-".$padlastcodeacteml."' ,'Instant Customer - Dup rec[".$formData['campaign_name']."]', ".$ACTSTATUS_ID.", ".$ACTCATEGORY_ID.", ".$IINCIDENT_TYPEID_C.", ".$IINCIDENT_TYPEGROUPID_C.", ".$ACTIVITY_RECSTATUS.", ".$ACTIVITY_CREATEDUSERID.", ".$ACTIVITY_MODIFIEDUSERID.", ".$ACTIVITY_USERID.", ".$ACTTYPE_ID.")";
		
		fwrite($lg, "INFO: Activity insertion Query: ".$insqueryacteml."\n");
		
		$insresultacteml = mssql_query($insqueryacteml);
		if(!$insresultacteml)  {  
		    mssql_query("ROLLBACK");  
		    exit();  
		}  
		
		$insrowacteml = mssql_num_rows($insresultacteml);		
		
		fwrite($lg, "INFO: Acitivity Insertion Status: ".$insrowacteml."\n");
		
		//update the Activity code
		$udtqueryActEml = "UPDATE SYS_LastCode SET LastCodeNumber = ".$lastcodeacteml." WHERE LastCodePrefix = '".$rowActEml["LastCodePrefix"]."' AND LastCodeNumber=".$rowActEml["LastCodeNumber"];
		fwrite($lg, "INFO: Activity UPdate Query: ".$udtqueryActEml."\n");
		
		$udtresultActEml = mssql_query($udtqueryActEml);
		if(!$udtresultActEml)  {  
		    mssql_query("ROLLBACK");  
		    exit();  
		}
		
		if(($insresultacteml) and ($udtresultActEml))  {  
		    mssql_query("COMMIT");  
		} 
		
		$udtrowActEml = mssql_num_rows($udtresultActEml);
		
		fwrite($lg, "INFO: ActivityNum Updation Status: ".$udtrowActEml."\n");
		fwrite($lg, "INFO: ActivityNum updated to: ".$lastcodeacteml."\n");
		
		fwrite($lg, "INFO: NOTES2: ".$formData['u_notes_2']."\n");
		fwrite($lg, "INFO: ENTITYID: ".$row["EntityID"]."\n");
		
		//update entityid into notes_2 field on IC for dup doc - do only if notes_2 != entityid
		//dynamically form the request URL
		//$formData['u_notes_2'] <> $row["EntityID"]
		if(  $formData['u_notes_2'] <> $row["EntityID"] ){
		    $updtURL = $API_URL."/?req=api&api_key=".$API_KEY."&mode=edit_subscriber&id=".$formData['id']."&u_notes_2=".$row["EntityID"]."&campaign_id=".$formData['campaign_id'];
		    
		    fwrite($lg, "INFO: URL for dupe marking on source: ".$updtURL."\n");
		    
		    $curl = curl_init();
		    curl_setopt_array($curl, array(
			CURLOPT_RETURNTRANSFER => 1,
			CURLOPT_URL => $updtURL
		    ));
		    $resp = curl_exec($curl);
		    curl_close($curl);
		    $xml = simplexml_load_string($resp);
		    
		    fwrite($lg, "INFO: Status for dupe marking on source: ".$xml->success."\n");
		}		
				
	}
	
	//if no, send email as well as insertion into DB
	if($dupFlag == 0){
		//get the current entity code
		$resultEntCode = mssql_query("SELECT LastCodeNumber, LastCodePrefix FROM SYS_LastCode WHERE LastCodePrefix = 'ENT'");
	
		if($rowEntCode = mssql_fetch_array($resultEntCode)){
			$lastcode = $rowEntCode["LastCodeNumber"];
			fwrite($lg, "INFO: Last Entity Code Number: ".$rowEntCode["LastCodeNumber"]."\n");
			fwrite($lg, "INFO: Last Entity Code Prefix: ".$rowEntCode["LastCodePrefix"]."\n");
		}
		
		//generate new entity code
		$lastcode = $lastcode + 1;
		$padlastcode = padd($lastcode, $NUM_ENTCODE);
		
		//insert into table
		$insquery = "INSERT INTO RM_Entity (
		CurrencyID,
		EntityCreatedUserID,
		EntityModifiedUserID,
		InstantCustomerID_c,
		EntityCode,
		EntityName,
		EntityIsActive,
		EntityAddress1,
		EntityAddress2,
		EntityCity,
		EntityState,
		CountryID,
		EntityPostCode,
		EntityTypeID,
		CustomerTypeID,
		EntityPhone,
		Telephone_C,
		EntityEmail,
		SourceID,
		EntityOkToCall,
		EntityOkToFax,
		EntityOkToEmail,
		EntityRecStatus,
		ucARReseller_C,
		ucARMarketingSource_C,
		Contact_Person_C,
		Addressee_C,
		ucARLastName_C,
		facebook_C,
		LinkedIn_C,";
		if($CUST1 <> "NA"){
		    $insquery .= $CUST1.',';
		}
		if($CUST2 <> "NA"){
		    $insquery .= $CUST2.',';
		}
		if($CUST3 <> "NA"){
		    $insquery .= $CUST3.',';
		}
		if($CUST4 <> "NA"){
		    $insquery .= $CUST4.',';
		}
		if($CUST5 <> "NA"){
		    $insquery .= $CUST5.',';
		}
		
		$insquery .= " Twitter_C)
		values
		('".$CURRENCY_ID."',
		'".$ENTITY_CREATED_USERID."',
		'".$ENTITY_MODIFIED_USERID."',
		'".$formData['id']."',
		'".$rowEntCode["LastCodePrefix"]."-".$padlastcode."',
		'".$tempCompany."',
		'".$ENTITY_IS_ACTIVE."',
		'".$formData['u_address']."',
		'".$formData['u_address_2']."',
		'".$formData['u_city']."',
		'".$formData['u_state']."',
		'".$COUNTRY_ID."',
		'".$formData['u_zip']."',
		'".$ENTITY_TYPE_ID."',
		'".$CUSTOMER_TYPE_ID."',
		'".$landline."',
		'".$cellphone."',
		'".$formData['u_email']."',
		'".$SOURCE_ID."',
		'".$ENTITY_OKTOCALL."',
		'".$ENTITY_OKTOFAX."',
		'".$ENTITY_OKTOEMAIL."',
		'".$ENTITY_RECSTATUS."',
		'".$UCARRESELLER_C."',
		'".$formData['campaign_name']."',
		'".$combname."',
		'".$formData['u_firstname']."',
		'".$formData['u_lastname']."',
		'".$formData['u_facebook']."',
		'".$formData['u_linkedin']."',";
		
		if($CUST1 <> "NA"){
		    $insquery .= "'".$formData['u_custom_1']."',";
		}
		if($CUST2 <> "NA"){
		    $insquery .= "'".$formData['u_custom_2']."',";
		}
		if($CUST3 <> "NA"){
		    $insquery .= "'".$formData['u_custom_3']."',";
		}
		if($CUST4 <> "NA"){
		    $insquery .= "'".$formData['u_custom_4']."',";
		}
		if($CUST5 <> "NA"){
		    $insquery .= "'".$formData['u_custom_5']."',";
		}
		
		$insquery .= "'".$formData['u_twitter']."')";
		fwrite($lg, "INFO: Insertion Query: ".$insquery."\n");
		
		//2nd xn here
		mssql_query("BEGIN TRAN"); 
		
		$insresult = mssql_query($insquery);
		if(!$insresult)  {  
		    mssql_query("ROLLBACK");  
		    exit();  
		}
		
		$insrow = mssql_num_rows($insresult);		
		
		fwrite($lg, "INFO: Insertion Status: ".$insrow."\n");
			
		//update the entity code
		$udtquery = "UPDATE SYS_LastCode SET LastCodeNumber = ".$lastcode." WHERE LastCodePrefix = '".$rowEntCode["LastCodePrefix"]."' AND LastCodeNumber=".$rowEntCode["LastCodeNumber"];
		fwrite($lg, "INFO: UPdate Query: ".$udtquery."\n");
		
		$udtresult = mssql_query($udtquery);
		if(!$udtresult)  {  
		    mssql_query("ROLLBACK");  
		    exit();  
		}
		$udtrow = mssql_num_rows($udtresult);
		
		fwrite($lg, "INFO: EntityNum Updation Status: ".$udtrow."\n");
		fwrite($lg, "INFO: EntityNum updated to: ".$lastcode."\n");
		
		//populate Activity entries
		//get EntityID
		$resEntID = mssql_query("SELECT EntityID FROM RM_Entity WHERE InstantCustomerID_c = '".$formData['id']."'");
	
		if($rowEntID = mssql_fetch_array($resEntID)){
			$entid = $rowEntID["EntityID"];
			fwrite($lg, "INFO: Fetched Entity ID: ".$entid."\n");
			$EID = $rowEntID["EntityID"];
		}
		
		//get the current activity code
		$resultAct = mssql_query("SELECT LastCodeNumber, LastCodePrefix FROM SYS_LastCode WHERE LastCodePrefix = 'ACT'");
	
		if($rowAct = mssql_fetch_array($resultAct)){
			$lastcodeact = $rowAct["LastCodeNumber"];
			fwrite($lg, "INFO: Last Activity Code Number: ".$rowAct["LastCodeNumber"]."\n");
			fwrite($lg, "INFO: Last Activity Code Prefix: ".$rowAct["LastCodePrefix"]."\n");
		}
		
		//generate new entity code
		$lastcodeact = $lastcodeact + 1;
		$padlastcodeact = padd($lastcodeact, $NUM_ENTCODE);
		
		//insert into RM_Activity
		$insqueryact = "INSERT INTO RM_Activity (EntityID, ActivityCode, ActivitySubject,ActStatusID, ActCategoryID, iIncidentTypeID_C, iIncidentTypeGroupID_C,
		ActivityRecStatus, ActivityCreatedUserID, ActivityModifiedUserID, UserID, ActTypeID) VALUES
		(".$entid.", '".$rowAct["LastCodePrefix"]."-".$padlastcodeact."' ,'Instant Customer - Campaign Response[".$formData['campaign_name']."]', ".$ACTSTATUS_ID.", ".$ACTCATEGORY_ID.", ".$IINCIDENT_TYPEID_C.", ".$IINCIDENT_TYPEGROUPID_C.", ".$ACTIVITY_RECSTATUS.", ".$ACTIVITY_CREATEDUSERID.", ".$ACTIVITY_MODIFIEDUSERID.", ".$ACTIVITY_USERID.",".$ACTTYPE_ID.")";
		
		fwrite($lg, "INFO: Activity insertion Query: ".$insqueryact."\n");
		
		$insresultact = mssql_query($insqueryact);
		
		if(!$insresultact)  {  
		    mssql_query("ROLLBACK");  
		    exit();  
		}
		
		$insrowact = mssql_num_rows($insresultact);		
		
		fwrite($lg, "INFO: Acitivity Insertion Status: ".$insrowact."\n");
		
		//update the Activity code
		$udtqueryAct = "UPDATE SYS_LastCode SET LastCodeNumber = ".$lastcodeact." WHERE LastCodePrefix = '".$rowAct["LastCodePrefix"]."' AND LastCodeNumber=".$rowAct["LastCodeNumber"];
		fwrite($lg, "INFO: Activity UPdate Query: ".$udtqueryAct."\n");
		
		$udtresultAct = mssql_query($udtqueryAct);
		if(!$udtresultAct)  {  
		    mssql_query("ROLLBACK");  
		    exit();  
		}
		
		if($insresult and $udtresult and $insresultact and $udtresultAct)  {  
		    mssql_query("COMMIT");  
		} 
		
		$udtrowAct = mssql_num_rows($udtresultAct);
		
		fwrite($lg, "INFO: ActivityNum Updation Status: ".$udtrowAct."\n");
		fwrite($lg, "INFO: ActivityNum updated to: ".$lastcodeact."\n");
		
	}
}
	
	fwrite($lg, "INFO: EID: ".$EID."\n");
		
	//send email
	if($MAILER_FLAG == 1){
		$mail = new PHPMailer();
		
		$mail->IsSMTP();
		$mail->SMTPAuth = true;
		//$mail->SMTPSecure = "tls";
		//$mail->SMTPDebug = true;
		
		//print $SMTP_SERVER . '->' .$SMTP_PORT. '->'.$SMTP_USER .'->'.$SMTP_PASS;
	
		$mail->Host = $SMTP_SERVER;
		$mail->Port = $SMTP_PORT;
		$mail->Username = $SMTP_USER;
		$mail->Password = $SMTP_PASS;
			
		$mail->SetFrom($FROM_EMAIL, $FROM_NAME);
		$mail->AddReplyTo("", "");
		$mail->Subject = $SUBJECT;
		$mail->AddAddress($TO_EMAIL, $TO_EMAIL);
		$mail->AddCC($CC_EMAIL, $CC_EMAIL);
		    
		$message = "";		
		
		//TODO include SQLServer info not found message
		if(strlen($msgNoDB)){
			$message .= $msgNoDB;
		}
		
		if(strlen($udtmsg)){
			$message .= $udtmsg;
		}
		
		if($dupFlag == 1){
			$message = $emlMsg;
		}
		$message .= "Hi, \n Following entity information received from Instant Customer to populate into SQLServer DB:\n";
		$message .= "First name : ".$formData['u_firstname']."\n";
		$message .= "Last name : ".$formData['u_lastname']."\n";
		$message .= "Company : ".$formData['u_company']."\n";
		$message .= "IC ID : ".$formData['id']."\n";
		$message .= "Campaign name : ".$formData['campaign_name']."\n";
		$message .= " This is auto-generated message. Please do not reply to it";
		
		$mail->Body = $message;
		$mail->Send();
		fwrite($lg, "INFO : Mail Sent\n");
	}
	

/* Free statement and connection resources. */    
mssql_close($dbhandle);
fclose($lg);

//send 200 in resp
echo $EID;
http_response_code(200);

?>
