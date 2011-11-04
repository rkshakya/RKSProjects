<?php
session_start();
/**
 * @ author : Ravi Shakya
 * @ company: Bazaar International
 * @ date: 8/11/2004
 * @ version : 1.0
*/

$path="./";
include $path."classes/class_edit.php";
include $path."wrapper.php";
include $path."includes/config.php";

$myEdit=new class_edit();



if($session_vars["sess_id"])
{
	session_unset();
	session_destroy();
}


if($submit) {

$query="SELECT user_id, user_name, password, name, type FROM $db_user WHERE user_name='".$HTTP_POST_VARS['uname']."' AND password='".md5($HTTP_POST_VARS['upassword'])."'";
$result=$myEdit->sql($query);

	if($row=mysql_fetch_array($result))
	{
		session_register("sess_id");
		session_register("sess_name");
		session_register("sess_type");
		session_register("sessName");


		$HTTP_SESSION_VARS["sess_id"]=$row['user_id'];
		$HTTP_SESSION_VARS["sess_name"]=$row['user_name'];
		$HTTP_SESSION_VARS["sess_type"]=$row['type'];


		if($row['type']=="admin")
			$session_vars["sessName"]="Administrator";
		else if($row['type']=="general")
			$session_vars["sessName"]="General";

		if( $HTTP_SESSION_VARS["sess_type"]=='admin') {
		print "<script>window.location='adduser.php?name=".$row['user_name']."'</script>";
		} else {
		print "<script>window.location='search_items.php'</script>";
		}//end if-else
		
		


	} else {
    		$msg="Invalid Account.Please try again!";
    		print "<script>window.location='login.php?msg=$msg'</script>";
		    }
}
?>

<html>
<head>
<title>Bazaar International Intranet Administration </title>
<meta name="Generator" content="EditPlus">
<meta name="Author" content="">
<meta name="Keywords" content="">
<meta name="Description" content="">
<link rel="stylesheet" type="text/css" href="style.css">
<script src="date.js"></script>
</head>

<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr bgcolor="#446B70">
<!--<td><img src="images/banner.gif" width="780" height="80" border="0" alt="" /></td>-->
</tr>

<tr>
<td bgcolor="#6B73B5" height="5"></td> 
</tr>

<tr>
<td bgcolor="#999ECC" height="15"><div align=right class=date><script>document.write(dys[dy] + ", " + months[month] + " " + day + ", " +  " " + year);</script>&nbsp;</div></td>
</tr>

<tr>
<td bgcolor="#6B73B5" height="20">
</tr>

<tr bgcolor="#fffafa">
<td align="center">
<br />
<h3>Bazaar International Intranet Administration</h3>

<table border="0" cellpadding="0" cellspacing="0" width="95%">
<tr>
<td>
<div align="justify">
<br />
<div align="center">
<form name="login" method="post" action="login.php">
<table border="0" cellpadding="2" cellspacing="1" width="210">
<?php echo $msg;?> <br><br>
<tr bgcolor="#483d8b">
<td colspan="2">
<a class="head" style="color:#FFFFFF;">Login</a>
</td>
</tr>

<tr bgcolor="#e6e6f8">
<td align="right">Username : </td>
<td><input type="text" name="uname" size=25></td>
</tr>

<tr bgcolor="#e6e6f8">
<td align="right">Password : </td>
<td><input type="password" name="upassword" size=25></td>
</tr>

<tr bgcolor="#e6e6f8" height="25" valign="top">
<td>&nbsp;</td>
<td><input type="submit" name="submit" value="Login" class="btn"></td>
</tr>


<tr bgcolor="#483d8">
<td colspan="2" height="5"></td>
</tr>
</table>
</form>
<script>
	document.login.uname.focus();
</script>
</div>

</div>
</td>
</tr>
</table>

<?php
include "footer.php";
?>