<?php 
session_start();
?>

<!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application :: Tags and related campaign</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
	
    
<?php 
if(isset($_SESSION['username'])){

    if(isset($_GET['id']) && is_numeric($_GET['id'])){
    $id = $_GET['id'];
    
    //echo "ID " .$id;
			    
    include 'include/header.php';
    
    //connect to database
    include 'core/db_connect.php';
    
    //get sqlserver db information
    $result=mysql_query("SELECT * FROM evl_revsync_settings WHERE id=$id") or die(mysql_error());
    
    if($row = mysql_fetch_array($result)){
	$serverhost = $row['sqlserver_host'];
	$serverport = $row['sqlserver_port'];
	$serverdb = $row['sqlserver_db'];
	$serveruser = $row['sqlserver_user'];
	$serverpass = $row['sqlserver_pass'];
    }
    
    //now get tag info from SQLServer table
    //get sqlserver connection
    $dbhandle = mssql_connect($serverhost.":".$serverport, $serveruser, $serverpass)
		or die("Couldn't connect to SQL Server on $serverhost");

    //select a database to work with
    $selected = mssql_select_db($serverdb, $dbhandle)
		or die("Couldn't open database $serverdb");
    
    //display data in table
    echo "<h3 id = 'misc'>Tags and related IC campaign</h3>";
    echo '<a href="addtags.php?dbid='.$id.'">Add new tag and related campaign</a>';
    echo "<table border='1' cellpadding='1' width='100%'>";
    echo "<tr>
		    <th colspan='2'>Actions</th>
		    <th>S.No</th>
		    <th>Tag ID</th>
		    <th>Tag code</th>
		    <th>Tag name</th>
		    <th>Tag order</th>
		    <th>Tag IsActive</th>
		    <th>Campaign ID</th>
		    <th>Campaign Name</th>		    
		    </tr>";
		    
	$qryTags = "SELECT * FROM RM_Tag";
	
	$resTags = mssql_query($qryTags);
	    
    
    //loop reults of database query, displaying them in table
    $count=0;
    while($rowTags = mssql_fetch_array($resTags)){
	    //fetch campaign name for each campaign
	    $campname = '';
	    if($rowTags['InstantCustomerCampaignID_C']){
	    $qryCamp = "select campaign_name from evl_dbmappings where campaign_id = ".$rowTags['InstantCustomerCampaignID_C'];
	    $resCamp = mysql_query($qryCamp) or die(mysql_error());
    
	    if($rowCamp = mysql_fetch_array($resCamp)){
		$campname = $rowCamp['campaign_name'];
	    }
	    }
	    
	    //echo each row into the table
	    $count++;
	    echo "<tr>";
	    echo '<td><a href="addtags.php?tid=' . $rowTags['TagID'] . '&dbid='.$id.'">Edit</a></td>';
	    echo '<td><a href="deletetags.php?tid=' . $rowTags['TagID'] . '&dbid='.$id.'">Delete</a></td>';
	    echo '<td>'.$count.'</td>';
	    echo '<td>'.$rowTags['TagID'].'</td>';
	    echo '<td>'.$rowTags['TagCode'].'</td>';
	    echo '<td>'.$rowTags['TagName'].'</td>';
	    echo '<td>'.$rowTags['TagOrder'].'</td>';
	    echo '<td>'.$rowTags['TagIsActive'].'</td>';
	    if($rowTags['InstantCustomerCampaignID_C']){
	    echo '<td>'.$rowTags['InstantCustomerCampaignID_C'].'</td>';
	    } else {echo '<td>&nbsp;</td>';}
	    echo '<td>'.$campname.'</td>';
	    echo "</tr>";
    }
    
    echo "</table>";
    
    mssql_close($dbhandle);
    
    }


}else{
	echo "Please Log In to the System.";
}
?>
  </body>
</html>    
