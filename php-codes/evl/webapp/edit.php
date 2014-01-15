<?php 
ob_start();
session_start();?>

<?php 
if(isset($_SESSION['username'])){
			
include 'include/header.php';

// creates the edit record form
function renderForm($id, $campaign_name,$campaign_id,$dbserver,$dbport,$dbname,$dbuser,$dbpass,$modified_by,$currency_id,$customertypeid,
	$entitytypeid,$entitycreateduserid,$entitymodifieduserid,$countryid,$entityisactive,$sourceid,$entityoktocall,$entityoktofax,$entityoktomail,$entityresstatus,
	$ucarreseller_c,$acttypeid,$actstatusid,$actcategoryid,$incidenttypeid_c,$activityresstatus,$activitycreateduserid,$activitymodifieduserid,$userid,$codedigits, $custom1, $custom2, $custom3, $custom4, $custom5, $mailerflag, $smtpserver, $smtpport, $smtpuser, $smtppass, $fromemail, $fromname, $toemail, $ccemail, $subject, $error)	 

 {
 ?>
 
 <!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application :: Edit DB Default and Mailer Settings</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
       <h3 id = 'misc'>Edit DB and default field values for campaign</h3>
 	<?php
 		$u_id=$_SESSION['u_id'];
	?>
	
 <?php
  
 // if there are any errors, display them
 if ($error != '')
	 {
	 echo '<div style="padding:4px; border:1px solid red; color:red;">'.$error.'</div>';
	 }
 ?> 
 
	<form action="" method="post">
	      <table>
	 <input type="hidden" name="id" value="<?php echo $id; ?>"/>
		 <tr><td class = 'lbl'>Campaign Name: </td><td><?php echo $campaign_name; ?></td></tr>
		 <tr><td class = 'lbl'>Campaign ID:</td><td> <?php echo $campaign_id; ?></td></tr>
		 <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
		 <tr><td class = 'lbl'>Db Server:</td><td> <input type="text" name="dbserver" value="<?php echo $dbserver; ?>"/><i>SQLServer DB host</i></td></tr>
		 <tr><td class = 'lbl'>Db Port: </td><td> <input type="text" name="dbport" value="<?php echo $dbport; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Db Name: </td><td> <input type="text" name="dbname" value="<?php echo $dbname; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Db User: </td><td> <input type="text" name="dbuser" value="<?php echo $dbuser; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Db Pass: </td><td> <input type="text" name="dbpass" value="<?php echo $dbpass; ?>"/><i>SQLServer DB password</i></td></tr>
		 <tr><td>&nbsp;</td><td><input type="hidden" name="modified_by" readonly="true" value="<?php echo $u_id; ?>"/></td></tr>
	         <tr><td>&nbsp;</td><td><i>Default values for RM_Entity table</i></td></tr>
		 <tr><td class = 'lbl'>Currency ID: </td><td> <input type="text" name="CurrencyID" value="<?php echo $currency_id; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Customer type ID: </td><td> <input type="text" name="CustomerTypeID" value="<?php echo $customertypeid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Entity Type ID: </td><td> <input type="text" name="EntityTypeID" value="<?php echo $entitytypeid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Entity Created User ID: </td><td> <input type="text" name="EntityCreatedUserID" value="<?php echo $entitycreateduserid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Entity Modified User ID: </td><td> <input type="text" name="EntityModifiedUserID" value="<?php echo $entitymodifieduserid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Country ID: </td><td> <input type="text" name="CountryID" value="<?php echo $countryid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Entity IsActive: </td><td> <input type="text" name="EntityIsActive" value="<?php echo $entityisactive; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Source ID: </td><td> <input type="text" name="SourceID" value="<?php echo $sourceid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>EntityOkToCall: </td><td> <input type="text" name="EntityOkToCall" value="<?php echo $entityoktocall; ?>"/></td></tr>
		 <tr><td class = 'lbl'>EntityOkToFax: </td><td> <input type="text" name="EntityOkToFax" value="<?php echo $entityoktofax; ?>"/></td></tr>
		 <tr><td class = 'lbl'>EntityOkToEmail: </td><td> <input type="text" name="EntityOkToEmail" value="<?php echo $entityoktomail; ?>"/></td></tr>
		 <tr><td class = 'lbl'>EntityRecStatus: </td><td> <input type="text" name="EntityRecStatus" value="<?php echo $entityresstatus; ?>"/></td></tr>
		 <tr><td class = 'lbl'>UCArReseller_C: </td><td> <input type="text" name="ucARReseller_C" value="<?php echo $ucarreseller_c; ?>"/></td></tr>
		 <tr><td>&nbsp;</td><td><i>Default values for RM_Activity table</i></td></tr>
		 <tr><td class = 'lbl'>ActType ID: </td><td> <input type="text" name="ActTypeID" value="<?php echo $acttypeid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Act Status ID: </td><td> <input type="text" name="ActStatusID" value="<?php echo $actstatusid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>Act Category ID: </td><td> <input type="text" name="ActCategoryID" value="<?php echo $actcategoryid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>IncidentType ID: </td><td> <input type="text" name="iIncidentTypeID_C" value="<?php echo $incidenttypeid_c; ?>"/></td></tr>
		 <tr><td class = 'lbl'>ActivityRecStatus: </td><td> <input type="text" name="ActivityRecStatus" value="<?php echo $activityresstatus; ?>"/></td></tr>
		 <tr><td class = 'lbl'>ActivityCreatedUserID: </td><td> <input type="text" name="ActivityCreatedUserID" value="<?php echo $activitycreateduserid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>ActivityModifiedUserID: </td><td> <input type="text" name="ActivityModifiedUserID" value="<?php echo $activitymodifieduserid; ?>"/></td></tr>
		 <tr><td class = 'lbl'>UserID: </td><td> <input type="text" name="userid" value="<?php echo $userid; ?>"/></td></tr>
		 <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
		 <tr><td class = 'lbl'>Code Digits: </td><td> <input type="text" name="codedigits" value="<?php echo $codedigits; ?>"/><i>No of digits in EntityCode, ActivityCode</i></td></tr>
		 <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
		 <tr><td class = 'lbl'>Custom1: </td><td> <input type="text" name="custom1" value="<?php echo $custom1; ?>"/><i>Mapping of custom field from IC to Evolve CRM</i></td></tr>
		 <tr><td class = 'lbl'>Custom2: </td><td> <input type="text" name="custom2" value="<?php echo $custom2; ?>"/><i>Mapping of custom field from IC to Evolve CRM</i></td></tr>
		 <tr><td class = 'lbl'>Custom3: </td><td> <input type="text" name="custom3" value="<?php echo $custom3; ?>"/><i>Mapping of custom field from IC to Evolve CRM</i></td></tr>
		 <tr><td class = 'lbl'>Custom4: </td><td> <input type="text" name="custom4" value="<?php echo $custom4; ?>"/><i>Mapping of custom field from IC to Evolve CRM</i></td></tr>
		 <tr><td class = 'lbl'>Custom5: </td><td> <input type="text" name="custom5" value="<?php echo $custom5; ?>"/><i>Mapping of custom field from IC to Evolve CRM</i></td></tr>
		 <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
		 <tr><td class = 'lbl'>MAILER_FLAG: </td><td> <input type="text" name="mailerflag" value="<?php echo $mailerflag; ?>"/><i>Set to 1 to enable mail alerts, 0 to disable.</i></td></tr>
		 <tr><td class = 'lbl'>SMTP_SERVER: </td><td> <input type="text" name="smtpserver" value="<?php echo $smtpserver; ?>"/></td></tr>
		 <tr><td class = 'lbl'>SMTP_PORT: </td><td> <input type="text" name="smtpport" value="<?php echo $smtpport; ?>"/></td></tr>
		 <tr><td class = 'lbl'>SMTP_USER: </td><td> <input type="text" name="smtpuser" value="<?php echo $smtpuser; ?>"/></td></tr>
		 <tr><td class = 'lbl'>SMTP_PASS: </td><td> <input type="text" name="smtppass" value="<?php echo $smtppass; ?>"/><i>SMTP server password</i></td></tr>
		 <tr><td class = 'lbl'>FROM_EMAIL: </td><td> <input type="text" name="fromemail" value="<?php echo $fromemail; ?>"/></td></tr>
		 <tr><td class = 'lbl'>FROM_NAME: </td><td> <input type="text" name="fromname" value="<?php echo $fromname; ?>"/></td></tr>
		 <tr><td class = 'lbl'>TO_EMAIL: </td><td> <input type="text" name="toemail" value="<?php echo $toemail; ?>"/><i>Email address where alerts are sent</i></td></tr>
		 <tr><td class = 'lbl'>CC_EMAIL: </td><td> <input type="text" name="ccemail" value="<?php echo $ccemail; ?>"/></td></tr>
		 <tr><td class = 'lbl'>SUBJECT: </td><td> <input type="text" name="subject" value="<?php echo $subject; ?>"/><i>Subject of email</i></td></tr>
		 <tr><td>&nbsp;</td><td><input type="submit" name="submit" value="Save"></td></tr>
			
	      </table>
 		</form> 
 	</body>
 </html>  
 <?php
 }

 // connect to the database
 include 'core/db_connect.php';
 
 // check if the form has been submitted. 
 if (isset($_POST['submit']))
 { 
 
	 if (is_numeric($_POST['id']))
	 {
	 // get form data, making sure it is valid
	 $id = $_POST['id'];
	 $campaign_name = mysql_real_escape_string(htmlspecialchars($_POST['campaign_name']));
	 $campaign_id = mysql_real_escape_string(htmlspecialchars($_POST['campaign_id']));
	 $dbserver = mysql_real_escape_string(htmlspecialchars($_POST['dbserver']));
	 $dbport = mysql_real_escape_string(htmlspecialchars($_POST['dbport']));
	 $dbname = mysql_real_escape_string(htmlspecialchars($_POST['dbname']));
	 $dbuser = mysql_real_escape_string(htmlspecialchars($_POST['dbuser']));
	 $dbpass = mysql_real_escape_string(htmlspecialchars($_POST['dbpass']));
	 //$modified_date = mysql_real_escape_string(htmlspecialchars($_POST['modified_date']));
	 $modified_by = mysql_real_escape_string(htmlspecialchars($_POST['modified_by']));
	 $currency_id = mysql_real_escape_string(htmlspecialchars($_POST['CurrencyID']));
	 $customertypeid = mysql_real_escape_string(htmlspecialchars($_POST['CustomerTypeID']));
	 $entitytypeid = mysql_real_escape_string(htmlspecialchars($_POST['EntityTypeID']));
	 $entitycreateduserid = mysql_real_escape_string(htmlspecialchars($_POST['EntityCreatedUserID']));
	 $entitymodifieduserid = mysql_real_escape_string(htmlspecialchars($_POST['EntityModifiedUserID']));
	 $countryid = mysql_real_escape_string(htmlspecialchars($_POST['CountryID']));
	 $entityisactive = mysql_real_escape_string(htmlspecialchars($_POST['EntityIsActive']));
	 $sourceid = mysql_real_escape_string(htmlspecialchars($_POST['SourceID']));
	 $entityoktocall = mysql_real_escape_string(htmlspecialchars($_POST['EntityOkToCall']));
	 $entityoktofax = mysql_real_escape_string(htmlspecialchars($_POST['EntityOkToFax']));
	 $entityoktomail = mysql_real_escape_string(htmlspecialchars($_POST['EntityOkToEmail']));
	 $entityresstatus = mysql_real_escape_string(htmlspecialchars($_POST['EntityRecStatus']));
	 $ucarreseller_c = mysql_real_escape_string(htmlspecialchars($_POST['ucARReseller_C']));
	 $acttypeid = mysql_real_escape_string(htmlspecialchars($_POST['ActTypeID']));
	 $actstatusid = mysql_real_escape_string(htmlspecialchars($_POST['ActStatusID']));
	 $actcategoryid = mysql_real_escape_string(htmlspecialchars($_POST['ActCategoryID']));
	 $incidenttypeid_c = mysql_real_escape_string(htmlspecialchars($_POST['iIncidentTypeID_C']));
	 $activityresstatus = mysql_real_escape_string(htmlspecialchars($_POST['ActivityRecStatus']));
	 $activitycreateduserid = mysql_real_escape_string(htmlspecialchars($_POST['ActivityCreatedUserID']));
	 $activitymodifieduserid = mysql_real_escape_string(htmlspecialchars($_POST['ActivityModifiedUserID']));
	 $userid = mysql_real_escape_string(htmlspecialchars($_POST['userid']));
	 $codedigits = mysql_real_escape_string(htmlspecialchars($_POST['codedigits']));
	 $custom1 = mysql_real_escape_string(htmlspecialchars($_POST['custom1']));
	 $custom2 = mysql_real_escape_string(htmlspecialchars($_POST['custom2']));
	 $custom3 = mysql_real_escape_string(htmlspecialchars($_POST['custom3']));
	 $custom4 = mysql_real_escape_string(htmlspecialchars($_POST['custom4']));
	 $custom5 = mysql_real_escape_string(htmlspecialchars($_POST['custom5']));
	 $mailerflag = mysql_real_escape_string(htmlspecialchars($_POST['mailerflag']));
	 $smtpserver = mysql_real_escape_string(htmlspecialchars($_POST['smtpserver']));
	 $smtpport = mysql_real_escape_string(htmlspecialchars($_POST['smtpport']));
	 $smtpuser = mysql_real_escape_string(htmlspecialchars($_POST['smtpuser']));
	 $smtppass = mysql_real_escape_string(htmlspecialchars($_POST['smtppass']));
	 $fromemail = mysql_real_escape_string(htmlspecialchars($_POST['fromemail']));
	 $fromname = mysql_real_escape_string(htmlspecialchars($_POST['fromname']));
	 $toemail = mysql_real_escape_string(htmlspecialchars($_POST['toemail']));
	 $ccemail = mysql_real_escape_string(htmlspecialchars($_POST['ccemail']));
	 $subject = mysql_real_escape_string(htmlspecialchars($_POST['subject']));
	 
	 //$is_deleted = mysql_real_escape_string(htmlspecialchars($_POST['is_deleted']));
	 
	 // check that fields are  filled in
	 if ($dbserver == '' )
	 {
	 
	 $error = 'ERROR: Please fill in all required fields!';
	 
	 
	 renderForm($id, $campaign_name,$campaign_id,$dbserver,$dbport,$dbname,$dbuser,$dbpass,$modified_by,$currency_id,$customertypeid,$entitytypeid,$entitycreateduserid,$entitymodifieduserid,$countryid,$entityisactive,$sourceid,
	$entityoktocall,$entityoktofax,$entityoktomail,$entityresstatus,$ucarreseller_c,$acttypeid,$actstatusid,$actcategoryid,$incidenttypeid_c,$activityresstatus,$activitycreateduserid,$activitymodifieduserid,$userid, $codedigits, $custom1, $custom2, $custom3, $custom4, $custom5, $mailerflag, $smtpserver, $smtpport, $smtpuser, $smtppass, $fromemail, $fromname, $toemail, $ccemail, $subject, $error);

 
	
	 }
 else
 {
	 //update the data to the database
	 mysql_query("UPDATE evl_dbmappings SET dbserver='$dbserver',dbport='$dbport',dbname='$dbname',dbuser='$dbuser',dbpass='$dbpass',modified_by='$modified_by',CurrencyID='$currency_id',CustomerTypeID='$customertypeid',
	 EntityTypeID='$entitytypeid',EntityCreatedUserID='$entitycreateduserid',EntityModifiedUserID='$entitymodifieduserid',CountryID='$countryid',EntityIsActive='$entityisactive',SourceID='$sourceid',
	 EntityOkToCall='$entityoktocall',EntityOkToFax='$entityoktofax',EntityOkToEmail='$entityoktomail',EntityRecStatus='$entityresstatus',ucARReseller_C='$ucarreseller_c',ActTypeID='$acttypeid',
	 ActStatusID='$actstatusid',ActCategoryID='$actcategoryid',iIncidentTypeID_C='$incidenttypeid_c',ActivityRecStatus='$activityresstatus',ActivityCreatedUserID='$activitycreateduserid',ActivityModifiedUserID='$activitymodifieduserid',UserID='$userid', u_custom_1='$custom1', u_custom_2='$custom2',u_custom_3='$custom3',u_custom_4='$custom4',u_custom_5='$custom5',codedigits='$codedigits', MAILER_FLAG='$mailerflag',SMTP_SERVER='$smtpserver',SMTP_PORT='$smtpport', SMTP_USER = '$smtpuser', SMTP_PASS = '$smtppass', FROM_EMAIL = '$fromemail',  FROM_NAME = '$fromname',TO_EMAIL = '$toemail', CC_EMAIL = '$ccemail' , SUBJECT = '$subject'  WHERE id='$id'") or die(mysql_error()); 	 
	 //display dashbaord page to show all contents
	 header('Location:dashboard.php'); 
	 }
 }
 else
 {
	 // if the 'id' isn't valid
	 echo 'Error!';
 }

}
 
 else
	 // if the form hasn't been submitted
	 {
	 
	 // get the 'id' value from the URL.
	 if (isset($_GET['id']) && is_numeric($_GET['id']) && $_GET['id'] > 0)
	 {
	 	// query database
		 $id = $_GET['id'];
		 $result = mysql_query("SELECT * FROM evl_dbmappings WHERE id=$id") or die(mysql_error()); 
		 $row = mysql_fetch_array($result);
		 
		 // check that the 'id' matches up with a row in the databse
		 
		 if($row)
		 {
		 
			 // get data from database
			 $campaign_name = $row['campaign_name'];
			 $campaign_id=$row['campaign_id'];
			 $dbserver = $row['dbserver'];
			 $dbport = $row['dbport'];
			 $dbname = $row['dbname'];
			 $dbuser = $row['dbuser'];
			 $dbpass = $row['dbpass'];
			 $modified_by = $row['modified_by'];
			 $currency_id = $row['CurrencyID'];
			 $customertypeid = $row['CustomerTypeID'];
			 $entitytypeid = $row['EntityTypeID'];
			 $entitycreateduserid = $row['EntityCreatedUserID'];
			 $entitymodifieduserid = $row['EntityModifiedUserID'];
			 $countryid = $row['CountryID'];
			 $entityisactive = $row['EntityIsActive'];
			 $sourceid = $row['SourceID'];
			 $entityoktocall = $row['EntityOkToCall'];
			 $entityoktofax = $row['EntityOkToFax'];
			 $entityoktomail = $row['EntityOkToEmail'];
			 $entityresstatus = $row['EntityRecStatus'];
			 $ucarreseller_c = $row['ucARReseller_C'];
			 $acttypeid = $row['ActTypeID'];
			 $actstatusid = $row['ActStatusID'];
			 $actcategoryid = $row['ActCategoryID'];
			 $incidenttypeid_c = $row['iIncidentTypeID_C'];
			 $activityresstatus = $row['ActivityRecStatus'];
			 $activitycreateduserid = $row['ActivityCreatedUserID'];
			 $activitymodifieduserid = $row['ActivityModifiedUserID'];
			 $userid = $row['UserID'];
			 $codedigits = $row['codedigits'];
			 $custom1 = $row['u_custom_1'];
			 $custom2 = $row['u_custom_2'];
			 $custom3 = $row['u_custom_3'];
			 $custom4 = $row['u_custom_4'];
			 $custom5 = $row['u_custom_5'];
			 $mailerflag = $row['MAILER_FLAG'];
			 $smtpserver = $row['SMTP_SERVER'];
			 $smtpport = $row['SMTP_PORT'];
			 $smtpuser = $row['SMTP_USER'];
			 $smtppass = $row['SMTP_PASS'];
			 $fromemail = $row['FROM_EMAIL'];
			 $fromname = $row['FROM_NAME'];
			 $toemail = $row['TO_EMAIL'];
			 $ccemail = $row['CC_EMAIL'];
			 $subject = $row['SUBJECT'];
		
			 
	 // show form
	 renderForm($id, $campaign_name,$campaign_id,$dbserver,$dbport,$dbname,$dbuser,$dbpass,$modified_by,$currency_id,$customertypeid,
	$entitytypeid,$entitycreateduserid,$entitymodifieduserid,$countryid,$entityisactive,$sourceid,
	$entityoktocall,$entityoktofax,$entityoktomail,$entityresstatus,$ucarreseller_c,$acttypeid,
	$actstatusid,$actcategoryid,$incidenttypeid_c,$activityresstatus,$activitycreateduserid,$activitymodifieduserid, $userid,
		$codedigits, $custom1, $custom2, $custom3, $custom4, $custom5, $mailerflag, $smtpserver, $smtpport, $smtpuser, $smtppass, $fromemail, $fromname, $toemail, $ccemail, $subject, '');
	 }
	 else
	 	
		 // if no match, display message
		 {
		 	
		 echo "No results!";
		 
		 }
 }
 else
 
	 // if there is no 'id' value, display an error message
	 {
	 	
	 echo 'Error!';
	 
	 }
 }

}else{
	echo "Please Log In to the System.";
}
?>
</body>
</html>

