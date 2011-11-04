

<html>
<head>
<title> Bazaar International Intranet Administration </title>
<meta name="Generator" content="EditPlus">
<meta name="Author" content="">
<meta name="Keywords" content="">
<meta name="Description" content="">
<link rel="stylesheet" type="text/css" href="style.css">
<script src="date.js"></script>
<!--
<script language="JavaScript1.2" src="js/fw_menu.js"></script>

<script language="JavaScript1.2">

function fwLoadMenus() {
  //if (window.fw_menu_0) return;


window.fw_menu_1 = new Menu("root",145,20,"verdana",10,"#000000","#ffffff","#C5D6D8","#769397");
fw_menu_1.addMenuItem(": Search " ,"location='transentry.php'");	
fw_menu_1.addMenuItem(": Add" ,"location='transrepair.php'");	
fw_menu_1.addMenuItem(": Regular Observation ","location='regobservation.php'");
fw_menu_1.addMenuItem(": Edit/Delete ","location='transRegularObs.php'");

fw_menu_1.hideOnMouseOut=true;

window.fw_menu_2 = new Menu("root",140,20,"verdana",10,"#000000","#ffffff","#C5D6D8","#769397");
fw_menu_2.addMenuItem(": Register Complaints ","location='regcomplaint.php'");
fw_menu_2.addMenuItem(": Work Completed ","location='workcompleted.php'");
fw_menu_2.addMenuItem(": Work Completed ","location='work.php'");

fw_menu_2.hideOnMouseOut=true;

 fw_menu_2.writeMenus();

}

</script>
-->
</head>

<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">


<tr bgcolor="#999ECC">

<td bgcolor="#999ECC" height="15"><h3><font color="white">Bazaar International Intranet Administration</font></h3></td>
<td colspan="2"><IMG SRC = "images/logo.gif" ALIGN = "right"></td>
<!--<img src="images/banner.gif" width="780" height="80" border="0" alt="" />-->
</tr>

<tr>
<td bgcolor="#6B73B5" height="5" colspan="2"></td>
</tr>

<tr>
<td bgcolor="#e6e6fa" height="15"><div align="left" class="date">Logged in as:  
<font color="#000000"><?echo $HTTP_SESSION_VARS["sess_name"]?>
</font> </div></td>
<td bgcolor="#9EB8BB" height="15">

<div align=right class=date><script>document.write(dys[dy] + ", " + months[month] + " " + day + ", " +  " " + year);</script></div></td>
</tr>

<tr>
<td bgcolor="#6B73B5" height="20" colspan="2">

<script language="JavaScript1.2">fwLoadMenus();
  
	<a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_5,1,121);" onfocus="if(this.blur)this.blur()" class="main">:: Items</a> 
	
	<a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_4,75,121);" onfocus="if(this.blur)this.blur()" class="main">:: Edit/Delete User</a> 

	<a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_2,110,121);" onfocus="if(this.blur)this.blur()" class="main">:: Change Password</a> 

   
    <a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_6,170,121);" onfocus="if(this.blur)this.blur()" class="main">::  Logout</a> 
	
	<a href="feederreg.php" class="main">:: Feeder Registration</a> 

	<a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_1,343,121);" onfocus="if(this.blur)this.blur()" class="main">:: Transformer</a> 
	
	<a href="maintainhistory.php" class="main">:: Maintenance History</a> 
   
    <a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_3,560,121);" onfocus="if(this.blur)this.blur()" class="main">:: 
    Search</a> 
   
 	<a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_7,615,121);" onfocus="if(this.blur)this.blur()" class="main">:: Consumer</a> 



    &nbsp;<a href="adduser.php" class="main">:: Add a new user</a> &nbsp;<a href="edituser.php" class="main">::Edit/Delete User</a>&nbsp;
	
	<a href="pwdchange.php" class="main">:: Change Password</a> &nbsp;<a href="login.php" class="main">:: Logout</a></td>
	
</tr>



<tr bgcolor="#F1F7F8">
<td align="center" colspan="2">
<br />