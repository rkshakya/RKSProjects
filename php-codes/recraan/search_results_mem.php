<?php
include "funda.php";
include "./docs/class_display.php";


$mname = $get_vars['mname'];
$maddress = $get_vars['maddress'];
$mbranch = $get_vars['mbranch'];
$mbatch = $get_vars['mbatch'];


$myDisplay = new class_display($myEdit);
	if($mname) {
            $n = "1" ;
           } else {
           $n = "0";
           }
           
         if($maddress) {
            $a = "1" ;
           } else {
           $a = "0";
           }
         
          if($mbranch) {
            $br = "1" ;
           } else {
           $br = "0";
           }
           
           if($mbatch) {
            $ba = "1" ;
           } else {
           $ba = "0";
           } 
	
	$determinant = $n.$a.$br.$ba;
	

	$srchQuery = "SELECT SN, Name, Batch, Branch, Email FROM members WHERE "; 

	switch ($determinant) {
	
	case "1111" :
	     $srchQuery .= " Name LIKE "."'%".$mname."%'"." AND  HomeAddress LIKE"."'%".$maddress."%'"." AND Branch = '".$mbranch."' AND Batch = '".$mbatch."'" ;
	     break;
	
	case "1110" :
	     $srchQuery .= " Name LIKE "."'%".$mname."%'"." AND  HomeAddress LIKE"."'%".$maddress."%'"." AND Branch = '".$mbranch."' " ;
	     break;
	
	case "1101" :
	     $srchQuery .= " Name LIKE "."'%".$mname."%'"." AND  HomeAddress LIKE"."'%".$maddress."%'"."  AND Batch = '".$mbatch."'" ;
	     break;

	case "1100" :
	     $srchQuery .= " Name LIKE "."'%".$mname."%'"." AND  HomeAddress LIKE"."'%".$maddress."%'" ;
	     break;
 

	//------------------------------------------------------------

	case "1011" :
	     $srchQuery .= " Name LIKE "."'%".$mname."%'"."  AND Branch = '".$mbranch."' AND Batch = '".$mbatch."'" ;
	     break;

	case "1010" :
	     $srchQuery .= " Name LIKE "."'%".$mname."%'"."  AND Branch = '".$mbranch."' " ;
	     break;

	case "1001" :
	     $srchQuery .= " Name LIKE "."'%".$mname."%'"."  AND Batch = '".$mbatch."'" ;
	     break;

	case "1000" :
	     $srchQuery .= " Name LIKE "."'%".$mname."%'" ;
	     break;

	//---------------------------------------------------------------------
	case "0111" :
	     $srchQuery .= "  HomeAddress LIKE"."'%".$maddress."%'"." AND Branch = '".$mbranch."' AND Batch = '".$mbatch."'" ;
	     break;

	case "0110" :
	     $srchQuery .= "  HomeAddress LIKE"."'%".$maddress."%'"." AND Branch = '".$mbranch."' " ;
	     break;

	case "0101" :
	     $srchQuery .= "  HomeAddress LIKE"."'%".$maddress."%'"."  AND Batch = '".$mbatch."'" ;
	     break;

	case "0100" :
	     $srchQuery .= "  HomeAddress LIKE"."'%".$maddress."%'" ;
	     break;

	//-------------------

	case "0011" :
	     $srchQuery .= " Branch='".$mbranch."' AND Batch = '".$mbatch."'" ;
	     break;


	case "0010" :
	     $srchQuery .= " Branch = '".$mbranch."' " ;
	     break;

	case "0001" :
	     $srchQuery .= "  Batch = '".$mbatch."'" ;
	     break;

	case "0000" :
	     $srchQuery .= " 1" ;

	} //end switch
	     
	
	
	$result=$myEdit->sql($srchQuery);
	

	 $count1 = mysql_num_rows($result);
	 
    	 $rpage=$PHP_SELF;
	 if(!$main)$main=0;
	 if(!$start)$start=0;
	 $sn=1;
	 if($start!=0)
		$sn=$start+1;
	$parameter="mname=$mname&maddress=$maddress&mbranch=$mbranch&mbatch=$mbatch";
	
	$myDisplay->sql_batching($srchQuery,$rpage,$parameter,$per_page,$max_rec,$main,$start);
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML><HEAD><TITLE>RECRAAN, 200+ Nepalese alumni of REC(NIT)Rourkela :: Branch-wise</TITLE>
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">

<META NAME="keywords" content="RECRAAN, recraan, REC Nepalese Students, NIT Nepalese Students, NIT Rourkela, REC Rourkela, Nepalese students, Engineers, Alumni Association, Alumnus, Kathmandu, Nepal">
<META NAME="description" content="RECRAAN, an alumni association of 200+ Nepalese students who have graduated from 
            Regional Engineering College(REC) or National Institute of Technology(NIT) Rourkela, India, is based in Kathmandu, Nepal">

<LINK href="images/NITstyle.css" type=text/css rel=stylesheet>

<SCRIPT language=Javascript src="images/dt1.js">
</SCRIPT>

<META content="Microsoft FrontPage 5.0" name=GENERATOR></HEAD>
<BODY bottomMargin=0 bgColor=#f4f4f4 leftMargin=0 topMargin=0 rightMargin=0 
marginwidth="0" marginheight="0">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width=779 align=center 
bgColor=#ffffff border=0>
  <TBODY>
  <TR>
    <TD height=126>
      <TABLE height=125 cellSpacing=0 cellPadding=0 width=779 border=0>
        <TBODY>
        <TR>
          <TD width=316 height=92 rowSpan=2>
          <img border="0" src="images/frontLeft1.gif" width="316" height="12"><p style="margin-top: 0; margin-bottom: 0">
          <img border="0" src="images/frontLeft2.gif" width="316" height="78"></p>
          <p style="margin-top: 0; margin-bottom: 0">
          <img border="0" src="images/frontLeft3.gif" width="316" height="2"></TD>
          <TD width=89 rowSpan=2 height="92">
          <IMG height=92 alt="" 
            src="images/logo.gif" width=89></TD>
          <TD width=374 height=22>
          <img src="images/topRightMenu.jpg" border="0" usemap="#Map4" width="374" height="22"></TD></TR>
        <TR>
          <TD width=374 height=70>
          <IMG height=70 alt="" 
            src="images/frontRight.gif" width=374></TD></TR>
        <TR>
          <TD colSpan=2 rowSpan=2 height="33">
          <IMG height=34 src="images/topLeftMenu.gif" width=405 useMap=#Map3 border=0></TD>
          <TD vAlign=bottom bgColor=#bde3ff height=32>
            <TABLE height=22 cellSpacing=0 cellPadding=0 width=374 border=0>
              <FORM name=frmSearch action = "search_results.php" method = "get">
              <TBODY>
              <TR>
                <TD width=19 height=15><font face ="Verdana" size="0"><b>Search</b></font></TD>
		<TD>&nbsp;</TD>
		<TD><INPUT type = "text" name = "srch" size = "20" maxchar = "25"></TD>
		<TD>&nbsp;</TD>
		<TD><INPUT type = "Submit" name = "Submit" value ="Go"></TD>
                <TD width=30>&nbsp;</TD>
                <TD width=30 height=22>
                <IMG height=15 alt="" 
                  src="images/date.gif" width=25></TD>
                <TD class=arial2 width=208>
                  <SCRIPT language=Javascript class=txtgray2>
	    //document.write('9/27/2004 8:55:53 AM<br>')
        //var now = new Date('9/27/2004 8:55:53 AM'); 
        //document.write(now+"----"); 
		document.write("&nbsp;" + time);
                  </SCRIPT>
                </TD>
                </TR></FORM></TBODY></TABLE></TD></TR>
        <TR>
          <TD height=1>
          <IMG height=2 alt="" src="images/line.gif" 
            width=374></TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD vAlign=top>
      <TABLE cellSpacing=0 cellPadding=0 width=779 border=0 height="100%">
        <TBODY>
        <TR >
          <TD vAlign=top width=164 height="365">
            <TABLE cellSpacing=0 cellPadding=0 width=164 
              border=0 style="border-collapse: collapse" bordercolor="#111111"><TBODY>

              <TR>
                <TD width=164 height=25 bgcolor="#FFFFCC" valign="bottom">
                <p style="margin-left: 10"><b>
                <font face="Verdana" size="1" color="#000099">
                <span style="vertical-align: middle; text-decoration:underline">
                <a href="aboutRECRAAN.htm"><font size="1" color="#00247E">About 
                RECRAAN</font></a></span></font></b></TD></TR>
              <TR>
                <TD height=25 bgcolor="#FFFFCC" width="164" valign="bottom">
                <p style="margin-left: 10"><u><b>
                <font face="Verdana" size="1" color="#000099">
                <a href="members.htm"><font size="1" color="#000099">Members</font></a></font></b></u></TD></TR>
              <tr>
                <TD height=25 bgcolor="#FFFFCC" width="164" valign="bottom">
                <p style="margin-left: 10"><u><b>
                <font face="Verdana" size="1" color="#000099">
                <a href="activities.htm"><font size="1" color="#000099">
                Activities</font></a></font></b></u></TD>
              </tr>
              <TR>
                <TD height=25 bgcolor="#FFFFCC" width="164" valign="bottom">
                <p style="margin-left: 10"><u><b>
                <font face="Verdana" size="1" color="#000099">
                <a href="achievements.htm"><font size="1" color="#000099">Achievements</font></a></font></b></u></TD></TR>
              <TR>
                <TD height=25 bgcolor="#FFFFCC" width="164" valign="bottom">
                <p style="margin-left: 10"><u><b>
                <font face="Verdana" size="1" color="#000099">
                <a href="photogallery.htm"><font size="1" color="#000099">Photo Gallery</font></a></font></b></u></TD></TR>
              <TR>
                <TD height=25 bgcolor="#FFFFCC" width="164" valign="bottom">
                <p style="margin-left: 10"><u><b>
                <font face="Verdana" size="1" color="#000099">
                <a href="board.htm"><font size="1" color="#000099">Executive Board</font></a></font></b></u></TD></TR>
              <TR>
                <TD height=25 bgcolor="#FFFFCC" width="164" valign="bottom">
                <p style="margin-left: 10"><u><b>
                <font face="Verdana" size="1" color="#000099">
                <a href="contacts.htm"><font size="1" color="#000099">Contacts</font></a></font></b></u></TD></TR>
              <TR>
                <TD width=164 height=25 bgcolor="#FFFFCC" valign="bottom">
                <p style="margin-left: 10"><b>
                <font face="Verdana" size="1" color="#000099">
                <span style="vertical-align: middle; text-decoration:underline">
                <a href="search.php"><font size="1" color="#800080">Search(Advanced)</font></a></span></font></b></TD></TR>
              <TR>
                <TD height=25 bgcolor="#FFFFCC" width="164">
                &nbsp;</TD></TR>
              <TR >
                <TD ID = "menuLeft" bgcolor="#C6EFFF" width="164" valign="top" height="121">
                <p style="margin-top: 12; margin-bottom: 2" align="center"><b>
                <font face="Times New Roman" size="1" color="#660066">President</font></b></p>
                <p style="margin-top: 2; margin-bottom: 2" align="center"><b>
                <font face="Times New Roman" size="1" color="#660066">RECRAAN</font></b></p>
                <script language="JavaScript">contAddress();</script>
                </TD></TR>
              </TBODY></TABLE></TD>



<TD ID="menuRight" vAlign=top width=615 height="365">
<p class="MsoBodyText" style="line-height: 150%; margin-left: 20; margin-top: 20; margin-bottom: 10" align="justify">
<b><font face="Book Antiqua" color="#00247E">Results for Member Search</font></b></p>

 
<?php
if($count1 < 1) {
	
		print "<i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sorry! No search result found!!!</i><br>";
} else {

print "<table>";

print "<tr><td><table><tr><td ><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total [<font color='red'>$count1</font>] Records found.</i></td>";
print "<td>";
 for($i=0; $i<=35; $i++){
	print "&nbsp;";
	}
	
print "</td><td>&nbsp;</td><td ><i>Displaying Page <font color='red'>".$myDisplay->getCurrentPage()."</font> out of <font color='red'>".$myDisplay->getTotalPages()."</font> Pages.</i></td>";
print "</tr></table></td></tr>";

?>

  
  <form name="form" method="post">
<tr><td>
<table align="center" width="100%"  bgcolor = "#FFFCF0" border="2"" cellspacing="1" cellpadding="2" style="border-collapse: collapse" bordercolor="#CCCCCC">
	<tr>
	
		
		<td bgcolor="#FEF5DA">
		<div align="center"><b><font color="#0000CC">S.No.</font></b></div></td>
		<td bgcolor="#FEF5DA">
		<div align="center"><b><font color="#0000CC">Name</font></b></div></td>
		<td bgcolor="#FEF5DA">
		<div align="center"><b><font color="#0000CC">Batch</font></b></div></td>
		<td bgcolor="#FEF5DA">
		<div align="center"><b><font color="#0000CC">Branch</font></b></div></td>
		<td bgcolor="#FEF5DA">
		<div align="center"><b><font color="#0000CC">Email</font></b></div></td>
		<td bgcolor="#FEF5DA">
		<div align="center"><b><font color="#0000CC">Option</font></b></div></td>
		
	</tr>
	
 <?php  
 
		
		while($row = $myDisplay->getNextRow())

		{
			print "<tr>";
			
    	print "<td bgcolor='#FFFCF0'><div align='center'><font color='#000001'>".$row['SN']."</font></div></td>";
    	print "<td bgcolor='#FFFCF0'><div align='center'>".$row['Name']."</div></td>";
    	
    	print "<td bgcolor='#FFFCF0'><div align='center'>".$row['Batch']."</div></td>";
    	print "<td bgcolor='#FFFCF0'><div align='center'>".$row['Branch']."</div></td>";
    	
    	print "<td bgcolor='#FFFCF0'><div align='center'>".$row['Email']."</div></td>";
    	print "<td bgcolor='#FFFCF0'><div align='center'><a href = 'findMemberDetails.php?memberID=".$row['SN']."'><font color= 'red'><u>Details</u></font></a></div></td>";
    	
    
			
			
			print "</tr>";
			$sn++;
		}//end while

?>


</table>
</td></tr>

<tr><td>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<?
	print "<tr bgcolor='#FEF5DA'>";
	print "<td  align='center'>".$myDisplay->getNavigationBar()."</td>";
	print "</tr>";

?>

</table>
</td></tr>

</form>			
</table> 
  	
<?  
        } //end if-else  

?>

</TD>
	
          </TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD vAlign=top bgColor=#ffcc66 height="30">
      <DIV class=biglinks align=center style="width: 779; height: 30">
      <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR>
          <TD width="5%">&nbsp;</TD>
          <TD width="90%">
            <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
              <TBODY>
              <script language=JavaScript>putFooter();</script>
              </TBODY>
            </TABLE>
          </TD>
          <TD width="5%">&nbsp;</TD>
        </TR>
        </TBODY>
      </TABLE>
      </DIV>
    </TD>
  </TR>
</TBODY></TABLE>
                
<script language=JavaScript>
document.all('menuLeft').height=document.all('menuRight').clientHeight-225;
coll=document.all.tags('DIV');coll[0].style.display="none";
</script>  
              
<MAP name=Map3>
<AREA shape=RECT 
  target=_blank coords="48, 12, 174, 29" href="http://www.nitrkl.ac.in">
<AREA 
  shape=RECT coords="226, 13, 339, 31" 
  href="docs/newsletter.pdf"></MAP>
<MAP 
  name=Map4>
<AREA shape=RECT coords="22, 4, 71, 19" 
  href="news.php" >
<AREA shape=RECT 
  coords="279, 6, 335, 18" href="members.htm" onClick="window.open('admin.htm','adminWin','height=400,width=700, left=50 scrollbars=1 status=1');">
<AREA 
  shape=RECT coords="185, 6, 252, 19" 
  href="feedback.php">
<AREA shape=RECT 
  coords="97, 5, 156, 19" href="notices.php"></MAP>
                </BODY></HTML>



