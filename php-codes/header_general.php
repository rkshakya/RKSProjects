<html>
<head>
<title> Bazaar International Intranet Administration </title>
<meta name="Generator" content="EditPlus">
<meta name="Author" content="">
<meta name="Keywords" content="">
<meta name="Description" content="">
<link rel="stylesheet" type="text/css" href="style.css">
<script src="date.js"></script>

<script language="JavaScript1.2" src="js/fw_menu.js"></script>

<script language="JavaScript1.2">
<!--
function fwLoadMenus() {
  if (window.fw_menu_0) return;

  window.fw_menu_0 = new Menu("root",110,20,"arial",12,"#000000","#ffffff","#FF9800","#BE7000");
  fw_menu_0.addMenuItem("Test1","location='#'");
  fw_menu_0.addMenuItem("Test1","location='#'");
   fw_menu_0.hideOnMouseOut=true;



window.fw_menu_2 = new Menu("root",140,20,"verdana",10,"#000000","#ffffff","#e6e6fa","#6B73B5");
fw_menu_2.addMenuItem(": Search Client ","location='search_clients.php'");
//fw_menu_2.addMenuItem(": Add new client ","location='add_clients.php'");
//fw_menu_2.addMenuItem(": Edit/Delete client ","location='edit_clients.php'");

fw_menu_2.hideOnMouseOut=true;




window.fw_menu_5 = new Menu("root",110,20,"verdana",10,"#000000","#ffffff","#e6e6fa","#6B73B5");
fw_menu_5.addMenuItem(": Search User ","location='search_user.php'");
//fw_menu_5.addMenuItem(": Add new User ","location='adduser.php'"); 
//fw_menu_5.addMenuItem(": Edit/Delete User ","location='edituser.php'"); 

fw_menu_5.hideOnMouseOut=true;




window.fw_menu_4 = new Menu("root",145,20,"verdana",10,"#000000","#ffffff","#e6e6fa","#6B73B5");
fw_menu_4.addMenuItem(": Search Items","location='search_items.php'"); 
//fw_menu_4.addMenuItem(": Add New Item ","location='add_items.php'");
//fw_menu_4.addMenuItem(":Edit/Delete Items  ","location='edit_items.php'");



fw_menu_4.hideOnMouseOut=true;




 fw_menu_3.writeMenus();


} // fwLoadMenus()

//-->

</script>
</head>

<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">


<tr bgcolor="#999ECC">

<td bgcolor="#999ECC" height="75"><h3><font color="white">Bazaar International Intranet Administration</font></h3></td>
<td colspan="2"><IMG SRC = "images/logo.gif" ALIGN = "right"></td>

</tr>

<tr>
<td bgcolor="#6B73B5" height="5" colspan="2"></td>
</tr>

<tr>
<td bgcolor="#e6e6fa" height="15"><div align="left" class="date">Logged in as:  
<font color="#000000"><?echo $HTTP_SESSION_VARS["sess_name"]; print "[".$HTTP_SESSION_VARS["sess_type"]."]";?>
</font> </div></td>
<td bgcolor="#e6e6fa" height="15">
<div align="right" class=date><script> document.write(dys[dy] + ", " + months[month] + " " + day + ", " +  " " + year);</script></div></td>
</tr>


<tr>
<td bgcolor="#999ECC" height="20" colspan="2">
<script language="JavaScript1.2">fwLoadMenus();</script>
    <!-- abha -->

	<a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_5,1,115);" onfocus="if(this.blur)this.blur()" class="main">:: Users</a> 
	
	<a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_4,50,115);" onfocus="if(this.blur)this.blur()" class="main">::Items</a> 

	<a href="#" onMouseOut="FW_startTimeout();"  onMouseOver="window.FW_showMenu(window.fw_menu_2,90,115);" onfocus="if(this.blur)this.blur()" class="main">:: Clients</a> 

    
    
    &nbsp;<a href="pwdchange.php" class="main">:: Change Password</a> &nbsp;<a href="login.php" class="main">:: Logout</a></td>
</tr>



<tr bgcolor="#F1F7F8">
<td align="center" colspan="2">
<br />


