<?php
session_start();
include "header.php";

$user=$session_vars["sess_id"];
if(!$user)
	print "<script>window.location='login.php'</script>";


?>

<form name="form" method="post">
<table border="0" cellpadding="0" cellspacing="0" width="95%">
<tr>
<td>
<div align="center"> <a class="head"><h3>Search User<h3></a> </td>

<td align="center"><b><?php echo $message;?></b></td>
</tr>
<tr>
<td align = "center"> <input type = "text" name = "search">
    <input type="submit" name="submit" value="Search">&nbsp; 
		<input type= "reset" name= "reset" value ="Reset">
		</td>
</tr>


</table>


<?php
if($submit) {
?>
 <table align="center" width="80%" border="0" cellspacing="1" cellpadding="3">
	<tr>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">S.N.</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Login-name</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Type</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Complete Name</font></b></div></td>
		
	<?php if( $HTTP_SESSION_VARS["sess_type"]=='admin') {
	?>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Options</font></b></div></td>
	<? } ?>	
	</tr>


<?php
$query="SELECT * FROM $db_user WHERE user_name LIKE "."'%".$post_vars['search']."%'"."OR name LIKE "."'%".$post_vars['search']."%'"."OR type LIKE "."'%".$post_vars['search']."%'";
$result=$myEdit->sql($query);
$i=1;
while($row=mysql_fetch_array($result))
{
	
	print "<tr>";
	print "<td bgcolor='#e6e6fa'><div align='center'>$i</div></td>";
	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['user_name']."</div></td>";
	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['type']."</div></td>";
	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['name']."</div></td>";
	if( $HTTP_SESSION_VARS["sess_type"]=='admin') {
	print "<td bgcolor='#e6e6fa'><div align='center'><a href='edit.php?uid=".$row['user_id']."'><font color='red'>Edit</font></a>|<a href='delete.php?uid=".$row['user_id']."'><font color='red'>Delete</font></a></td>";
    }

	print "</tr>";
	$i++;
    }
}
?>

</table>

</form>	


<?php
include "footer.php";
?>