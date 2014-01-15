<?php session_start();
include_once('config.php');
/*
 *header page, needs to be included in each page that participates in session
 *Author : Ravi K Shakya
 */

$inactive = $TIMEOUT;

if(isset($_SESSION['start']) ) {
        $session_life = time() - $_SESSION['start'];
        if($session_life > $inactive){
            header("Location: logout.php");
        }
    }
    
$_SESSION['start'] = time();

//if session present, display appropriate header
if(isset($_SESSION['user'])){
    print "<h4> Hi ". $_SESSION['user'] . " | <a href = 'logout.php'> Logout </a></h4>";
}else{
    //if no session, prevent direct access, direct to log in page
    header("Location: login.php");
    exit;
}
?>