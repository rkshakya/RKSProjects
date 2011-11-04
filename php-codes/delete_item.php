<?
include "header.php";
$ID=$id;
$TITLE = $title;
$ISSN = $issn;
$PUBLISHER = $publisher;
$SUBJECT = $subject;
$TYPE = $type;

//echo $userID;
?>

<?
if($submit){
	//print ">>>>".$id;
	$query="DELETE FROM $db_journal WHERE id='".$id."'";
	//echo $query;
	$result=$myEdit->sql($query);
    //print $result;
	if($result)
		$message="Item information has been successfully deleted!";
	else
		$message="Item information could not be deleted.";

	print "<script>window.location='search_results.php?message=$message'</script>";
}

if($cancel)
	print "<script>window.location='search_results.php?title=$title&issn=$issn&publisher=$publisher&subject=$subject&type=$type'</script>";
?>

<form action="delete_item.php" method="post">
<table border="0" cellpadding="0" cellspacing="0" width="95%">
		<tr>
			<td align="center">Are you sure you want to delete this item?</td>
		</tr><tr></tr><tr></tr><tr /><tr /><tr />
		
		<tr>
            <td align="center"><input type="submit" name="submit" class="btn" value="Delete">
			<input type="submit" name="cancel" class="btn" value="Cancel"></td>
		
			<td><input type="hidden" name="id" value="<?=$ID;?>"></td>
			<td><input type="hidden" name="title" value="<?=$TITLE;?>"></td>
			<td><input type="hidden" name="issn" value="<?=$ISSN;?>"></td>
			<td><input type="hidden" name="publisher" value="<?=$PUBLISHER;?>"></td>
			<td><input type="hidden" name="subject" value="<?=$SUBJECT;?>"></td>
			<td><input type="hidden" name="type" value="<?=$TYPE;?>"></td>
		</tr>
</table>
</form>


<?
include "footer.php";
?>

