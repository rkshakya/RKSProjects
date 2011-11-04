<?php
session_start();
include "wrapper.php";
include "classes/class_edit.php";
$myEdit=new class_edit();

if(!$HTTP_SESSION_VARS["sess_id"])
	print "<script>window.location='login.php'</script>";


switch($HTTP_SESSION_VARS["sess_type"])
{
    case "admin":
		include "header_admin.php";
		break;
	case "general":
		include "header_general.php";
		
}

?>

