<?
include "header.php";
$userID=$uid;
//echo $userID;
?>

<?
if($submit){
	$query="delete from $db_user where user_id='".$userid."'";
	//echo $query;
	$result=$myEdit->sql($query);

	if($result)
		$message="User information has been successfully deleted!";
	else
		$message="User information could not be deleted.";

	print "<script>window.location='edituser.php?message=$message'</script>";
}

if($cancel)
	print "<script>window.location='edituser.php'</script>";
?>

<form action="delete.php" method="post">
<table border="0" cellpadding="0" cellspacing="0" width="95%">
		<tr>
			<td align="center">Are you sure you want to delete this user?</td>
		</tr><tr></tr><tr></tr><tr /><tr /><tr />
		
		<tr>
            <td align="center"><input type="submit" name="submit" class="btn" value="Delete">
			<input type="submit" name="cancel" class="btn" value="Cancel"></td>
		
			<td><input type="hidden" name="userid" value="<?=$userID;?>"></td>
		</tr>
</table>
</form>


<?
include "footer.php";
?>

