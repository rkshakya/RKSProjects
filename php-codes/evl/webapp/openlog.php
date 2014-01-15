<?php 
ob_start();
session_start();
//error_reporting(E_ALL);
?>

<!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application :: View Log</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
<?php
if(isset($_SESSION['username'])){
	
include 'include/header.php';
	
$u_id=$_SESSION['u_id'];

include 'core/db_connect.php';

$result = mysql_query("SELECT `value` FROM evl_miscsettings where `key` = 'LOG_LOCATION'") or die(mysql_error());
$row = mysql_fetch_object($result);
$logloc = $row -> value;
//print $logloc;

if(substr($logloc, -1) != "/"){
    $logloc .= "/";
}

$lines = file($logloc.$_GET['name']);

echo "<h3>Contents of ".$_GET['name']."</h3>";
echo "<textarea rows='100' cols='150' readonly>";

// Loop through our array, show HTML source as HTML source; and line numbers too.
foreach ($lines as $line_num => $line) {
    //echo  htmlspecialchars($line) . "<br />\n";
    
    echo htmlspecialchars($line);    
}
echo "</textarea>";

}else{
	echo "Please Log In to the System.";
}
?>
</body>
</html>