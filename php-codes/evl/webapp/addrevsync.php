<?php 
ob_start();
session_start();

include 'include/header.php';

if(isset($_SESSION['username'])){
    //render form
    function preparedata($data){
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    $data = urlencode($data);
    return $data;
}

include 'core/db_connect.php';

if($_SERVER["REQUEST_METHOD"] == "POST"){
    $errMessages = array(); //holds error messages post validation
    $formData = array();
    //put mandatory fields
    $mandatory = array("sqlserver_host" => "sqlserver_host", "sqlserver_port" => "sqlserver_port","sqlserver_db" => "sqlserver_db", "sqlserver_user" => "sqlserver_user", "sqlserver_pass" => "sqlserver_pass");
    
    foreach($_POST as $field => $value){	
        $formData[$field] = $value;
        
        if ( strlen($value) == 0 && in_array($field, $mandatory) ) {
        $errMessages[$field] = 'Required.';
        }	        
    }    
    
    if(empty($errMessages)){
        //print $formData['fname'];
        //print $formData['lname'];
        //print $formData['phone'];
        //print $formData['email'];
        //on submit - we are clean at this point
        //create client
        if(isset($formData[id])){
            $sql = "UPDATE evl_revsync_settings SET sqlserver_host = '".$formData[sqlserver_host]."', sqlserver_port = '". $formData[sqlserver_port]."', sqlserver_user = '". $formData[sqlserver_user]."', sqlserver_pass = '". $formData[sqlserver_pass]."', include_autosync = '". $formData[include_autosync]."', sqlserver_db = '". $formData[sqlserver_db]."' WHERE id = '".$formData[id]."'";
        }else{
            $sql = "INSERT INTO evl_revsync_settings (sqlserver_host, sqlserver_port, sqlserver_db, sqlserver_user, sqlserver_pass, include_autosync)VALUES('$formData[sqlserver_host]','$formData[sqlserver_port]','$formData[sqlserver_db]', '$formData[sqlserver_user]', '$formData[sqlserver_pass]', '$formData[include_autosync]')";
        }    
        $retval = mysql_query( $sql);
        if(! $retval ){
          die('Could not enter data: ' . mysql_error());
        }
                
        
        if($retval) {			
	    header('Location:revsyncsettings.php');
        }       
        exit();
        }	
    } else if ($_SERVER["REQUEST_METHOD"] == "GET"){
        if (isset($_GET['id']) && is_numeric($_GET['id']) && $_GET['id'] > 0)
	 {
	 	// query database
		 $id = $_GET['id'];
		 $result = mysql_query("SELECT * FROM evl_revsync_settings WHERE id=$id") or die(mysql_error()); 
		 $row = mysql_fetch_array($result);
		 
		 // check that the 'id' matches up with a row in the databse
		 
		 if($row){
			 // get data from database
                         $formData['id'] = $row['id'];
			 $formData['sqlserver_host'] = $row['sqlserver_host'];
			 $formData['sqlserver_port'] = $row['sqlserver_port'];
                         $formData['sqlserver_db'] = $row['sqlserver_db'];
			 $formData['sqlserver_user'] = $row['sqlserver_user'];
			 $formData['sqlserver_pass'] = $row['sqlserver_pass'];
			 $formData['include_autosync'] = $row['include_autosync'];
                 }
        }
    }
    
 ?>   
    <html>
<head>
<title>IC Backoffice Application :: Add or edit Evolve DB settings</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
Please fill up the information below:
<table border = "1">
<form name = "settings" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
<?php if (isset($errMessages)){ ?>
<tr>
<td align="center"><font color="red" face="Arial, Helvetica, sans-serif" size="+1">Error! Please correct the errors in form fields below.</font></td>
</tr>
<?php } ?>
<tr><td colspan = "2"><i>Fields marked * are mandatory</i></td><?php if(isset($formData[id])) {  echo "<td><input type = 'hidden' name = 'id' value = '$formData[id]'/></td>"; } ?></tr>
<tr><td>SQLServer Host*</td><td><input type = "text" name="sqlserver_host" id="sqlserver_host" value="<?php echo htmlspecialchars($formData["sqlserver_host"]); ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['sqlserver_host'])) { ?>
<font color="red"><?php echo $errMessages['sqlserver_host']; ?></font>
<?php } ?></td></tr>
<tr><td>SQLServer Port*</td><td><input type = "text" name="sqlserver_port" id="sqlserver_port" value="<?php if(isset($formData["sqlserver_port"])){ echo htmlspecialchars($formData["sqlserver_port"]); } else {echo '1433';}  ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['sqlserver_port'])) { ?>
<font color="red"><?php echo $errMessages['sqlserver_port']; ?></font>
<?php } ?></td></tr>
<tr><td>SQLServer DB*</td><td><input type = "text" name="sqlserver_db" id="sqlserver_db" value="<?php echo htmlspecialchars($formData["sqlserver_db"]); ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['sqlserver_db'])) { ?>
<font color="red"><?php echo $errMessages['sqlserver_db']; ?></font>
<?php } ?></td></tr>
<tr><td>SQLServer User*</td><td><input type = "text" name="sqlserver_user" id="sqlserver_user" value="<?php echo htmlspecialchars($formData["sqlserver_user"]); ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['sqlserver_user'])) { ?>
<font color="red"><?php echo $errMessages['sqlserver_user']; ?></font>
<?php } ?></td></tr>
<tr><td>SQLServer Password*</td><td><input type = "text" name="sqlserver_pass" id="sqlserver_pass" value="<?php echo htmlspecialchars($formData["sqlserver_pass"]); ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['sqlserver_pass'])) { ?>
<font color="red"><?php echo $errMessages['sqlserver_pass']; ?></font>
<?php } ?></td></tr>
<tr><td>Autosync?</td><td><input type="radio" name="include_autosync" value="1" <?php if($formData[include_autosync] == '1' or (!isset($formData[include_autosync])) ) { echo 'checked'; } ?> >Yes<input type="radio" name="include_autosync" value="0" <?php if($formData[include_autosync] == '0') { echo 'checked'; } ?>>No</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type = "submit" name="submit" value="submit"/></td></tr>
</table>
</form>

<?php    
}else{
	echo "Please Log In to the System.";
}
?>
</body>
</html>