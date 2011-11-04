<?php 
session_start();

include "header.php";

$user=$session_vars["sess_id"];
if(!$user)
	print "<script>window.location='login.php'</script>";

?>





<form name="form1" action="search_results.php" method="get">

<div align="center">

<h3>Search for an Item</h3><hr>

<table>
<tr bgcolor="#fffafa">
   <tr>
		<td>Title:&nbsp;</td>
		<td><input type="text" name="title"></td>
   </tr>
   <tr>
		<td>ISSN/ISBN/EAN:&nbsp;</td>
		<td><input type ="text" name="issn"></td>
   </tr>
   <tr>
		<td>Publisher:</td>
		<td><select name="publisher" size=2>
		<?php	$query2="SELECT DISTINCT imprint FROM journal_info";
			$result2=$myEdit->sql($query2);
			while($row2=mysql_fetch_array($result2))
				{
				    $pub = $row2['imprint'];
				    echo "<option value='$pub' >$pub</option>";
					$i++;
				}
				
			?>
			</select>
			</td>
   </tr>
   <tr>
		<td>Subject:</td>
		<td><select name="subject" size=2 >
		<?php	$query3="SELECT DISTINCT subject_area FROM journal_info";
			$result3=$myEdit->sql($query3);
			while($row3=mysql_fetch_array($result3))
				{
				    $sub = $row3['subject_area'];
				    echo "<option value='$sub' >$sub</option>";
					$i++;
				}
				
			?>
			</select>
			</td>
   </tr>
   <tr>
		<td>Item Type:</td>
		<td><select name=type size=2>
		<?php	$query4="SELECT DISTINCT type FROM journal_info";
			$result4=$myEdit->sql($query4);
			while($row4=mysql_fetch_array($result4))
				{
				    $typ = $row4['type'];
				    echo "<option value='$typ' >$typ</option>";
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
//}
include "footer.php";
?>