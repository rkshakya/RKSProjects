<?
include "header.php";
/*
$user=$session_vars["id"];
if(!$user)
	print "<script>window.location='login.php'</script>";
*/

?>

<form name="form" method="post">
<table border="0" cellpadding="0" cellspacing="0" width="95%">
<tr>
<td>
<div align="center"> <a class="head"><h3>Search a user<h3></a></div><br></td></tr>
<tr><td>

<div align=center valign=top>
 Search: <input type="textbox" name="search">
		<input type="submit"  name="submit" value="Go">
<?
 print $search;
?>
</td> </tr>
</table>

<?
$submit=1;
if($submit)
			{
				$query="select * from $db_user where user_name like '$search%'";
				$result=$myEdit->sql($query);
				if($result)
				{
?>


					 <table align="center" width="80%" border="0" cellspacing="1" cellpadding="3">
						<tr>
							<td bgcolor="#6B73B5">
							<div align="center"><b><font color="white">S.N.</font></b></div></td>
							<td bgcolor="#6B73B5">
							<div align="center"><b><font color="white">Username</font></b></div></td>
							<td bgcolor="#6B73B5">
							<div align="center"><b><font color="white">Base Directory</font></b></div></td>
							<td bgcolor="#6B73B5">
							<div align="center"><b><font color="white">Preview URL</font></b></div></td>
							<td bgcolor="#6B73B5">
							<div align="center"><b><font color="white">Options</font></b></div></td>
							</td>
						</tr>
<?
					$i=1;

								while($row=mysql_fetch_array($result))
								{
					
					print "<tr>";
					print "<td bgcolor='#e6e6fa'><div align='center'>$i</div></td>";
					print "<td bgcolor='#e6e6fa'><div align='center'>".$row['user_name']."</div></td>";
					print "<td bgcolor='#e6e6fa'><div align='center'>".$row['base_dir']."</div></td>";
					print "<td bgcolor='#e6e6fa'><div align='center'>".$row['preview_url']."</div></td>";
					print "<td bgcolor='#e6e6fa'><div align='center'><a href='edit.php?uid=".$row['user_id']."'><font color='red'>Edit</font></a>|<a href='delete.php?uid=".$row['user_id']."'><font color='red'>Delete</font></a></td>";
					print "</tr>";
					$i++;
								}//end while
?>

					</table>
<?			
				}else
				{
				print "Search Unsuccessful::No matching record found.";
				}//end if_else
}//end if

?>


<!--</form>	-->


<?
include "footer.php";
?>