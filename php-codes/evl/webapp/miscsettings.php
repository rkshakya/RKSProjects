<?php 
ob_start();
session_start();?>

<!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application :: Misc Settings</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
<?php
if(isset($_SESSION['username'])){
	
include 'include/header.php';
	
$u_id=$_SESSION['u_id'];

// connect to the DB
include 'core/db_connect.php';

if($_POST){
	foreach($_POST['id'] as $id=> $type){
		
		 $query = "UPDATE evl_miscsettings
                SET value = '".$type."'
                WHERE id = '".$id."'";
				
		mysql_query($query) or die(mysql_error());;		
	}
	$query=mysql_query("SELECT CURRENT_TIMESTAMP");
	$query1=mysql_fetch_array($query);
	$modifed_date=$query1[0];
	mysql_query("update evl_miscsettings set modified_at='$modifed_date',modified_by='$u_id'") or die(mysql_error());
	header("Location: dashboard.php"); 
	
}else{
	
  // Get all evl_miscsettings records and then iterate
  $result = mysql_query("SELECT * FROM evl_miscsettings") or die(mysql_error()); ?>
<h3 id = 'misc'>API and Mail Settings</h3>
  <form name='form' action='<?php echo $_SERVER['PHP_SELF'] ?>' method='post'>
  <table>
    <?php while($row = mysql_fetch_object($result)){ ?>
      <tr><td class = 'lbl'><label><?php echo $row->key; ?></label></td>
      <td><input size='30' type='text' name='id[<?php echo $row->id ?>]' value='<?php echo $row->value; ?>'>
      <?php if ($row->key == 'MAILER_FLAG'){
		print "<i>Set to 1 to enable mail alerts, 0 to disable.</i>";
	}elseif($row->key == 'API_URL'){
		print "<i>API URL of Instant Customer</i>";
	}elseif($row->key == 'SMTP_PASS'){
		print "<i>SMTP Server password</i>";
	}elseif($row->key == 'TO_EMAIL'){
		print "<i>Email address where alerts are sent</i>";
	}elseif($row->key == 'SUBJECT'){
		print "<i>Subject of email</i>";
	}
      ?>
      </td>
      </tr>
    <?php } ?>
    <tr><td>&nbsp;</td><td><input type='submit' value="Save"></td></tr>
   </table>
  </form>
  
<?php 
}

}else{
	echo "Please Log In to the System.";
}
?>
</body>
</html>    
