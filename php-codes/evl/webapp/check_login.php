<?php
	session_start();
	
	include 'core/db_connect.php';
	
	$username=$_POST['login'];
	$pass=$_POST['password'];
	
	$query="select md5('$pass') from DUAL";
	$result=mysql_query($query);
	$row=mysql_fetch_array($result);
	$password=$row[0];
	
	$sql = "SELECT id,username,password FROM evl_users WHERE username='$username' and password='$password'";
	$result1=mysql_query($sql);
	$row1=mysql_fetch_array($result1);

	/*echo $row1;exit;*/
	if($row1==null){
		$_SESSION['id'] = "Access Denied. Invalid Information!";
		header('Location:index.php');
	}else {
		$_SESSION['u_id']=$row1[0];
		$_SESSION['username']=$row1[1];
		header('Location: dashboard.php');
	}
	
?>