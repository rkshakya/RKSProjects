<?php 
session_start();
?>

<!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application :: RevSync Schedules</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
	
    
<?php 
if(isset($_SESSION['username'])){

include 'include/header.php';

//connect to database
include 'core/db_connect.php';

$dbid = $_GET['dbid'];
$mode = $_GET['mode'];

if($mode == 'add'){
    //insert job into tbl
    $qryInsSch = "INSERT INTO evl_revsync_schedule (dbid, status) VALUES ('$dbid', 'SCHEDULED')";
    $retval = mysql_query( $qryInsSch);
    if(! $retval ){
        die('Could not enter data: ' . mysql_error());
    }
}
			

//get results from database
$result=mysql_query("SELECT * FROM evl_revsync_schedule WHERE is_deleted=0 and dbid = ".$dbid." order by schdate DESC") or die(mysql_error());

//display data in table
echo "<h3 id = 'misc'>Sync job schedule and status</h3>";
echo "<h5><i>Sync via API is a time consuming task and takes about 2 hours to sync 400 records. Please set your expectations accordingly. An email will be sent when each scheduled job completes.</i></h5>";
echo "<table border='1' cellpadding='1' width='100%'>";
echo "<tr>
		<th colspan='1'>Action</th>
		<th>S.No</th>
		<th>Database ID</th>
		<th>Run Number</th>
		<th>Scheduled datetime</th>
                <th>Start datetime</th>
                <th>End datetime</th>
                <th>Status</th>
		<th>Insert count</th>
		<th>Update count</th>
		<th>Delete count</th>
		<th>Nochange count</th>
		</tr>";

//loop reults of database query, displaying them in table
$count=0;
while($row=mysql_fetch_array($result)){
	//echo each row into the table
	$count++;
	echo "<tr>";
	echo '<td><a href="deleteschedule.php?id=' . $row['id'] . '&dbid='.$dbid.'">Delete</a></td>';
	echo '<td>'.$count.'</td>';
	echo '<td>'.$row['dbid'].'</td>';
	echo '<td>'.$row['runno'].'</td>';
	echo '<td>'.$row['schdate'].'</td>';
	echo '<td>'.$row['sdate'].'</td>';
        echo '<td>'.$row['edate'].'</td>';
        echo '<td>'.$row['status'].'</td>';
        echo '<td>'.$row['cnt_inserts'].'</td>';
	echo '<td>'.$row['cnt_updates'].'</td>';
	echo '<td>'.$row['cnt_deletes'].'</td>';
	echo '<td>'.$row['cnt_unchanged'].'</td>';
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
