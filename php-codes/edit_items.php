<?php
session_start();

include "header.php";
include "classes/class.display.php";

$user=$session_vars["sess_id"];
if(!$user)
	print "<script>window.location='login.php'</script>";

$myDisplay = new class_display($myEdit);

    $srchQuery = "SELECT id, issn, title,remarks, frequency, currency, price_world_$, subject_area, imprint, type, m_date FROM $db_journal"; 
	   
   // print ">>>>".$srchQuery;
    $result=$myEdit->sql($srchQuery);
 
    $count1=mysql_num_rows($result);
    $rpage=$PHP_SELF;
	 if(!$main)$main=0;
	 if(!$start)$start=0;
	 $sn=1;
	 if($start!=0)
		$sn=$start+1;
	$parameter="";
	//$parameter="search=$search&start_date=$start_date&end_date=$end_date&keyword=$keyword&count1=$count1";
	$myDisplay->sql_batching($srchQuery,$rpage,$parameter,$per_page,$max_rec,$main,$start);


if($count1 < 1) {
		print "<b>Sorry! No result found!!!</b><br>";
} else {

print "<tr><td align='left'><b>Total [<font color='red'>$count1</font>] Records</b></td>";
print "<td align='right'><b>Displaying Page <font color='red'>".$myDisplay->getCurrentPage()."</font> out of <font color='red'>".$myDisplay->getTotalPages()."</font> Pages.</b></td>";
print "</tr>";

?>

  
  <form name="form" method="post">
<table border="0" cellpadding="0" cellspacing="0" width="95%">
<tr>
<td>
<div align="center"> <a class="head"><h3>Item Info<h3></a> <br /><br></td>
<td align="center"><b><?php echo $message;?></b></td>
</tr>
</table>

 <table align="center" width="80%" border="0" cellspacing="1" cellpadding="3">
	<tr>
	<?php
	if( $HTTP_SESSION_VARS["sess_type"]=='admin') {
	?>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Option</font></b></div></td>
    <?php
        }
    ?>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">ISSN/ISBN/EAN</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Title</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Price</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Remarks</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Frequency(/year)</font></b></div></td>
		
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Subject</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Publisher</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Type</font></b></div></td>
		<td bgcolor="#6B73B5">
		<div align="center"><b><font color="white">Date</font></b></div></td>
		
		</td>
	</tr>
	
 <?php  
 
		
		while($row = $myDisplay->getNextRow())

		{
			print "<tr>";
			if( $HTTP_SESSION_VARS["sess_type"]=='admin') {
			print "<td bgcolor='#e6e6fa'><div align='center'><a href='edit_item.php?id=".$row['id']."&title=$title&issn=$issn&publisher=$publisher&subject=$subject&type=$type'><font color='red'>Edit</font></a>|<a href='delete_item.php?id=".$row['id']."&title=$title&issn=$issn&publisher=$publisher&subject=$subject&type=$type'><font color='red'>Delete</font></a></td>";
			}
    	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['issn']."</div></td>";
    	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['title']."</div></td>";
    	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['currency'].$row['price_world_$']."</div></td>";
    	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['remarks']."</div></td>";
    	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['frequency']."</div></td>";
    	
    	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['subject_area']."</div></td>";
    	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['imprint']."</div></td>";
    	print "<td bgcolor='#e6e6fa'><div align='center'>".$row['type']."</div></td>";
        print "<td bgcolor='#e6e6fa'><div align='center'>".$row['m_date']."</div></td>";
    	
    /*	print "<td bgcolor='#CEDEE0'><div align='center'><a href='edit.php?uid=".$row['id']."'>Edit</a>|<a href='delete.php?uid=".$row['id']."'>Delete</a>|<a href='changepass.php?uid=".$row['id']."'>Change password</a><div></td>";*/
			
			
			print "</tr>";
			$sn++;
		}//end while

?>


</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<?
	print "<tr bgcolor='#CEDEE0'>";
	print "<td  align='center'>".$myDisplay->getNavigationBar()."</td>";
	print "</tr>";

?>

</table>			
   	
<?  
        } //end if-else  

?>

</table>

</form>	

<?php
include "footer.php";
?>