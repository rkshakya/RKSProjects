<?php
/*
 *invalidates session, logout
 *Author : Ravi K Shakya
 */
session_start();
if(isset($_SESSION['user'])){
  unset($_SESSION['user']);
}
session_destroy();
header("Location: login.php");
exit;
?> 