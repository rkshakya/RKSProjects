<?php 
session_start();
?>

<!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application :: RevSync Settings</title>
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
$result=mysql_query("SELECT * FROM evl_revsync_settings WHERE is_deleted=0") or die(mysql_error());

//display data in table
echo "<h3 id = 'misc'>Evolve CRM DB details (for sync from Evolve DB to IC)</h3>";
echo '<a href="addrevsync.php">Add DB information</a>';
echo "<table border='1' cellpadding='1' width='100%'>";
echo "<tr>
		<th colspan='2'>Actions</th>
		<th>S.No</th>
		<th>SQLSERVER_HOST</th>
		<th>SQLSERVER_PORT</th>
		<th>SQLSERVER_DB</th>
                <th>SQLSERVER_USER</th>
                <th>SQLSERVER_PASSWORD</th>
                <th>Autosync?</th>
		<th>Modified Date</th>
                <th colspan='3'>Events</th>
		</tr>";

//loop reults of database query, displaying them in table
$count=0;
while($row=mysql_fetch_array($result)){
	//echo each row into the table
	$count++;
	echo "<tr>";
	echo '<td><a href="addrevsync.php?id=' . $row['id'] . '">Edit</a></td>';
	echo '<td><a href="deleterevsync.php?id=' . $row['id'] . '">Delete</a></td>';
	echo '<td>'.$count.'</td>';
	echo '<td>'.$row['sqlserver_host'].'</td>';
	echo '<td>'.$row['sqlserver_port'].'</td>';
	echo '<td>'.$row['sqlserver_db'].'</td>';
	echo '<td>'.$row['sqlserver_user'].'</td>';
        echo '<td>'.$row['sqlserver_pass'].'</td>';
        echo '<td>'.$row['include_autosync'].'</td>';
        echo '<td>'.$row['mdate'].'</td>';
        echo '<td><a href="schedule.php?mode=add&dbid=' . $row['id'] . '" >Schedule Sync</a></td>';
	echo '<td><a href="viewtags.php?id=' . $row['id'] . '" >View or create tags</a></td>';
	echo '<td><a href="schedule.php?dbid=' . $row['id'] . '" >View sync status</a></td>';
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
