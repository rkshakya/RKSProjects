<?php 
session_start();

include "header.php";

$user=$session_vars["sess_id"];
if(!$user)
	print "<script>window.location='login.php'</script>";



if ($submit) {
	if($get_vars['title']&& $get_vars['issn']) {
	    $newQuery = "SELECT * FROM $db_journal WHERE title='".$get_vars['title']."'"." OR issn = '".$get_vars['issn']."'";
	}
	if($get_vars['issn']) {
	    $newQuery = "SELECT * FROM $db_journal WHERE issn = '".$get_vars['issn']."'";
	}
	if($get_vars['title']) {
	    $newQuery = "SELECT * FROM $db_journal WHERE title = '".$get_vars['title']."'";
	}
	$newResult = $myEdit->sql($newQuery);
	$cnt = mysql_num_rows($newResult);
	if($cnt >= 1) {
		$msg = "Item already exists in the database. Please check the item information again.";
	} else {
                $today = date("Y-m-d H:i:s");
				$query = "INSERT INTO $db_journal (journal_no, issn, title, remarks, frequency, currency, price_world_$, subject_area, imprint, type, m_date ) VALUES ('".$get_vars['journal_no']."','".$get_vars['issn']."','".$get_vars['title']."','".$get_vars['remarks']."','".$get_vars['frequency']."','".$get_vars['currency']."','".$get_vars['price']."','".$get_vars['subject']."','".$get_vars['publisher']."','".$get_vars['type']."','".$today."')";
				//echo $query;
				$result = $myEdit->sql($query);
				    if($result) {
					$msg = "New item information has been added successfully.";
				    } else {
					$msg = "Sorry!Item information could not be added.";
					} //end if-else
			
    } //end if-else
} // end if(submit)   


	if($msg){
				echo"<div align='center'><BR><a class='head'>$msg</a><BR><BR>";
				echo"<a  href='javascript:history.back(-1)'>----Back----</a></div>";
			}
	else {
?>

<form name = "form" action = "add_items.php" method = "get">
<div align = "center">
<h3>Add a new item</h3><hr>
<table>
   <tr>
		<td>Journal No:&nbsp;</td>
		<td><input type="text" name="journal_no" ></td>
   </tr>
   <tr>
		<td>ISSN/ISBN/EAN:&nbsp;</td>
		<td><input type="text" name="issn" ></td>
   </tr>
   
    <tr>
		<td>Title:&nbsp;</td>
		<td><input type="textarea" SIZE=40 name="title" ></td>
   </tr>
   
    <tr>
		<td>Remarks:&nbsp;</td>
		<td><input type="textarea" size=40 name="remarks" ></td>
   </tr>
   
   <tr>
		<td>Frequency:&nbsp;</td>
		<td><input type="text" name="frequency" ></td>
   </tr>
   
    <tr>
		<td>Currency:&nbsp;</td>
		<td><input type="text" name="currency" ></td>
   </tr>
   
   <tr>
		<td>Price:&nbsp;</td>
		<td><input type="text" name="price" ></td>
   </tr>
       
     <tr>
		<td>Subject:&nbsp;</td>
		<td><input type="text" name="subject" ></td>
   </tr>
   
   <tr>
		<td>Publisher:&nbsp;</td>
		<td><input type="text" name="publisher" ></td>
   </tr>
  
  <tr>
		<td>Type:&nbsp;</td>
		<td><input type="text" name="type" ></td>
   </tr> 
   
    

<tr></tr>
	<tr>
		<td></td>
		<td><input type="submit" name="submit" value="Add">&nbsp; 
		<input type= "reset" name= "reset" value ="Reset">
		
	
		</td>
	</tr>

</table>
</form>


<?php
}
include "footer.php";
?>