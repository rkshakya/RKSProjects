<?php 
session_start();?>

<!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application :: Home</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
	
    
<?php 
if(isset($_SESSION['username'])){
			
include 'include/header.php';

//connect to database
include 'core/db_connect.php';

//get results from database
$result=mysql_query("SELECT * FROM evl_dbmappings WHERE is_deleted=0") or die(mysql_error());

//display data in table
echo "<h3 id = 'misc'>Campaigns and their DB and default value settings</h3>";
echo "<table border='1' cellpadding='1' width='100%'>";
echo "<tr>
		<th colspan='3'>Actions</th>
		<th>S.No</th>
		<th>Campagin Name</th>
		<th>Campaign ID</th>
		<th>DbServer</th>
		<th>DbPort</th>
		<th>DbName</th>
		<th>DbUser</th>
		<th>DbPass</th>
		<th>&nbsp;</th>
		<th>Currency ID</th>
		<th>CustomerType ID</th>
		<th>EntityType ID</th>
		<th>EntityCreated UserID</th>
		<th>EntityModified UserID</th>
		<th>Country ID</th>
		<th>EntityIsActive</th>
		<th>SourceID</th>
		<th>EntityOkToCall</th>
		<th>EntityOkToFax</th>
		<th>EntityOkToEmail</th>
		<th>EntityRecStatus</th>
		<th>ucARReseller_C</th>
		<th>&nbsp;</th>
		<th>ActTypeID</th>
		<th>ActStatusID</th>
		<th>ActCategoryID</th>
		<th>iINcidentTypeID_C</th>
		<th>ActivityRecStatus</th>
		<th>ActivityCreatedUserID</th>
		<th>ActivityModifiedUserID</th>
		<th>UserID</th>
		<th>codedigits</th>
		<th>Custom1</th>
		<th>Custom2</th>
		<th>Custom3</th>
		<th>Custom4</th>
		<th>Custom5</th>
		<th>&nbsp;</th>
		<th>MAILER_FLAG</th>
		<th>SMTP_SERVER</th>
		<th>SMTP_PORT</th>
		<th>SMTP_USER</th>
		<th>SMTP_PASS</th>
		<th>FROM_EMAIL</th>
		<th>FROM_NAME</th>
		<th>TO_EMAIL</th>
		<th>CC_EMAIL</th>
		<th>SUBJECT</th>
		</tr>";

//loop reults of database query, displaying them in table
$count=0;
while($row=mysql_fetch_array($result)){
	//echo each row into the table
	$count++;
	echo "<tr>";
	echo '<td><a href="edit.php?id=' . $row['id'] . '">Edit</a></td>';
	echo '<td><a href="delete.php?id=' . $row['id'] . '">Delete</a></td>';
	echo '<td><a href="sync.php?campid=' . $row['campaign_id'] . '">Sync Subscriber</a></td>';
	echo '<td>'.$count.'</td>';
	echo '<td>'.$row['campaign_name'].'</td>';
	echo '<td>'.$row['campaign_id'].'</td>';
	echo '<td>'.$row['dbserver'].'</td>';
	echo '<td>'.$row['dbport'].'</td>';
	echo '<td>'.$row['dbname'].'</td>';
	echo '<td>'.$row['dbuser'].'</td>';
	echo '<td>'.$row['dbpass'].'</td>';
	echo '<td>&nbsp;</td>';
	echo '<td>'.$row['CurrencyID'].'</td>';
	echo '<td>'.$row['CustomerTypeID'].'</td>';
	echo '<td>'.$row['EntityTypeID'].'</td>';
	echo '<td>'.$row['EntityCreatedUserID'].'</td>';
	echo '<td>'.$row['EntityModifiedUserID'].'</td>';
	echo '<td>'.$row['CountryID'].'</td>';
	echo '<td>'.$row['EntityIsActive'].'</td>';
	echo '<td>'.$row['SourceID'].'</td>';
	echo '<td>'.$row['EntityOkToCall'].'</td>';
	echo '<td>'.$row['EntityOkToFax'].'</td>';
	echo '<td>'.$row['EntityOkToEmail'].'</td>';
	echo '<td>'.$row['EntityRecStatus'].'</td>';
	echo '<td>'.$row['ucARReseller_C'].'</td>';
	echo '<td>&nbsp;</td>';
	echo '<td>'.$row['ActTypeID'].'</td>';
	echo '<td>'.$row['ActStatusID'].'</td>';
	echo '<td>'.$row['ActCategoryID'].'</td>';
	echo '<td>'.$row['iIncidentTypeID_C'].'</td>';
	echo '<td>'.$row['ActivityRecStatus'].'</td>';
	echo '<td>'.$row['ActivityCreatedUserID'].'</td>';
	echo '<td>'.$row['ActivityModifiedUserID'].'</td>';
	echo '<td>'.$row['UserID'].'</td>';
	echo '<td>'.$row['codedigits'].'</td>';
	echo '<td>'.$row['u_custom_1'].'</td>';
	echo '<td>'.$row['u_custom_2'].'</td>';
	echo '<td>'.$row['u_custom_3'].'</td>';
	echo '<td>'.$row['u_custom_4'].'</td>';
	echo '<td>'.$row['u_custom_5'].'</td>';
	echo '<td>&nbsp;</td>';
	echo '<td>'.$row['MAILER_FLAG'].'</td>';
	echo '<td>'.$row['SMTP_SERVER'].'</td>';
	echo '<td>'.$row['SMTP_PORT'].'</td>';
	echo '<td>'.$row['SMTP_USER'].'</td>';
	echo '<td>'.$row['SMTP_PASS'].'</td>';
	echo '<td>'.$row['FROM_EMAIL'].'</td>';
	echo '<td>'.$row['FROM_NAME'].'</td>';
	echo '<td>'.$row['TO_EMAIL'].'</td>';
	echo '<td>'.$row['CC_EMAIL'].'</td>';
	echo '<td>'.$row['SUBJECT'].'</td>';
	echo "</tr>";
}

//close table
echo "</table>";


}else{
	echo "Please Log In to the System.";
}
?>
  </body>
</html>    

