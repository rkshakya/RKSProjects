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
    $mandatory = array("uname" => "uname", "apikey" => "apikey", "apiurl" => "apiurl");
    
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
            $sql = "UPDATE evl_managedaccounts SET uname = '".$formData[uname]."', api_url = '". $formData[apiurl]."', api_key = '". $formData[apikey]."' WHERE id = '".$formData[id]."'";
        }else{
            $sql = "INSERT INTO evl_managedaccounts (uname, api_url, api_key)VALUES('$formData[uname]','$formData[apiurl]','$formData[apikey]')";
        }    
        $retval = mysql_query( $sql);
        if(! $retval ){
          die('Could not enter data: ' . mysql_error());
        }
                
        
        if($retval) {			
	    header('Location:managed.php');
        }       
        exit();
        }	
    } else if ($_SERVER["REQUEST_METHOD"] == "GET"){
        if (isset($_GET['id']) && is_numeric($_GET['id']) && $_GET['id'] > 0)
	 {
	 	// query database
		 $id = $_GET['id'];
		 $result = mysql_query("SELECT * FROM evl_managedaccounts WHERE id=$id") or die(mysql_error()); 
		 $row = mysql_fetch_array($result);
		 
		 // check that the 'id' matches up with a row in the databse
		 
		 if($row){
			 // get data from database
                         $formData['id'] = $row['id'];
			 $formData['uname'] = $row['uname'];
			 $formData['apiurl'] = $row['api_url'];
                         $formData['apikey'] = $row['api_key'];
                 }
        }
    }
    
 ?>   
    <html>
<head>
<title>IC Backoffice Application :: Add or edit account</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
Please fill up the information below:
<table border = "1">
<form name = "account" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
<?php if (isset($errMessages)){ ?>
<tr>
<td align="center"><font color="red" face="Arial, Helvetica, sans-serif" size="+1">Error! Please correct the errors in form fields below.</font></td>
</tr>
<?php } ?>
<tr><td colspan = "2"><i>Fields marked * are mandatory</i></td><?php if(isset($formData[id])) {  echo "<td><input type = 'hidden' name = 'id' value = '$formData[id]'/></td>"; } ?></tr>
<tr><td>Username*</td><td><input type = "text" name="uname" id="uname" value="<?php echo htmlspecialchars($formData["uname"]); ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['uname'])) { ?>
<font color="red"><?php echo $errMessages['uname']; ?></font>
<?php } ?></td></tr>
<tr><td>API URL*</td><td><input type = "text" name="apiurl" id="apiurl" value="<?php if(isset($formData["apiurl"])){ echo htmlspecialchars($formData["apiurl"]); } else { echo "http://www.instantcustomer.com"; } ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['apiurl'])) { ?>
<font color="red"><?php echo $errMessages['apiurl']; ?></font>
<?php } ?></td></tr>
<tr><td>API KEY*</td><td><input type = "text" name="apikey" id="apikey" value="<?php echo htmlspecialchars($formData["apikey"]); ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['apikey'])) { ?>
<font color="red"><?php echo $errMessages['apikey']; ?></font>
<?php } ?></td></tr>
<tr><td><input type = "submit" name="submit" value="submit"/></td></tr>
</table>
</form>

<?php    
}else{
	echo "Please Log In to the System.";
}
?>
</body>
</html>