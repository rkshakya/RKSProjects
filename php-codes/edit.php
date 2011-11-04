<?php
session_start();
include "header.php";

$user=$session_vars["sess_id"];
if(!$user)
	print "<script>window.location='login.php'</script>";


$userID=$uid;


$query="SELECT * FROM $db_user WHERE user_id='".$userID."'";

$result=$myEdit->sql($query);
$row=mysql_fetch_array($result);



if($submit)
{
	if($password==$rePassword)
	{
	
	$editQ="update $db_user set type='".$type."' where user_id='".$userid."'";
	
	
	$resultQ=$myEdit->sql($editQ);
	
	if($resultQ)
		$msg="User information has been successfully edited!";
	else
		$msg="User information could not be edited.";
	
	$editR="update $db_user set name ='".$name."' where user_id='".$userid."'";
	$resultR=$myEdit->sql($editR);
	if($resultQ)
		$msg="User information has been successfully edited!";
	else
		$msg="User information could not be edited.";
	}
	

	print "<script>window.location='edituser.php?message=$msg'</script>";
}
?>


<form name="form" action="edit.php">

<table>
   <tr>
		<td>Login-name:&nbsp;</td>
		<td><input type="text" name="username" value="<?echo $row['user_name']?>" readonly></td>
   </tr>
   <tr>
		<td>Complete Name:&nbsp;</td>
		<td><input type="text" name="name" value="<?echo $row['name']?>"></td>
   </tr>
       
     <tr>
		<td>Type:</td>
		<td><select name="type">
		<?	$query1="select * from priv_info";
			$result1=$myEdit->sql($query1);
			while($row1=mysql_fetch_array($result1))
				{
					$utype=$row1['priv_level'];
					if($utype==$row['type'])
						echo"<option value='$utype' selected=true>$utype</option>";
					else
						echo"<option value='$utype'>$utype</option>";
				}
				
			?>

		</select>
		</td>
	</tr>


<tr></tr>
	<tr>
		<td></td>
		<td><input type="submit" name="submit" value="Edit">&nbsp;
            <input type="reset" name="reset" value="Reset">
		<input type="hidden" name="userid" value="<?=$userID;?>">
		</td>
	</tr>

</table>
</form>

<?
include "footer.php";
?>