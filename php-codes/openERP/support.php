<?php
include './openerp.php';

/*
 *support.php -simple web form to file helpdesk tickets into crm.helpdesk of openERP
 *dependency - openerp.php, xmlrpc.inc
 *
 *Author - Ravi Kishor Shakya
 */

function preparedata($data){
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    $data = urlencode($data);  
    return $data;
}

if($_SERVER["REQUEST_METHOD"] == "POST"){
	$errMessages = array();  //holds error messages post validation
	$formData = array();
	//put mandatory fields
	$mandatory = array("fname" => "fname", "lname" => "lname", "email" => "email", "subject" => "subject", "message" => "message");

	foreach($_POST as $field => $value){		
		$formData[$field] = $value;
		
		if ( strlen($value) == 0 && in_array($field, $mandatory) ) { 
			$errMessages[$field] = 'Required.';
		}		
		if( $field == 'email' && !preg_match("/([\w\-]+\@[\w\-]+\.[\w\-]+)/",$value) ){
			$errMessages[$field] = 'Please enter valid email';
		}
	}

	if(empty($errMessages)){
	print $formData['fname'];
	print $formData['message'];
	//on submit
	//check if customer already exists(see if exists in res.partner)
	$pid = 0;
	$srch_arr = array(new xmlrpcval(array(new xmlrpcval("name" , "string"),new xmlrpcval("=","string"),new xmlrpcval($formData['fname']." ".$formData['lname'],"string")),"array"),);
	$pid = search('res.partner', $srch_arr);
	print_r($pid);
	if(!empty($pid)){
		print "A";
		//print $pid[0]['me'];
		print_r($pid[0]);
		print('Kindof:'.$pid[0]->scalarval());		
		$partid = $pid[0]->scalarval();
		
	} else {
		$nsrch_arr = array(new xmlrpcval(array(new xmlrpcval("name" , "string"),new xmlrpcval("like","string"),new xmlrpcval($formData['fname']." ".$formData['lname'],"string")),"array"),);
		$pid = search('res.partner', $nsrch_arr);
		//print_r($pid[0]);
		if(!empty($pid)){
			print ('Partner Id: '.$pid[0]->scalarval());
			$partid = $pid[0]->scalarval();
		}else{
			//assume partner does not exist in our DB -- create one
			$arrayVal = array(
				'name'=>new xmlrpcval($formData['fname'].' '.$formData['lname'], "string") ,				
			);
			$partid = execute('res.partner', 'create', $arrayVal);
		}
	}
	
	print "\n PARTNER ID: ".$partid;
	//if yes, file helpdesk tkt
	$arrayTkt = array(
		'name'=>new xmlrpcval($formData['subject'], "string") ,
		'description'=>new xmlrpcval($formData['message'].' Email:'.$formData['email']. ' Phone: ' .$formData['phone'], "string") ,	
		//'description'=>new xmlrpcval("Aladdin", "string") ,	
		'partner_id'=>new xmlrpcval($partid, "string"),
	);
	$tktid = execute('crm.helpdesk', 'create', $arrayTkt);
	print "\nFiled ticket ID: ".$tktid;
	exit();
	}	
}
?>
<html>
<head>
<title>Customer Support Request</title>
<head>
<body>
Please fill up the form for support request
<table border = "1">
<form name = "support" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
<?php if (isset($errMessages)){ ?>
			<tr>
			<td align="center"><font color="red" face="Arial, Helvetica, sans-serif" size="+1">Error! Please correct the errors in form fields below.</font></td>
			</tr>
<?php } ?>
<tr><td colspan = "2"><i>Fields marked * are mandatory</i></td></tr>
<tr><td>First Name*</td><td><input type = "text" name="fname" id="fname" value="<?php echo htmlspecialchars($formData["fname"]); ?>"/></td><td><?php if (isset($errMessages) && isset($errMessages['fname']))  { ?>
			<font color="red"><?php echo $errMessages['fname']; ?></font>
			<?php } ?></td></tr>
<tr><td>Last Name*</td><td><input type = "text" name="lname" id="lname" value="<?php echo htmlspecialchars($formData["lname"]); ?>"/></td><td><?php if (isset($errMessages) && isset($errMessages['lname']))  { ?>
			<font color="red"><?php echo $errMessages['lname']; ?></font>
			<?php } ?></td></tr>
<tr><td>Email*</td><td><input type = "text" name="email" id="email" value="<?php echo htmlspecialchars($formData["email"]); ?>" /></td><td><?php if (isset($errMessages) && isset($errMessages['email']))  { ?>
			<font color="red"><?php echo $errMessages['email']; ?></font>
			<?php } ?></td></tr>
<tr><td>Phone</td><td><input type = "text" name="phone" id="phone"/><td></tr>
<tr><td>Subject*</td><td><input type = "text" name="subject" id="subject" value="<?php echo htmlspecialchars($formData["subject"]); ?>"/><td><td><?php if (isset($errMessages) && isset($errMessages['subject']))  { ?>
			<font color="red"><?php echo $errMessages['subject']; ?></font>
			<?php } ?></td></tr>
<tr><td>Description*</td><td><textarea name = "message"> <?php echo htmlspecialchars($formData["message"]); ?></textarea></td><td><?php if (isset($errMessages) && isset($errMessages['message']))  { ?>
			<font color="red"><?php echo $errMessages['message']; ?></font>
			<?php } ?></td></tr>
<tr><td><input type = "submit" name="submit"/></td></tr>
</table>
</form>
</body>
</html>