<?php
session_start();
include "header.php";

$user=$session_vars["sess_id"];
if(!$user)
	print "<script>window.location='login.php'</script>";


$ID=$id;
$TITLE = $title;
$ISSN = $issn;
$PUBLISHER = $publisher;
$SUBJECT = $subject;
$TYPE = $type;


$query="SELECT journal_no, issn, title, remarks, frequency, currency, price_world_$, subject_area, imprint, type FROM $db_journal WHERE id='".$ID."'";
$result=$myEdit->sql($query);
$row=mysql_fetch_array($result);

if($submit)
{
	
	{
	$today = date("Y-m-d H:i:s");  
	$editQ="update $db_journal set journal_no='".$journal_no."', issn = '".$issn."', title = '".$title."', remarks= '".$remarks."', frequency = '".$frequency."', currency = '".$currency."', price_world_$ = '".$price."', subject_area = '".$subject."', imprint= '".$publisher."', type ='".$type."', m_date= '".$today."' where id='".$id."'";
	
	$resultQ=$myEdit->sql($editQ);
	
	if($resultQ) {
		$msg="Item information has been successfully edited!";
	} else {
		$msg="Item information could not be edited.";
	}
	
	}
	
    print "<script>window.location='search_results.php?message=$msg&title=$TITLE&issn=$ISSN&publisher=$PUBLISHER&subject=$SUBJECT&type=$TYPE'</script>";
	
}
?>


<form name="form" action="edit_item.php">

<table>
   <tr>
		<td>Journal No:&nbsp;</td>
		<td><input type="text" name="journal_no" value="<?echo $row['journal_no']?>" ></td>
   </tr>
   <tr>
		<td>ISSN/ISBN/EAN:&nbsp;</td>
		<td><input type="text" name="issn" value="<?echo $row['issn']?>"></td>
   </tr>
   
    <tr>
		<td>Title:&nbsp;</td>
		<td><input type="textarea" SIZE=40 name="title" value="<?echo $row['title']?>"></td>
   </tr>
   
    <tr>
		<td>Remarks:&nbsp;</td>
		<td><input type="textarea" size=40 name="remarks" value="<?echo $row['remarks']?>"></td>
   </tr>
   
   <tr>
		<td>Frequency:&nbsp;</td>
		<td><input type="text" name="frequency" value="<?echo $row['frequency']?>"></td>
   </tr>
   
    <tr>
		<td>Currency:&nbsp;</td>
		<td><input type="text" name="currency" value="<?echo $row['currency']?>"></td>
   </tr>
   
   <tr>
		<td>Price:&nbsp;</td>
		<td><input type="text" name="price" value="<?echo $row['price_world_$']?>"></td>
   </tr>
       
     <tr>
		<td>Subject:&nbsp;</td>
		<td><input type="text" name="subject" value="<?echo $row['subject_area']?>"></td>
   </tr>
   
   <tr>
		<td>Publisher:&nbsp;</td>
		<td><input type="text" name="publisher" value="<?echo $row['imprint']?>"></td>
   </tr>
  
  <tr>
		<td>Type:&nbsp;</td>
		<td><input type="text" name="type" value="<?echo $row['type']?>"></td>
   </tr> 
   
    

<tr></tr>
	<tr>
		<td></td>
		<td><input type="submit" name="submit" value="Edit">&nbsp;
		<input type= "reset" name= "reset" value ="Reset">
		<input type="hidden" name="id" value="<?=$ID;?>">
	
		</td>
	</tr>

</table>
</form>

<?php
include "footer.php";
?>