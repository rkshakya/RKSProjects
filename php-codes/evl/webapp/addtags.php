<?php 
ob_start();
session_start();

include 'include/header.php';

if(isset($_SESSION['username'])){

    function preparedata($data){
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    $data = urlencode($data);
    return $data;
}

include 'core/db_connect.php';

//fetch dbid, get SQLServer info and open connection
if((isset($_GET['dbid']) && is_numeric($_GET['dbid']) && $_GET['dbid'] > 0) or (isset($_POST['dbid']) && is_numeric($_POST['dbid']) && $_POST['dbid'] > 0)){
    $dbid = 0;
    if (isset($_GET['dbid'])){
	$dbid = $_GET['dbid'];
    }elseif(isset($_POST['dbid'])){
	$dbid = $_POST['dbid'];
    }
    $reshandle = mysql_query("SELECT * FROM evl_revsync_settings WHERE id=$dbid") or die(mysql_error());    
    
    if( $rowhandle = mysql_fetch_array($reshandle)){
	$serverhost = $rowhandle['sqlserver_host'];
	$serverport = $rowhandle['sqlserver_port'];
	$serverdb = $rowhandle['sqlserver_db'];
	$serveruser = $rowhandle['sqlserver_user'];
	$serverpass = $rowhandle['sqlserver_pass'];
    }
    
    //get sqlserver connection
    $dbhandle = mssql_connect($serverhost.":".$serverport, $serveruser, $serverpass)
		or die("Couldn't connect to SQL Server on $serverhost");

    //select a database to work with
    $selected = mssql_select_db($serverdb, $dbhandle)
		or die("Couldn't open database $serverdb");
    
}

if($_SERVER["REQUEST_METHOD"] == "POST"){
    $errMessages = array(); //holds error messages post validation
    $formData = array();
    //put mandatory fields
    $mandatory = array("TagCode" => "TagCode", "TagOrder" => "TagOrder");
    
    foreach($_POST as $field => $value){	
        $formData[$field] = $value;
        
        if ( strlen($value) == 0 && in_array($field, $mandatory) ) {
        $errMessages[$field] = 'Required.';
        }	        
    }    
    
    if(empty($errMessages)){
        //print $formData['fname'];
        //print $formData['lname'];
        //print $formData['phone'];
        //print $formData['email'];
//	insert into RM_Tag(TagCreatedUserID, TagModifiedUserID, TagCode, TagName, TagOrder, TagIsActive , InstantCustomerCampaignID_C)
//values (1, 1,'Tyt',  'test', 0, 'true',  89080)
	
        //on submit - we are clean at this point
        //create client
        if(isset($formData[tid])){
            $sql = "UPDATE RM_Tag SET TagCode = '".$formData[TagCode]."', TagName = '". $formData[TagName]."', TagOrder = '". $formData[TagOrder]."', TagIsActive = '". $formData[TagIsActive]."', InstantCustomerCampaignID_C = '". $formData[InstantCustomerCampaignID_C]."' WHERE TagID = '".$formData[tid]."'";
        }else{
            $sql = "INSERT INTO RM_Tag (TagCreatedUserID, TagModifiedUserID, TagCode, TagName, TagOrder, TagIsActive , InstantCustomerCampaignID_C)VALUES(1, 1, '$formData[TagCode]','$formData[TagName]','$formData[TagOrder]', '$formData[TagIsActive]', '$formData[InstantCustomerCampaignID_C]')";
        }    
        $retval = mssql_query( $sql);
        if(! $retval ){
          die('Could not enter data: ' . mssql_error());
        }
                
        
        if($retval) {
	    header('Location:viewtags.php?id='.$dbid);
	    //http_redirect("viewtags.php", array("id" => $dbid), true, HTTP_REDIRECT_PERM);
        }       
        exit();
        }	
    } else if ($_SERVER["REQUEST_METHOD"] == "GET"){
        if (isset($_GET['tid']) && is_numeric($_GET['tid']) && $_GET['tid'] > 0)
	 {
	 	// query database
		 $tid = $_GET['tid'];
		 $resTag = mssql_query("SELECT * FROM RM_Tag WHERE TagID=$tid") or die(mssql_error()); 
		 $rowTag = mssql_fetch_array($resTag);
		 
		 if($rowTag){
		    //TagCode, TagName, TagOrder, TagIsActive , InstantCustomerCampaignID_C
			 // get data from database
                         $formData['TagID'] = $rowTag['TagID'];
			 $formData['TagCode'] = $rowTag['TagCode'];
			 $formData['TagName'] = $rowTag['TagName'];
                         $formData['TagOrder'] = $rowTag['TagOrder'];
			 $formData['TagIsActive'] = $rowTag['TagIsActive'];
			 $formData['InstantCustomerCampaignID_C'] = $rowTag['InstantCustomerCampaignID_C'];			 
                 }
        }
    }
    
 ?>   
    <html>
<head>
<title>IC Backoffice Application :: Add or edit Tags</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
Please fill up the information below:
<table border = "1">
<form name = "settings" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
<?php if (isset($errMessages)){
//     $formData['TagID'] = $rowTag['TagID'];
//			 $formData['TagCode'] = $rowTag['TagCode'];
//			 $formData['TagName'] = $rowTag['TagName'];
//                         $formData['TagOrder'] = $rowTag['TagOrder'];
//			 $formData['TagIsActive'] = $rowTag['TagIsActive'];
//			 $formData['InstantCustomerCampaignID_C'] = $rowTag['InstantCustomerCampaignID_C'];	
    
    ?>
<tr>
<td align="center"><font color="red" face="Arial, Helvetica, sans-serif" size="+1">Error! Please correct the errors in form fields below.</font></td>
</tr>
<?php } ?>
<tr><td colspan = "2"><i>Fields marked * are mandatory</i></td><?php if(isset($formData[TagID])) {  echo "<td><input type = 'hidden' name = 'tid' value = '$formData[TagID]'/></td>"; } ?><?php if(isset($dbid)) {  echo "<td><input type = 'hidden' name = 'dbid' value = '$dbid'/></td>"; } ?></tr>
<tr><td>Tag Code*</td><td><input type = "text" name="TagCode" id="TagCode" value="<?php echo htmlspecialchars($formData["TagCode"]); ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['TagCode'])) { ?>
<font color="red"><?php echo $errMessages['TagCode']; ?></font>
<?php } ?></td></tr>
<tr><td>Tag Name</td><td><input type = "text" name="TagName" id="TagName" value="<?php if(isset($formData["TagName"])){ echo htmlspecialchars($formData["TagName"]); }  ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['TagName'])) { ?>
<font color="red"><?php echo $errMessages['TagName']; ?></font>
<?php } ?></td></tr>
<tr><td>Tag Order*</td><td><input type = "text" name="TagOrder" id="TagOrder" value="<?php if(isset($formData["TagOrder"])){ echo htmlspecialchars($formData["TagOrder"]); } else { echo '0';}  ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['TagOrder'])) { ?>
<font color="red"><?php echo $errMessages['TagOrder']; ?></font>
<?php } ?></td></tr>
<tr><td>Tag IsActive</td><td><input type="radio" name="TagIsActive" value="true" <?php if($formData[TagIsActive] == 1 or (!isset($formData[TagIsActive])) ) { echo 'checked'; } ?> >Yes<input type="radio" name="TagIsActive" value="false" <?php if($formData[TagIsActive] == '0') { echo 'checked'; } ?>>No</td><td>&nbsp;</td></tr>
<tr><td>Campaign</td><td><select  name="InstantCustomerCampaignID_C" id="InstantCustomerCampaignID_C" />
<option value="" >Select</option>
<?php
    $qryCamp = "select campaign_id, campaign_name from evl_dbmappings" ;
    $resCamp = mysql_query($qryCamp) or die(mysql_error());
    
    while($rowCamp = mysql_fetch_array($resCamp)){
	$campname = $rowCamp['campaign_name'];
	$cid = $rowCamp['campaign_id'];
?>		
	<option value='<?php echo $cid; ?>' <?php if($formData['InstantCustomerCampaignID_C'] == $cid) print 'selected'; ?> ><?php echo $campname.'('.$cid.')'; ?></option>
<?php	
    }
?>
</select></td></tr>
<tr><td>&nbsp;</td><td><input type = "submit" name="submit" value="submit"/></td></tr>
</table>
</form>

<?php

mssql_close($dbhandle);
}else{
	echo "Please Log In to the System.";
}
?>
</body>
</html>