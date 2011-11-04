<?
session_start();
include "header.php";

$user=$session_vars["sess_id"];
if(!$user)
	print "<script>window.location='login.php'</script>";

$userId=$HTTP_SESSION_VARS["sess_id"];
$userName=$HTTP_SESSION_VARS["sess_name"];

?>
<?
if($submit)
	{
		$query1="select * from $db_user where user_id='".$userId."'";
		$result1=$myEdit->sql($query1);
		$row1=mysql_fetch_array($result1);
		if(md5($post_vars['old_pass'])==$row1['password'])
		{
			if($post_vars['new_pass']==$post_vars['renew_pass'])
			{	
				$newquery="update $db_user set password= '".md5($post_vars['new_pass'])."' where user_id='".$userId."'";
				
				$newresult=$myEdit->sql($newquery);
				if($newresult){
					$msg="Password has been changed successfully.";
				}
			}
			else 
				$msg="New passwords do not match. Try again.";
		}
		else
			$msg="Invalid old password. Try again.";
	}

	if($msg){
				echo"<div align='center'><BR><a class='head'>$msg</a><BR><BR>";
				echo"<a  href='javascript:history.back(-1)'>----Back----</a></div>";
			}
	else{


?>

<form name="form1" method="post">
<table border="0" cellpadding="0" cellspacing="0" width="95%">
<tr>
<td>
<div align="center">
<a class="head"><h3>Change Password</h3></a><hr>
<br />

	<table align="center" width="" border="0" cellspacing="1" cellpadding="3">
	<tr valign="top"> 
		<td> <table width="100%" border="0" cellspacing="1" cellpadding="3">
	</tr>

		<tr>
			<td>Username:</td>
			<td><input type="text" name="uname" value="<?=$userName?>" readonly></td>
		</tr>

		<tr>
			<td>Type Old Password:</td>
			<td><input type="password" name="old_pass"></td>
		</tr>

		<tr>
			<td>Type New Password:</td>
			<td><input type="password" name="new_pass"></td>
		</tr>

		<tr>
			<td>Retype New Password:</td>
			<td><input type="password" name="renew_pass"></td>
		</tr>
			
<tr>
	<td></td>

		<td><input type="submit" name="submit" value="Submit">&nbsp;
		<input type="reset" value="Reset"></td>
</tr>

</table>
</div>
</td>
</tr>
</table>
</form>

		

<?php
	}
include "footer.php";
?>