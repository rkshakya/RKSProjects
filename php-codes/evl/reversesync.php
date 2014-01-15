<?php
include "./config.php";

/*
* Script to reverse sync Entity/Contact information from Evolve CRM DB to Instant Customer.
* 
* Author - Ravi K Shakya
*/


//SQLServer related
//$SQL_SERVER = '5.152.180.41';
//$SQL_PORT = 1433;
//$SQL_DB = 'EVOLVECRM_EVODEMO';
//$SQL_USER = 'sa';
//$SQL_PASS = 'hp8442W';

$APIURL = 'http://www.instantcustomer.com';

function preparedata($data){
    $data = trim($data);
    $data = urlencode($data);
    return $data;
}

//get data from SQL Server
//populate into mysql stage table
if(isset($_GET['dbid']) && is_numeric($_GET['dbid'])){
    
$dbid = $_GET['dbid'];

$logFile = "revsyncinfo_".date('Y_m_d_H_i').".log";
$lg = fopen($logFile, 'w') or die("Can't open log file");

$mydbhandle = mysql_connect($MYSQL_SERVER, $MYSQL_USER, $MYSQL_PASS)
  or die("Couldn't connect to MySQL Server");
$myselected = mysql_select_db($MYSQL_DB, $mydbhandle)
  or die("Couldn't open database $MYSQL_DB");
  
//fetch sqlserver settings
if($dbid == 999999){
    $sqlsettings = "SELECT * FROM evl_revsync_settings where is_deleted = 0 and include_autosync = 1";
}else{
    $sqlsettings = "SELECT * FROM evl_revsync_settings where is_deleted = 0 and id = ".$dbid;
}
$resultsettings = mysql_query($sqlsettings);

$did = 0;
$SQL_SERVER = '';
$SQL_PORT = 0;
$SQL_DB = '';
$SQL_USER = '';
$SQL_PASS = '';

while ($rowsettings = mysql_fetch_assoc($resultsettings)) {
    $did = $rowsettings['id'];
    $SQL_SERVER = $rowsettings['sqlserver_host'];
    $SQL_PORT = $rowsettings['sqlserver_port'];
    $SQL_DB = $rowsettings['sqlserver_db'];
    $SQL_USER = $rowsettings['sqlserver_user'];
    $SQL_PASS = $rowsettings['sqlserver_pass'];
    
    fwrite($lg, "INFO: ITERATING FOR DBID: ".$did." Server ".$SQL_SERVER." DB ".$SQL_DB." USER ".$SQL_USER. " Pass ".$SQL_PASS."\n");
  
//calculate run number
$sqlmisc = "SELECT MAX(runno) as mx FROM evl_revsync";
$resultmisc = mysql_query($sqlmisc);
$maxrun = 0;

if ($rowmisc = mysql_fetch_assoc($resultmisc)) {
    $maxrun = $rowmisc['mx'];
}
$maxrun++;

$dbhandle = mssql_connect($SQL_SERVER.":".$SQL_PORT, $SQL_USER, $SQL_PASS)
  or die("Couldn't connect to SQL Server on $SQL_SERVER");
$selected = mssql_select_db($SQL_DB, $dbhandle)
  or die("Couldn't open database $SQL_DB");

//get recs from Evolve CRM DB  
$qryMode = "select A.*, CON.ContactName, CON.ContactEmail1 from (
	    select ET.EntityID, ET.TagID, T.InstantCustomerCampaignID_C, 
            ENT.Addressee_C, ENT.ucARLastName_C, ENT.EntityAddress1, ENT.EntityAddress2, ENT.EntityCity, ENT.EntityState, ENT.EntityPostCode, ENT.CountryID,
            ENT.EntityPhone, ENT.EntityEmail, ENT.EntityName, ENT.EntityRecModified, CONVERT(VARCHAR(19), ENT.EntityRecModified, 120) as moddate_proc, ENT.Contact_Person_C
            from RM_EntityTag ET, RM_Tag T, RM_Entity ENT
            where ET.TagID = T.TagID
            and ET.EntityID = ENT.EntityID  and ENT.EntityOkToEmail = 'true') A left join RM_Contact CON
            ON A.EntityID = CON.EntityID";
  
fwrite($lg, "INFO: Query for fetching recs from Evolve: ".$qryMode."\n");
	
$resevl = mssql_query($qryMode);

if($maxrun == 1){ $tempstat = 'INSERT';} else {$tempstat = 'NA';}
//populate into staging table
while ($rowevl = mssql_fetch_assoc($resevl)) {
    //print $rowevl['EntityRecModified'];
    //$tempdate = date('Y-m-d H:i:s', strtotime($rowevl['EntityRecModified']));
    //print $tempdate;
    $qryIns = "insert into evl_revsync_stage(dbid, entid,campid,tagid,runno,status,finstatus,delstatus,icid,u_firstname,u_address,u_address_2,u_city,
    u_state,u_zip,u_country,u_phone,u_email,u_company,moddate, moddate_proc, conname,conemail)
    values ($did, '$rowevl[EntityID]', '$rowevl[InstantCustomerCampaignID_C]', '$rowevl[TagID]', '$maxrun', '$tempstat',
    0, 0, 0, '$rowevl[Contact_Person_C]', '$rowevl[EntityAddress1]', '$rowevl[EntityAddress2]', '$rowevl[EntityCity]',
    '$rowevl[EntityState]', '$rowevl[EntityPostCode]', '$rowevl[CountryID]', '$rowevl[EntityPhone]', '$rowevl[EntityEmail]',
    '$rowevl[EntityName]', '$rowevl[EntityRecModified]', '$rowevl[moddate_proc]','$rowevl[ContactName]', '$rowevl[ContactEmail1]')";
    
    $resultins = mysql_query($qryIns);
    if (!$resultins) {
        fwrite($lg, "INFO: Insertion to staging table failed\n");
    }
} 
mssql_close($dbhandle);

//process records on staging table - mark update, insert, NOCHANGE
if($maxrun > 1){
    //mark recs to update
    $qryudt = "update evl_revsync_stage ST, evl_revsync M
		set ST.status = 'UPDATE'
		where ST.entid = M.entid
		and ST.campid = M.campid
		and ST.dbid = M.dbid
		and ST.finstatus = 0
		and ST.is_deleted = 0
		and ST.moddate_proc >= M.rundt
		and exists ( select 'x' from evl_revsync T where T.entid = ST.entid and T.campid = ST.campid and ST.dbid = M.dbid and T.status = 'INSERT')";
    $resudt = mysql_query($qryudt);
    if (!$resudt) {
        fwrite($lg, "INFO: UPDATE marking on stage failed\n");
    }
    
    //mark nochange recs
    $qryno = "update evl_revsync_stage ST, evl_revsync M
		set ST.status = 'NOCHANGE'
		where ST.entid = M.entid
		and ST.dbid = M.dbid
		and ST.campid = M.campid
		and ST.finstatus = 0
		and ST.is_deleted = 0
		and ST.moddate_proc < M.rundt
		and exists ( select 'x' from evl_revsync T where T.entid = ST.entid and T.campid = ST.campid and ST.dbid = M.dbid and T.status = 'INSERT')";
    $resno = mysql_query($qryno);
    if (!$resno) {
        fwrite($lg, "INFO: NOCHANGE marking on stage failed\n");
    }
    
    //mark insert recs
    $qryin = "update evl_revsync_stage
		set status = 'INSERT'
		where status not in ('UPDATE' , 'NOCHANGE')
		and is_deleted = 0
		and finstatus = 0";
    $resin = mysql_query($qryin);
    if (!$resin) {
        fwrite($lg, "INFO: INSERT marking on stage failed\n");
    }
    
    //mark delete recs from main table
    $qrydel = "update evl_revsync M
		set M.status = 'DELETE'
		where M.delstatus = 0
		and M.status = 'INSERT'
		and not exists (
		  select 'x' from evl_revsync_stage ST
		  where ST.entid = M.entid
		  and ST.campid = M.campid
		  and ST.dbid = M.dbid
		  and ST.is_deleted = 0
		) and M.dbid = ".$did;
    $resdel = mysql_query($qrydel);
    if (!$resdel) {
        fwrite($lg, "INFO: DELETE marking on main tbl failed\n");
    }
    
}

//perform insert, update, delete operations and update status
//start with INSERT from staging table
$qrystage = "SELECT ST.*, M.apkey from evl_revsync_stage ST, evl_dbmappings M
		where ST.campid = M.campaign_id and ST.finstatus = 0 and ST.status = 'INSERT' and ST.is_deleted = 0";
$resstage = mysql_query($qrystage);


//"insert into evl_revsync(entid,campid,tagid,runno,status,finstatus,delstatus,icid,u_firstname,u_address,u_address_2,u_city,
//    u_state,u_zip,u_country,u_phone,u_email,u_company,moddate, moddate_proc, conname,conemail)
//    values ('$rowevl[EntityID]', '$rowevl[InstantCustomerCampaignID_C]', '$rowevl[TagID]', '$maxrun', '$tempstat',
//    0, 0, 0, '$rowevl[Contact_Person_C]', '$rowevl[EntityAddress1]', '$rowevl[EntityAddress2]', '$rowevl[EntityCity]',
//    '$rowevl[EntityState]', '$rowevl[EntityPostCode]', '$rowevl[CountryID]', '$rowevl[EntityPhone]', '$rowevl[EntityEmail]',
//    '$rowevl[EntityName]', '$rowevl[EntityRecModified]', '$tempdate','$rowevl[ContactName]', '$rowevl[ContactEmail1]')";

 $ch = curl_init();
 
while ($rowstage = mysql_fetch_assoc($resstage)){    
    $fields = array('req' => 'api', 'mode' => 'add_subscriber');
    $fields['api_key'] = $rowstage['apkey'];
    $fields['campaign_id'] = $rowstage['campid'];
    if(strlen($rowstage['u_firstname']) > 0){
	$fields['u_firstname'] = preparedata($rowstage['u_firstname']);
    }
    if(strlen($rowstage['u_address']) > 0){
	$fields['u_address'] = preparedata($rowstage['u_address']);
    }
    if(strlen($rowstage['u_address_2']) > 0){
	$fields['u_address_2'] = preparedata($rowstage['u_address_2']);
    }
    if(strlen($rowstage['u_city']) > 0){
	$fields['u_city'] = preparedata($rowstage['u_city']);
    }
    if(strlen($rowstage['u_state']) > 0){
	$fields['u_state'] = preparedata($rowstage['u_state']);
    }
    if(strlen($rowstage['u_zip']) > 0){
	$fields['u_zip'] = preparedata($rowstage['u_zip']);
    }
    if(strlen($rowstage['u_country']) > 0){
	$fields['u_country'] = preparedata($rowstage['u_country']);
    }
    if(strlen($rowstage['u_phone']) > 0){
	$fields['u_phone'] = preparedata($rowstage['u_phone']);
    }
    if(strlen($rowstage['u_email']) > 0){
	$fields['u_email'] = preparedata($rowstage['u_email']);
    }
    if(strlen($rowstage['u_company']) > 0){
	$fields['u_company'] = preparedata($rowstage['u_company']);
    }
    if(strlen($rowstage['conname']) > 0){
	$fields['u_notes_1'] = preparedata($rowstage['conname']);
    }
    if(strlen($rowstage['conemail']) > 0){
	$fields['u_notes_2'] = preparedata($rowstage['conemail']);
    }

    $fields_string = "";	
	
    //url-ify the data for the POST
    foreach($fields as $key=>$value) { $fields_string .= $key.'='.$value.'&'; }
    
    $fields_string = rtrim($fields_string, "&");
    
    fwrite($lg, "INFO: POST URL".$APIURL."/?".$fields_string."\n");
    
   
    
    curl_setopt_array($ch, array(
	CURLOPT_HTTPHEADER => array("Content-Type: text/xml"),
	CURLOPT_HEADER => 0,
	CURLOPT_TIMEOUT => 30,
	CURLOPT_FOLLOWLOCATION => 1,
	CURLOPT_RETURNTRANSFER => 1,
	CURLOPT_URL => $APIURL."/?".$fields_string
    ));

    //execute post
    $resp = curl_exec($ch);
    
    //var_dump($resp);
    //print "CURL_ERROR: ". curl_error($ch);
    
    $xmlins = simplexml_load_string($resp);
    
    $success = 0;
    $icid = 0;
    $message = '';
    
    foreach($xmlins as $k => $v){
	if($k == 'success'){
	    $success = $v;
	}
	if($k == 'new_id'){
	    $icid = $v;
	}
	if($k == 'message'){
	    $message = $v;
	}
     }
     
     if($success == 1){
     //update status and icid
	$qryudtstat = "update evl_revsync_stage set finstatus = 1, icid = ".$icid." where id = ".$rowstage['id'];
	$resudtstat = mysql_query($qryudtstat);
	
	if(!$resudtstat){
	    fwrite($lg, "INFO: Status update for INSERT in Stage table failed\n");
	}
	
     }else{
	fwrite($lg, "INFO: API call failed".$message."\n");
     }
    
}
 curl_close($ch);


//process UPDATE from staging table
$qrystageudt = "SELECT ST.*, M.apkey, OLD.icid from evl_revsync_stage ST, evl_dbmappings M, evl_revsync OLD
		where ST.campid = M.campaign_id and ST.finstatus = 0 and ST.status = 'UPDATE' and ST.entid = OLD.entid and ST.campid = OLD.campid and ST.dbid = OLD.dbid and OLD.status = 'INSERT' and ST.is_deleted = 0";
$resstageudt = mysql_query($qrystageudt);


//"insert into evl_revsync(entid,campid,tagid,runno,status,finstatus,delstatus,icid,u_firstname,u_address,u_address_2,u_city,
//    u_state,u_zip,u_country,u_phone,u_email,u_company,moddate, moddate_proc, conname,conemail)
//    values ('$rowevl[EntityID]', '$rowevl[InstantCustomerCampaignID_C]', '$rowevl[TagID]', '$maxrun', '$tempstat',
//    0, 0, 0, '$rowevl[Contact_Person_C]', '$rowevl[EntityAddress1]', '$rowevl[EntityAddress2]', '$rowevl[EntityCity]',
//    '$rowevl[EntityState]', '$rowevl[EntityPostCode]', '$rowevl[CountryID]', '$rowevl[EntityPhone]', '$rowevl[EntityEmail]',
//    '$rowevl[EntityName]', '$rowevl[EntityRecModified]', '$tempdate','$rowevl[ContactName]', '$rowevl[ContactEmail1]')";
$chudt = curl_init();

while ($rowstageudt = mysql_fetch_assoc($resstageudt)){    
    $fieldsudt = array('req' => 'api', 'mode' => 'edit_subscriber');
    $fieldsudt['id'] = $rowstageudt['icid'];
    $fieldsudt['api_key'] = $rowstageudt['apkey'];
    $fieldsudt['campaign_id'] = $rowstageudt['campid'];
    if(strlen($rowstageudt['u_firstname']) > 0){
	$fieldsudt['u_firstname'] = preparedata($rowstageudt['u_firstname']);
    }
    if(strlen($rowstageudt['u_address']) > 0){
	$fieldsudt['u_address'] = preparedata($rowstageudt['u_address']);
    }
    if(strlen($rowstageudt['u_address_2']) > 0){
	$fieldsudt['u_address_2'] = preparedata($rowstageudt['u_address_2']);
    }
    if(strlen($rowstageudt['u_city']) > 0){
	$fieldsudt['u_city'] = preparedata($rowstageudt['u_city']);
    }
    if(strlen($rowstageudt['u_state']) > 0){
	$fieldsudt['u_state'] = preparedata($rowstageudt['u_state']);
    }
    if(strlen($rowstageudt['u_zip']) > 0){
	$fieldsudt['u_zip'] = preparedata($rowstageudt['u_zip']);
    }
    if(strlen($rowstageudt['u_country']) > 0){
	$fieldsudt['u_country'] = preparedata($rowstageudt['u_country']);
    }
    if(strlen($rowstageudt['u_phone']) > 0){
	$fieldsudt['u_phone'] = preparedata($rowstageudt['u_phone']);
    }
    if(strlen($rowstageudt['u_email']) > 0){
	$fieldsudt['u_email'] = preparedata($rowstageudt['u_email']);
    }
    if(strlen($rowstageudt['u_company']) > 0){
	$fieldsudt['u_company'] = preparedata($rowstageudt['u_company']);
    }
    if(strlen($rowstageudt['conname']) > 0){
	$fieldsudt['u_notes_1'] = preparedata($rowstageudt['conname']);
    }
    if(strlen($rowstageudt['conemail']) > 0){
	$fieldsudt['u_notes_2'] = preparedata($rowstageudt['conemail']);
    }

    $fieldsudt_string = "";	
	
    //url-ify the data for the POST
    foreach($fieldsudt as $key=>$value) { $fieldsudt_string .= $key.'='.$value.'&'; }
    
    $fieldsudt_string = rtrim($fieldsudt_string, "&");
    
    fwrite($lg, "INFO: POST URL".$APIURL."/?".$fieldsudt_string."\n");
    
    curl_setopt_array($chudt, array(
	CURLOPT_HTTPHEADER => array("Content-Type: text/xml"),
	CURLOPT_HEADER => 0,
	CURLOPT_TIMEOUT => 30,
	CURLOPT_FOLLOWLOCATION => 1,
	CURLOPT_RETURNTRANSFER => 1,
	CURLOPT_URL => $APIURL."/?".$fieldsudt_string
    ));

    //execute post
    $respudt = curl_exec($chudt);
        
    $xmludt = simplexml_load_string($respudt);
    
    $successudt = 0;
    $icidudt = 0;
    $messageudt = '';
    
    foreach($xmludt as $k => $v){
	if($k == 'success'){
	    $successudt = $v;
	}
	if($k == 'new_id'){
	    $icidudt = $v;
	}
	if($k == 'message'){
	    $messageudt = $v;
	}
     }
     
     if($successudt == 1){
     //update status and icid
	$qryudtstatudt = "update evl_revsync_stage set finstatus = 1, icid = ".$rowstageudt['icid']." where id = ".$rowstageudt['id'];
	$resudtstatudt = mysql_query($qryudtstatudt);
	
	if(!$resudtstatudt){
	    fwrite($lg, "INFO: Status update for UPDATE in Stage table failed\n");
	}
	
     }else{
	fwrite($lg, "INFO: API call failed".$messageudt."\n");
     }
    
}
curl_close($chudt);

//process DELETE from staging table
$qrydel = "select M.campid, M.icid, M.id, T.apkey from evl_revsync M, evl_dbmappings T where M.status = 'DELETE' and M.delstatus = 0 and M.campid = T.campaign_id and M.dbid =".$did;
$resdel = mysql_query($qrydel);

$chdel = curl_init();

while ($rowdel = mysql_fetch_assoc($resdel)){    
    $fieldsdel = array('req' => 'api', 'mode' => 'delete_subscriber');
    $fieldsdel['id'] = $rowdel['icid'];
    $fieldsdel['api_key'] = $rowdel['apkey'];
    $fieldsdel['campaign_id'] = $rowdel['campid'];   

    $fieldsdel_string = "";	
	
    //url-ify the data for the POST
    foreach($fieldsdel as $key=>$value) { $fieldsdel_string .= $key.'='.$value.'&'; }
    
    $fieldsdel_string = rtrim($fieldsdel_string, "&");
    
    fwrite($lg, "INFO: POST URL".$APIURL."/?".$fieldsdel_string."\n");
    
    curl_setopt_array($chdel, array(
	CURLOPT_HTTPHEADER => array("Content-Type: text/xml"),
	CURLOPT_HEADER => 0,
	CURLOPT_TIMEOUT => 30,
	CURLOPT_FOLLOWLOCATION => 1,
	CURLOPT_RETURNTRANSFER => 1,
	CURLOPT_URL => $APIURL."/?".$fieldsdel_string
    ));

    //execute post
    $respdel = curl_exec($chdel);
        
    $xmldel = simplexml_load_string($respdel);
    
    $successdel = 0;
    $iciddel = 0;
    $messagedel = '';
    
    foreach($xmldel as $k => $v){
	if($k == 'success'){
	    $successdel = $v;
	}
	if($k == 'new_id'){
	    $iciddel = $v;
	}
	if($k == 'message'){
	    $messagedel = $v;
	}
     }
     
     if($successdel == 1){
     //update status and icid
	$qryudtstatdel = "update evl_revsync set delstatus = 1 where id = ".$rowdel['id'];
	$resudtstatdel = mysql_query($qryudtstatdel);
	
	if(!$resudtstatdel){
	    fwrite($lg, "INFO: Status update for DELETE in main table failed\n");
	}
	
     }else{
	fwrite($lg, "INFO: API call failed".$messagedel."\n");
     }
    
}
curl_close($chdel);

//move recs from stage to main tbl
$qryTrf = "insert into evl_revsync(dbid, entid,campid,tagid,runno,rundt, status,finstatus,delstatus,icid,u_firstname,u_address,u_address_2,u_city,
    u_state,u_zip,u_country,u_phone,u_email,u_company,moddate, moddate_proc, conname,conemail) select dbid, entid,campid,tagid,runno,rundt, status,finstatus,delstatus,icid,u_firstname,u_address,u_address_2,u_city,
    u_state,u_zip,u_country,u_phone,u_email,u_company,moddate, moddate_proc, conname,conemail from evl_revsync_stage where is_deleted = 0";
$resTrf = mysql_query($qryTrf);

if(!$resTrf){
    fwrite($lg, "INFO: Trnsfer from stage to main tbl failed\n");
}

//truncate stage table
$qryEmp = "update evl_revsync_stage set is_deleted = 1 where dbid=".$did;
$resEmp = mysql_query($qryEmp);

if(!$resEmp){
    fwrite($lg, "INFO: Truncation of stage tbl failed\n");
}

print "SYNC SUCCESS";

}

mysql_close($mydbhandle);
fclose($lg);
}

?>