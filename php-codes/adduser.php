<?php 
session_start();

include "header.php";

$user=$session_vars["sess_id"];
if(!$user)
	print "<script>window.location='login.php'</script>";



if ($submit) {
	$newQuery="SELECT * FROM $db_user WHERE user_name='".$post_vars['username']."'";
	$newResult=$myEdit->sql($newQuery);
	$cnt=mysql_num_rows($newResult);
	if($cnt>=1) {
		$msg="Username already exists. Please select another username.";
	} else {

			if($post_vars['password']==$post_vars['rePassword']) {

				$query="INSERT INTO $db_user (user_name,password,name,type) VALUES ('".$post_vars['username']."','".md5($post_vars['password'])."','".$post_vars['full_name']."','".$post_vars['type']."')";
				//echo $query;
				$result=$myEdit->sql($query);
				    if($result) {
					$msg="User has been successfully created.";
				    } else {
					$msg="Sorry!User could not be created.";
					} //end if-else
			} else {
				$msg="The passwords do not match.Please try again.";
		    } //end if-else
    } //end if-else
}    


	if($msg){
				echo"<div align='center'><BR><a class='head'>$msg</a><BR><BR>";
				echo"<a  href='javascript:history.back(-1)'>----Back----</a></div>";
			}
	else {
?>
<form name="form1" action="adduser.php" method="post">

<div align="center">

<h3>Add a new user</h3><hr>

<table>
<tr bgcolor="#fffafa">
   <tr>
		<td>Login Name:&nbsp;</td>
		<td><input type="text" name="username"></td>
   </tr>
   <tr>
		<td>Password:&nbsp;</td>
		<td><input type="password" name="password"></td>
   </tr>
   <tr>
		<td>Retype Password:</td>
		<td><input type="password" name="rePassword"></td>
   </tr>
   <tr>
		<td>Full name :</td>
		<td><input type="text" size="35" name="full_name"></td>
   </tr>
   <tr>
		<td>User Type:</td>
		<td><select name=type>
		<?php	$query1="SELECT * FROM priv_info";
			$result1=$myEdit->sql($query1);
			while($row1=mysql_fetch_array($result1))
				{
				    $utype = $row1['priv_level'];
				    if( $utype == "general") {
				    echo "<option value='$utype'selected = true >$utype</option>";
				    } else {
					echo "<option value='$utype'>$utype</option>";
					}
					$i++;
				}
				
			?>
        </select>
		</td>
   </tr>
 
	
<tr></tr>
	<tr>
		<td></td>
		<td><input type="submit" name="submit" value="Submit">&nbsp;
		<input type="reset" value="Reset"></td>
	</tr>
</tr>	
</table>
</div>
</form>

<?php
}
include "footer.php";
?>