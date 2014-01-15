<?php 
session_start();?>

<!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application :: Managed Accounts</title>
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
$result=mysql_query("SELECT * FROM evl_managedaccounts WHERE is_deleted=0") or die(mysql_error());

//display data in table
echo "<h3 id = 'misc'>Managed accounts and their API details</h3>";
echo '<a href="addmgd.php">Add new account</a>';
echo "<table border='1' cellpadding='1' width='100%'>";
echo "<tr>
		<th colspan='2'>Actions</th>
		<th>S.No</th>
		<th>Username</th>
		<th>API URL</th>
		<th>API KEY</th>
		<th>Created Date</th>
		</tr>";

//loop reults of database query, displaying them in table
$count=0;
while($row=mysql_fetch_array($result)){
	//echo each row into the table
	$count++;
	echo "<tr>";
	echo '<td><a href="addmgd.php?id=' . $row['id'] . '">Edit</a></td>';
	echo '<td><a href="deletemgd.php?id=' . $row['id'] . '">Delete</a></td>';
	echo '<td>'.$count.'</td>';
	echo '<td>'.$row['uname'].'</td>';
	echo '<td>'.$row['api_url'].'</td>';
	echo '<td>'.$row['api_key'].'</td>';
	echo '<td>'.$row['cdate'].'</td>';
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

