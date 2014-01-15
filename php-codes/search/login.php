<?php session_start();
include_once('config.php');
/*
 *displays login page, authenticates and sets session
 *Author : Ravi K Shakya
 */

if(isset($_POST) and $_POST['submitForm'] == "Login" ){
$usrname = mysql_escape_string($_POST['username']);
$password = mysql_escape_string($_POST['password']);
$errors = array();
// Field Validation
if(empty($usrname) or empty($password)){
    $errors['login'] = "Either username or password field is blank";
}

if(count($errors) == 0){
    $con = new Mongo('mongodb://'.$UNAME.':'.$PWD.'@'.$HOST.':'.$PORT.'/'.$DB);
    if($con){
        $db = $con->$DB;
        $user = $db->$USERS;
        $qry = array("uname" => $usrname,"pwd" => md5($password));
        $result = $user->findOne($qry);
            if($result){                
                //set the session variable
                $_SESSION['user'] = $result['uname'];
                //redirect to search page
                header("Location: search.php");
                exit;
            }else{
                //set error message and remain in login page
                $errors['login'] = "Invalid username or password information.";
            }
    } else {
    die("Mongo DB not installed");
    }
}
} 
?>
<html>
    <head><title>Login Page</title></head>
<body>
   <h3><?php if(isset($errors['login'])) { print $errors['login']; } ?></h3>
    <form action="" method="POST">
    Name:
    <input type="text" id="username" name="username"  /><br/>
    Password:
    <input type="password" id="password" name="password" /><br/>  
    <input  name="submitForm" id="submitForm" type="submit" value="Login" />
    </form>
</body>
</html>