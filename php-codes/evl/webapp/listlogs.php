<?php 
ob_start();
session_start();
//error_reporting(E_ALL);

function file_list($d,$x){ 
       foreach(array_diff(scandir($d, 2),array('.','..')) as $f)if(is_file($d.'/'.$f)&&(($x)?ereg($x.'$',$f):1))$l[]=$f; 
       return $l; 
} 
?>

<!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application :: Log File Listing</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
	
	<div id = "log">
	<table>
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

//print $logloc;

echo "<h3>Pls Click on the log file to view its content</h3>";

//get log file list
$loglist = file_list($logloc, 'log');
//print_r($loglist);
$i = 1;
foreach($loglist as $val){
    print "<tr><td>$i</td><td><a href = 'openlog.php?name=$val'>$val</a></td></tr>";
    $i++;
}

}else{
	echo "Please Log In to the System.";
}
?>
	</div>
	</table>
</body>
</html>