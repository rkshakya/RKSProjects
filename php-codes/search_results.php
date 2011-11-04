<?php
session_start();

include "header.php";
include "classes/class.display.php";

$user=$session_vars["sess_id"];
if(!$user)
	print "<script>window.location='login.php'</script>";

$title = $get_vars['title'];
$issn = $get_vars['issn'];
$publisher = $get_vars['publisher'];
$subject = $get_vars['subject'];
$type = $get_vars["type"];



$myDisplay = new class_display($myEdit);
        
        if($title) {
            $t = "1" ;
           } else {
           $t = "0";
           }
           
         if($issn) {
            $i = "1" ;
           } else {
           $i = "0";
           }
         
          if($publisher) {
            $p = "1" ;
           } else {
           $p = "0";
           }
           
           if($subject) {
            $s = "1" ;
           } else {
           $s = "0";
           } 
           
            if($type) {
            $ty = "1" ;
           } else {
           $ty = "0";
           }
           
           $determinant = $t.$i.$p.$s.$ty;
    
        


    $srchQuery = "SELECT id, issn, title,remarks, frequency, currency, price_world_$, subject_area, imprint, type, m_date FROM $db_journal WHERE "; 
	
	switch ($determinant) {
	case "11111" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND issn='".$get_vars['issn']."' AND imprint LIKE"."'".$get_vars['publisher']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	
	case "11110" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND issn='".$get_vars['issn']."' AND imprint LIKE"."'".$get_vars['publisher']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'";
	     break;
	
	case "11101" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND issn='".$get_vars['issn']."' AND imprint LIKE"."'".$get_vars['publisher']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
    case "11100" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND issn='".$get_vars['issn']."' AND imprint LIKE"."'".$get_vars['publisher']."%'";
	     break;	
	//------------------------
    case "11011" :
	    $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND issn='".$get_vars['issn']."' AND subject_area LIKE"."'%".$get_vars['subject']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;	
    
    case "11010" :
	    $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND issn='".$get_vars['issn']."' AND subject_area LIKE"."'%".$get_vars['subject']."%'";
	     break;	
	     
	case "11001" :
	    $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND issn='".$get_vars['issn']."' AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
	case "11000" :
	    $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND issn='".$get_vars['issn']."'";
	     break;			          
	//------------------------------
	case "10111" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND imprint LIKE"."'".$get_vars['publisher']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	
	case "10110" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND imprint LIKE"."'".$get_vars['publisher']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'";
	     break;
	     
	case "10101" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND imprint LIKE"."'".$get_vars['publisher']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
	case "10100" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND imprint LIKE"."'".$get_vars['publisher']."%'";
	     break;
	//-----------------------------
	 case "10011" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
	 case "10010" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'";
	     break;  
	     
	 case "10001" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
	 case "10000" :
	     $srchQuery .= " title LIKE "."'%".$get_vars['title']."%'";
	     break;  
    //-----------------------------
    case "01111" :
	     $srchQuery .= " issn='".$get_vars['issn']."' AND imprint LIKE"."'".$get_vars['publisher']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	
	case "01110" :
	     $srchQuery .= " issn='".$get_vars['issn']."' AND imprint LIKE"."'".$get_vars['publisher']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'";
	     break;
	     
	case "01101" :
	     $srchQuery .= " issn='".$get_vars['issn']."' AND imprint LIKE"."'".$get_vars['publisher']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
	case "01100" :
	     $srchQuery .= " issn='".$get_vars['issn']."' AND imprint LIKE"."'".$get_vars['publisher']."%'";
	     break;
	//-----------------------------
	case "01011" :
	     $srchQuery .= " issn='".$get_vars['issn']."' AND subject_area LIKE"."'%".$get_vars['subject']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
	case "01010" :
	     $srchQuery .= " issn='".$get_vars['issn']."' AND subject_area LIKE"."'%".$get_vars['subject']."%'";
	     break;
	     
    case "01001" :
	     $srchQuery .= " issn='".$get_vars['issn']."' AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
	 case "01000" :
	     $srchQuery .= " issn='".$get_vars['issn']."'";
	      break;
	//----------------------------------
	 case "00111" :
	     $srchQuery .= " imprint LIKE"."'".$get_vars['publisher']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	   
	 case "00110" :
	     $srchQuery .= " imprint LIKE"."'".$get_vars['publisher']."%'"." AND subject_area LIKE"."'%".$get_vars['subject']."%'";
	     break;
	     
	 case "00101" :
	     $srchQuery .= " imprint LIKE"."'".$get_vars['publisher']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
	 case "00100" :
	     $srchQuery .= " imprint LIKE"."'".$get_vars['publisher']."%'";
	     break;
	//------------------------------------
	case "00011" :
	     $srchQuery .= " subject_area LIKE"."'%".$get_vars['subject']."%'"." AND type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
	case "00010" :
	     $srchQuery .= " subject_area LIKE"."'%".$get_vars['subject']."%'";
	     break;
	     
	case "00001" :
	     $srchQuery .= " type LIKE"."'".$get_vars['type']."%'";
	     break;
	     
    case "00000" :
	     $srchQuery .= " 1";
	    	        
	} //end switch
	
    //print ">>>>".$srchQuery;
   
    $result=$myEdit->sql($srchQuery);
 
    $count1=mysql_num_rows($result);
    $rpage=$PHP_SELF;
	 if(!$main)$main=0;
	 if(!$start)$start=0;
	 $sn=1;
	 if($start!=0)
		$sn=$start+1;
	$parameter="title=$title&issn=$issn&publisher=$publisher&subject=$subject&type=$type";
	//$parameter="search=$search&start_date=$start_date&end_date=$end_date&keyword=$keyword&count1=$count1";
	$myDisplay->sql_batching($srchQuery,$rpage,$parameter,$per_page,$max_rec,$main,$start);
  

if($count1 < 1) {
		print "<b>Sorry! No search result found!!!</b><br>";
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