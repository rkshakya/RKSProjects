<?php
include './openerp.php';

/*
 *lead_capture.php -simple web form to capture leads and populate into crm.lead of openERP
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
	$mandatory = array("name" => "name", "subject" => "subject", "email" => "email", "phone"=>"phone");

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
	//on submit- create new crm.lead record
	$pid = 0;	
	if($formData['optin'] == 'Yes'){
		$optin = true;
		$optout = false;
	}else{
		$optin = false;
		$optout = true;
	}
	$arrayLead = array(		
		'contact_name'=>new xmlrpcval($formData['name'], "string") ,	
		'function'=>new xmlrpcval($formData['designation'], "string") ,	
		'name'=>new xmlrpcval($formData['subject'], "string") ,
		'email_from'=>new xmlrpcval($formData['email'], "string") ,
		'phone'=>new xmlrpcval($formData['phone'], "string") ,
		'mobile'=>new xmlrpcval($formData['mobile'], "string") ,
		'street'=>new xmlrpcval($formData['address'], "string") ,
		'street2'=>new xmlrpcval($formData['address1'], "string") ,
		'city'=>new xmlrpcval($formData['city'], "string") ,
		'zip'=>new xmlrpcval($formData['zip'], "string") ,
		'country_id'=>new xmlrpcval($formData['country'], "int") ,
		'optin'=>new xmlrpcval($optin, "boolean") ,
		'optout'=>new xmlrpcval($optout, "boolean") ,
		'channel_id'=>new xmlrpcval("1", "int") ,
		'lead'=>new xmlrpcval("lead", "string") ,
		'active'=>new xmlrpcval("1", "boolean")
	);	
	$leadid = execute('crm.lead', 'create', $arrayLead);
	print "\nCreated Lead ID: ".$leadid;
	exit();
	}	
}
?>

<html>
<head>
<title>Customer Inquiry</title>
<head>
<body>
Please fill up the form so that our customer support staff will contact/help you
<table border = "1">
<form name = "support" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
<tr><td><i>(Fields marked * are mandatory)</i></td></tr>
<?php if (isset($errMessages)){ ?>
			<tr>
			<td align="center"><font color="red" face="Arial, Helvetica, sans-serif" size="+1">Error! Please correct the errors in form fields below.</font></td>
			</tr>
<?php } ?>
<tr><td>Name*</td><td><input type = "text" name="name" id="name" value="<?php echo htmlspecialchars($formData["name"]); ?>"/></td><td><?php if (isset($errMessages) && isset($errMessages['name']))  { ?>
			<font color="red"><?php echo $errMessages['name']; ?></font>
			<?php } ?></td></tr>
<tr><td>Designation</td><td><input type = "text" name="designation" id="designation" value="<?php echo htmlspecialchars($formData["designation"]); ?>"/></td></tr>
<tr><td>Inquiry/Subject*</td><td><input type = "text" name="subject" id="subject" size="60" value="<?php echo htmlspecialchars($formData["subject"]); ?>"/></td><td><?php if (isset($errMessages) && isset($errMessages['subject']))  { ?>
			<font color="red"><?php echo $errMessages['subject']; ?></font>
			<?php } ?></td></tr>
<tr><td>Email*</td><td><input type = "text" name="email" id="email" value="<?php echo htmlspecialchars($formData["email"]); ?>"/></td><td><?php if (isset($errMessages) && isset($errMessages['email']))  { ?>
			<font color="red"><?php echo $errMessages['email']; ?></font>
			<?php } ?></td></tr>
<tr><td>Phone*</td><td><input type = "text" name="phone" id="phone" value="<?php echo htmlspecialchars($formData["phone"]); ?>"/><td><td><?php if (isset($errMessages) && isset($errMessages['phone']))  { ?>
			<font color="red"><?php echo $errMessages['phone']; ?></font>
			<?php } ?></td></tr>
<tr><td>Mobile</td><td><input type = "text" name="mobile" id="mobile" value="<?php echo htmlspecialchars($formData["mobile"]); ?>"/><td></tr>
<tr><td>Address</td><td><input type = "text" name="address" id="address" size="60" value="<?php echo htmlspecialchars($formData["address"]); ?>"/><td></tr>
<tr><td>Address(Contd)</td><td><input type = "text" name="address1" id="address1" size="60" value="<?php echo htmlspecialchars($formData["address1"]); ?>"/><td></tr>
<tr><td>City</td><td><input type = "text" name="city" id="city" value="<?php echo htmlspecialchars($formData["city"]); ?>"/><td></tr>
<tr><td>Zip</td><td><input type = "text" name="zip" id="zip" value="<?php echo htmlspecialchars($formData["zip"]); ?>"/><td></tr>
<tr><td>Country</td> <td><select name="country" id="country" size="0">                        
<option value="3" <?php if ($formData["country"]=="3" ) echo 'selected'; ?> >Afghanistan, Islamic State of</option>
<option value="16" <?php if ($formData["country"]=="16" ) echo 'selected'; ?> >Aland Islands</option>
<option value="6" <?php if ($formData["country"]=="6" ) echo 'selected'; ?> >Albania</option>
<option value="63" <?php if ($formData["country"]=="63" ) echo 'selected'; ?> >Algeria</option>
<option value="12" <?php if ($formData["country"]=="12" ) echo 'selected'; ?> >American Samoa</option>
<option value="1" <?php if ($formData["country"]=="1" ) echo 'selected'; ?> >Andorra, Principality of</option>
<option value="9" <?php if ($formData["country"]=="9" ) echo 'selected'; ?> >Angola</option>
<option value="5" <?php if ($formData["country"]=="5" ) echo 'selected'; ?> >Anguilla</option>
<option value="10" <?php if ($formData["country"]=="10" ) echo 'selected'; ?> >Antarctica</option>
<option value="4" <?php if ($formData["country"]=="4" ) echo 'selected'; ?> >Antigua and Barbuda</option>
<option value="11" <?php if ($formData["country"]=="11" ) echo 'selected'; ?> >Argentina</option>
<option value="7" <?php if ($formData["country"]=="7" ) echo 'selected'; ?> >Armenia</option>
<option value="15" <?php if ($formData["country"]=="15" ) echo 'selected'; ?> >Aruba</option>
<option value="14" <?php if ($formData["country"]=="14" ) echo 'selected'; ?> >Australia</option>
<option value="13" <?php if ($formData["country"]=="13" ) echo 'selected'; ?> >Austria</option>
<option value="17" <?php if ($formData["country"]=="17" ) echo 'selected'; ?> >Azerbaijan</option>
<option value="33" <?php if ($formData["country"]=="33" ) echo 'selected'; ?> >Bahamas</option>
<option value="24" <?php if ($formData["country"]=="24" ) echo 'selected'; ?> >Bahrain</option>
<option value="20" <?php if ($formData["country"]=="20" ) echo 'selected'; ?> >Bangladesh</option>
<option value="19" <?php if ($formData["country"]=="19" ) echo 'selected'; ?> >Barbados</option>
<option value="37" <?php if ($formData["country"]=="37" ) echo 'selected'; ?> >Belarus</option>
<option value="21" <?php if ($formData["country"]=="21" ) echo 'selected'; ?> >Belgium</option>
<option value="38" <?php if ($formData["country"]=="38" ) echo 'selected'; ?> >Belize</option>
<option value="26" <?php if ($formData["country"]=="26" ) echo 'selected'; ?> >Benin</option>
<option value="28" <?php if ($formData["country"]=="28" ) echo 'selected'; ?> >Bermuda</option>
<option value="34" <?php if ($formData["country"]=="34" ) echo 'selected'; ?> >Bhutan</option>
<option value="30" <?php if ($formData["country"]=="30" ) echo 'selected'; ?> >Bolivia</option>
<option value="31" <?php if ($formData["country"]=="31" ) echo 'selected'; ?> >Bonaire, Sint Eustatius and Saba</option>
<option value="18" <?php if ($formData["country"]=="18" ) echo 'selected'; ?> >Bosnia-Herzegovina</option>
<option value="36" <?php if ($formData["country"]=="36" ) echo 'selected'; ?> >Botswana</option>
<option value="35" <?php if ($formData["country"]=="35" ) echo 'selected'; ?> >Bouvet Island</option>
<option value="32" <?php if ($formData["country"]=="32" ) echo 'selected'; ?> >Brazil</option>
<option value="106" <?php if ($formData["country"]=="106" ) echo 'selected'; ?> >British Indian Ocean Territory</option>
<option value="29" <?php if ($formData["country"]=="29" ) echo 'selected'; ?> >Brunei Darussalam</option>
<option value="23" <?php if ($formData["country"]=="23" ) echo 'selected'; ?> >Bulgaria</option>
<option value="22" <?php if ($formData["country"]=="22" ) echo 'selected'; ?> >Burkina Faso</option>
<option value="25" <?php if ($formData["country"]=="25" ) echo 'selected'; ?> >Burundi</option>
<option value="117" <?php if ($formData["country"]=="117" ) echo 'selected'; ?> >Cambodia, Kingdom of</option>
<option value="48" <?php if ($formData["country"]=="48" ) echo 'selected'; ?> >Cameroon</option>
<option value="39" <?php if ($formData["country"]=="39" ) echo 'selected'; ?> >Canada</option>
<option value="53" <?php if ($formData["country"]=="53" ) echo 'selected'; ?> >Cape Verde</option>
<option value="124" <?php if ($formData["country"]=="124" ) echo 'selected'; ?> >Cayman Islands</option>
<option value="41" <?php if ($formData["country"]=="41" ) echo 'selected'; ?> >Central African Republic</option>
<option value="216" <?php if ($formData["country"]=="216" ) echo 'selected'; ?> >Chad</option>
<option value="47" <?php if ($formData["country"]=="47" ) echo 'selected'; ?> >Chile</option>
<option value="49" <?php if ($formData["country"]=="49" ) echo 'selected'; ?> >China</option>
<option value="55" <?php if ($formData["country"]=="55" ) echo 'selected'; ?> >Christmas Island</option>
<option value="40" <?php if ($formData["country"]=="40" ) echo 'selected'; ?> >Cocos (Keeling) Islands</option>
<option value="50" <?php if ($formData["country"]=="50" ) echo 'selected'; ?> >Colombia</option>
<option value="119" <?php if ($formData["country"]=="119" ) echo 'selected'; ?> >Comoros</option>
<option value="43" <?php if ($formData["country"]=="43" ) echo 'selected'; ?> >Congo</option>
<option value="42" <?php if ($formData["country"]=="42" ) echo 'selected'; ?> >Congo, The Democratic Republic of the</option>
<option value="46" <?php if ($formData["country"]=="46" ) echo 'selected'; ?> >Cook Islands</option>
<option value="51" <?php if ($formData["country"]=="51" ) echo 'selected'; ?> >Costa Rica</option>
<option value="98" <?php if ($formData["country"]=="98" ) echo 'selected'; ?> >Croatia</option>
<option value="52" <?php if ($formData["country"]=="52" ) echo 'selected'; ?> >Cuba</option>
<option value="54" <?php if ($formData["country"]=="54" ) echo 'selected'; ?> >Curaçao</option>
<option value="56" <?php if ($formData["country"]=="56" ) echo 'selected'; ?> >Cyprus</option>
<option value="57" <?php if ($formData["country"]=="57" ) echo 'selected'; ?> >Czech Republic</option>
<option value="60" <?php if ($formData["country"]=="60" ) echo 'selected'; ?> >Denmark</option>
<option value="59" <?php if ($formData["country"]=="59" ) echo 'selected'; ?> >Djibouti</option>
<option value="61" <?php if ($formData["country"]=="61" ) echo 'selected'; ?> >Dominica</option>
<option value="62" <?php if ($formData["country"]=="62" ) echo 'selected'; ?> >Dominican Republic</option>
<option value="225" <?php if ($formData["country"]=="225" ) echo 'selected'; ?> >East Timor</option>
<option value="64" <?php if ($formData["country"]=="64" ) echo 'selected'; ?> >Ecuador</option>
<option value="66" <?php if ($formData["country"]=="66" ) echo 'selected'; ?> >Egypt</option>
<option value="211" <?php if ($formData["country"]=="211" ) echo 'selected'; ?> >El Salvador</option>
<option value="88" <?php if ($formData["country"]=="88" ) echo 'selected'; ?> >Equatorial Guinea</option>
<option value="68" <?php if ($formData["country"]=="68" ) echo 'selected'; ?> >Eritrea</option>
<option value="65" <?php if ($formData["country"]=="65" ) echo 'selected'; ?> >Estonia</option>
<option value="70" <?php if ($formData["country"]=="70" ) echo 'selected'; ?> >Ethiopia</option>
<option value="73" <?php if ($formData["country"]=="73" ) echo 'selected'; ?> >Falkland Islands</option>
<option value="75" <?php if ($formData["country"]=="75" ) echo 'selected'; ?> >Faroe Islands</option>
<option value="72" <?php if ($formData["country"]=="72" ) echo 'selected'; ?> >Fiji</option>
<option value="71" <?php if ($formData["country"]=="71" ) echo 'selected'; ?> >Finland</option>
<option value="76" <?php if ($formData["country"]=="76" ) echo 'selected'; ?> >France</option>
<option value="80" <?php if ($formData["country"]=="80" ) echo 'selected'; ?> >French Guyana</option>
<option value="217" <?php if ($formData["country"]=="217" ) echo 'selected'; ?> >French Southern Territories</option>
<option value="77" <?php if ($formData["country"]=="77" ) echo 'selected'; ?> >Gabon</option>
<option value="85" <?php if ($formData["country"]=="85" ) echo 'selected'; ?> >Gambia</option>
<option value="79" <?php if ($formData["country"]=="79" ) echo 'selected'; ?> >Georgia</option>
<option value="58" <?php if ($formData["country"]=="58" ) echo 'selected'; ?> >Germany</option>
<option value="81" <?php if ($formData["country"]=="81" ) echo 'selected'; ?> >Ghana</option>
<option value="82" <?php if ($formData["country"]=="82" ) echo 'selected'; ?> >Gibraltar</option>
<option value="89" <?php if ($formData["country"]=="89" ) echo 'selected'; ?> >Greece</option>
<option value="84" <?php if ($formData["country"]=="84" ) echo 'selected'; ?> >Greenland</option>
<option value="78" <?php if ($formData["country"]=="78" ) echo 'selected'; ?> >Grenada</option>
<option value="87" <?php if ($formData["country"]=="87" ) echo 'selected'; ?> >Guadeloupe (French)</option>
<option value="92" <?php if ($formData["country"]=="92" ) echo 'selected'; ?> >Guam (USA)</option>
<option value="91" <?php if ($formData["country"]=="91" ) echo 'selected'; ?> >Guatemala</option>
<option value="83" <?php if ($formData["country"]=="83" ) echo 'selected'; ?> >Guernsey</option>
<option value="86" <?php if ($formData["country"]=="86" ) echo 'selected'; ?> >Guinea</option>
<option value="93" <?php if ($formData["country"]=="93" ) echo 'selected'; ?> >Guinea Bissau</option>
<option value="94" <?php if ($formData["country"]=="94" ) echo 'selected'; ?> >Guyana</option>
<option value="99" <?php if ($formData["country"]=="99" ) echo 'selected'; ?> >Haiti</option>
<option value="96" <?php if ($formData["country"]=="96" ) echo 'selected'; ?> >Heard and McDonald Islands</option>
<option value="238" <?php if ($formData["country"]=="238" ) echo 'selected'; ?> >Holy See (Vatican City State)</option>
<option value="97" <?php if ($formData["country"]=="97" ) echo 'selected'; ?> >Honduras</option>
<option value="95" <?php if ($formData["country"]=="95" ) echo 'selected'; ?> >Hong Kong</option>
<option value="100" <?php if ($formData["country"]=="100" ) echo 'selected'; ?> >Hungary</option>
<option value="109" <?php if ($formData["country"]=="109" ) echo 'selected'; ?> >Iceland</option>
<option value="105" <?php if ($formData["country"]=="105" ) echo 'selected'; ?> >India</option>
<option value="101" <?php if ($formData["country"]=="101" ) echo 'selected'; ?> >Indonesia</option>
<option value="108" <?php if ($formData["country"]=="108" ) echo 'selected'; ?> >Iran</option>
<option value="107" <?php if ($formData["country"]=="107" ) echo 'selected'; ?> >Iraq</option>
<option value="102" <?php if ($formData["country"]=="102" ) echo 'selected'; ?> >Ireland</option>
<option value="104" <?php if ($formData["country"]=="104" ) echo 'selected'; ?> >Isle of Man</option>
<option value="103" <?php if ($formData["country"]=="103" ) echo 'selected'; ?> >Israel</option>
<option value="110" <?php if ($formData["country"]=="110" ) echo 'selected'; ?> >Italy</option>
<option value="45" <?php if ($formData["country"]=="45" ) echo 'selected'; ?> >Ivory Coast (Cote D'Ivoire)</option>
<option value="112" <?php if ($formData["country"]=="112" ) echo 'selected'; ?> >Jamaica</option>
<option value="114" <?php if ($formData["country"]=="114" ) echo 'selected'; ?> >Japan</option>
<option value="111" <?php if ($formData["country"]=="111" ) echo 'selected'; ?> >Jersey</option>
<option value="113" <?php if ($formData["country"]=="113" ) echo 'selected'; ?> >Jordan</option>
<option value="125" <?php if ($formData["country"]=="125" ) echo 'selected'; ?> >Kazakhstan</option>
<option value="115" <?php if ($formData["country"]=="115" ) echo 'selected'; ?> >Kenya</option>
<option value="118" <?php if ($formData["country"]=="118" ) echo 'selected'; ?> >Kiribati</option>
<option value="123" <?php if ($formData["country"]=="123" ) echo 'selected'; ?> >Kuwait</option>
<option value="116" <?php if ($formData["country"]=="116" ) echo 'selected'; ?> >Kyrgyz Republic (Kyrgyzstan)</option>
<option value="126" <?php if ($formData["country"]=="126" ) echo 'selected'; ?> >Laos</option>
<option value="135" <?php if ($formData["country"]=="135" ) echo 'selected'; ?> >Latvia</option>
<option value="127" <?php if ($formData["country"]=="127" ) echo 'selected'; ?> >Lebanon</option>
<option value="132" <?php if ($formData["country"]=="132" ) echo 'selected'; ?> >Lesotho</option>
<option value="131" <?php if ($formData["country"]=="131" ) echo 'selected'; ?> >Liberia</option>
<option value="136" <?php if ($formData["country"]=="136" ) echo 'selected'; ?> >Libya</option>
<option value="129" <?php if ($formData["country"]=="129" ) echo 'selected'; ?> >Liechtenstein</option>
<option value="133" <?php if ($formData["country"]=="133" ) echo 'selected'; ?> >Lithuania</option>
<option value="134" <?php if ($formData["country"]=="134" ) echo 'selected'; ?> >Luxembourg</option>
<option value="148" <?php if ($formData["country"]=="148" ) echo 'selected'; ?> >Macau</option>
<option value="144" <?php if ($formData["country"]=="144" ) echo 'selected'; ?> >Macedonia, the former Yugoslav Republic of</option>
<option value="142" <?php if ($formData["country"]=="142" ) echo 'selected'; ?> >Madagascar</option>
<option value="156" <?php if ($formData["country"]=="156" ) echo 'selected'; ?> >Malawi</option>
<option value="158" <?php if ($formData["country"]=="158" ) echo 'selected'; ?> >Malaysia</option>
<option value="155" <?php if ($formData["country"]=="155" ) echo 'selected'; ?> >Maldives</option>
<option value="145" <?php if ($formData["country"]=="145" ) echo 'selected'; ?> >Mali</option>
<option value="153" <?php if ($formData["country"]=="153" ) echo 'selected'; ?> >Malta</option>
<option value="143" <?php if ($formData["country"]=="143" ) echo 'selected'; ?> >Marshall Islands</option>
<option value="150" <?php if ($formData["country"]=="150" ) echo 'selected'; ?> >Martinique (French)</option>
<option value="151" <?php if ($formData["country"]=="151" ) echo 'selected'; ?> >Mauritania</option>
<option value="154" <?php if ($formData["country"]=="154" ) echo 'selected'; ?> >Mauritius</option>
<option value="248" <?php if ($formData["country"]=="248" ) echo 'selected'; ?> >Mayotte</option>
<option value="157" <?php if ($formData["country"]=="157" ) echo 'selected'; ?> >Mexico</option>
<option value="74" <?php if ($formData["country"]=="74" ) echo 'selected'; ?> >Micronesia</option>
<option value="139" <?php if ($formData["country"]=="139" ) echo 'selected'; ?> >Moldavia</option>
<option value="138" <?php if ($formData["country"]=="138" ) echo 'selected'; ?> >Monaco</option>
<option value="147" <?php if ($formData["country"]=="147" ) echo 'selected'; ?> >Mongolia</option>
<option value="140" <?php if ($formData["country"]=="140" ) echo 'selected'; ?> >Montenegro</option>
<option value="152" <?php if ($formData["country"]=="152" ) echo 'selected'; ?> >Montserrat</option>
<option value="137" <?php if ($formData["country"]=="137" ) echo 'selected'; ?> >Morocco</option>
<option value="159" <?php if ($formData["country"]=="159" ) echo 'selected'; ?> >Mozambique</option>
<option value="146" <?php if ($formData["country"]=="146" ) echo 'selected'; ?> >Myanmar</option>
<option value="160" <?php if ($formData["country"]=="160" ) echo 'selected'; ?> >Namibia</option>
<option value="169" <?php if ($formData["country"]=="169" ) echo 'selected'; ?> >Nauru</option>
<option value="168" <?php if ($formData["country"]=="168" ) echo 'selected'; ?> selected >Nepal</option>
<option value="166" <?php if ($formData["country"]=="166" ) echo 'selected'; ?> >Netherlands</option>
<option value="8" <?php if ($formData["country"]=="8" ) echo 'selected'; ?> >Netherlands Antilles</option>
<option value="170" <?php if ($formData["country"]=="170" ) echo 'selected'; ?> >Neutral Zone</option>
<option value="161" <?php if ($formData["country"]=="161" ) echo 'selected'; ?> >New Caledonia (French)</option>
<option value="172" <?php if ($formData["country"]=="172" ) echo 'selected'; ?> >New Zealand</option>
<option value="165" <?php if ($formData["country"]=="165" ) echo 'selected'; ?> >Nicaragua</option>
<option value="162" <?php if ($formData["country"]=="162" ) echo 'selected'; ?> >Niger</option>
<option value="164" <?php if ($formData["country"]=="164" ) echo 'selected'; ?> >Nigeria</option>
<option value="171" <?php if ($formData["country"]=="171" ) echo 'selected'; ?> >Niue</option>
<option value="163" <?php if ($formData["country"]=="163" ) echo 'selected'; ?> >Norfolk Island</option>
<option value="121" <?php if ($formData["country"]=="121" ) echo 'selected'; ?> >North Korea</option>
<option value="149" <?php if ($formData["country"]=="149" ) echo 'selected'; ?> >Northern Mariana Islands</option>
<option value="167" <?php if ($formData["country"]=="167" ) echo 'selected'; ?> >Norway</option>
<option value="173" <?php if ($formData["country"]=="173" ) echo 'selected'; ?> >Oman</option>
<option value="179" <?php if ($formData["country"]=="179" ) echo 'selected'; ?> >Pakistan</option>
<option value="186" <?php if ($formData["country"]=="186" ) echo 'selected'; ?> >Palau</option>
<option value="184" <?php if ($formData["country"]=="184" ) echo 'selected'; ?> >Palestinian Territory, Occupied</option>
<option value="174" <?php if ($formData["country"]=="174" ) echo 'selected'; ?> >Panama</option>
<option value="177" <?php if ($formData["country"]=="177" ) echo 'selected'; ?> >Papua New Guinea</option>
<option value="187" <?php if ($formData["country"]=="187" ) echo 'selected'; ?> >Paraguay</option>
<option value="175" <?php if ($formData["country"]=="175" ) echo 'selected'; ?> >Peru</option>
<option value="178" <?php if ($formData["country"]=="178" ) echo 'selected'; ?> >Philippines</option>
<option value="182" <?php if ($formData["country"]=="182" ) echo 'selected'; ?> >Pitcairn Island</option>
<option value="180" <?php if ($formData["country"]=="180" ) echo 'selected'; ?> >Poland</option>
<option value="176" <?php if ($formData["country"]=="176" ) echo 'selected'; ?> >Polynesia (French)</option>
<option value="185" <?php if ($formData["country"]=="185" ) echo 'selected'; ?> >Portugal</option>
<option value="183" <?php if ($formData["country"]=="183" ) echo 'selected'; ?> >Puerto Rico</option>
<option value="188" <?php if ($formData["country"]=="188" ) echo 'selected'; ?> >Qatar</option>
<option value="189" <?php if ($formData["country"]=="189" ) echo 'selected'; ?> >Reunion (French)</option>
<option value="190" <?php if ($formData["country"]=="190" ) echo 'selected'; ?> >Romania</option>
<option value="192" <?php if ($formData["country"]=="192" ) echo 'selected'; ?> >Russian Federation</option>
<option value="193" <?php if ($formData["country"]=="193" ) echo 'selected'; ?> >Rwanda</option>
<option value="27" <?php if ($formData["country"]=="27" ) echo 'selected'; ?> >Saint Barthélémy</option>
<option value="200" <?php if ($formData["country"]=="200" ) echo 'selected'; ?> >Saint Helena</option>
<option value="120" <?php if ($formData["country"]=="120" ) echo 'selected'; ?> >Saint Kitts & Nevis Anguilla</option>
<option value="128" <?php if ($formData["country"]=="128" ) echo 'selected'; ?> >Saint Lucia</option>
<option value="141" <?php if ($formData["country"]=="141" ) echo 'selected'; ?> >Saint Martin (French part)</option>
<option value="181" <?php if ($formData["country"]=="181" ) echo 'selected'; ?> >Saint Pierre and Miquelon</option>
<option value="210" <?php if ($formData["country"]=="210" ) echo 'selected'; ?> >Saint Tome (Sao Tome) and Principe</option>
<option value="239" <?php if ($formData["country"]=="239" ) echo 'selected'; ?> >Saint Vincent & Grenadines</option>
<option value="246" <?php if ($formData["country"]=="246" ) echo 'selected'; ?> >Samoa</option>
<option value="205" <?php if ($formData["country"]=="205" ) echo 'selected'; ?> >San Marino</option>
<option value="194" <?php if ($formData["country"]=="194" ) echo 'selected'; ?> >Saudi Arabia</option>
<option value="206" <?php if ($formData["country"]=="206" ) echo 'selected'; ?> >Senegal</option>
<option value="191" <?php if ($formData["country"]=="191" ) echo 'selected'; ?> >Serbia</option>
<option value="196" <?php if ($formData["country"]=="196" ) echo 'selected'; ?> >Seychelles</option>
<option value="204" <?php if ($formData["country"]=="204" ) echo 'selected'; ?> >Sierra Leone</option>
<option value="199" <?php if ($formData["country"]=="199" ) echo 'selected'; ?> >Singapore</option>
<option value="212" <?php if ($formData["country"]=="212" ) echo 'selected'; ?> >Sint Maarten (Dutch part)</option>
<option value="203" <?php if ($formData["country"]=="203" ) echo 'selected'; ?> >Slovakia</option>
<option value="201" <?php if ($formData["country"]=="201" ) echo 'selected'; ?> >Slovenia</option>
<option value="195" <?php if ($formData["country"]=="195" ) echo 'selected'; ?> >Solomon Islands</option>
<option value="207" <?php if ($formData["country"]=="207" ) echo 'selected'; ?> >Somalia</option>
<option value="250" <?php if ($formData["country"]=="250" ) echo 'selected'; ?> >South Africa</option>
<option value="90" <?php if ($formData["country"]=="90" ) echo 'selected'; ?> >South Georgia and the South Sandwich Islands</option>
<option value="122" <?php if ($formData["country"]=="122" ) echo 'selected'; ?> >South Korea</option>
<option value="209" <?php if ($formData["country"]=="209" ) echo 'selected'; ?> >South Sudan</option>
<option value="69" <?php if ($formData["country"]=="69" ) echo 'selected'; ?> >Spain</option>
<option value="130" <?php if ($formData["country"]=="130" ) echo 'selected'; ?> >Sri Lanka</option>
<option value="197" <?php if ($formData["country"]=="197" ) echo 'selected'; ?> >Sudan</option>
<option value="208" <?php if ($formData["country"]=="208" ) echo 'selected'; ?> >Suriname</option>
<option value="202" <?php if ($formData["country"]=="202" ) echo 'selected'; ?> >Svalbard and Jan Mayen Islands</option>
<option value="214" <?php if ($formData["country"]=="214" ) echo 'selected'; ?> >Swaziland</option>
<option value="198" <?php if ($formData["country"]=="198" ) echo 'selected'; ?> >Sweden</option>
<option value="44" <?php if ($formData["country"]=="44" ) echo 'selected'; ?> >Switzerland</option>
<option value="213" <?php if ($formData["country"]=="213" ) echo 'selected'; ?> >Syria</option>
<option value="229" <?php if ($formData["country"]=="229" ) echo 'selected'; ?> >Taiwan</option>
<option value="220" <?php if ($formData["country"]=="220" ) echo 'selected'; ?> >Tajikistan</option>
<option value="230" <?php if ($formData["country"]=="230" ) echo 'selected'; ?> >Tanzania</option>
<option value="219" <?php if ($formData["country"]=="219" ) echo 'selected'; ?> >Thailand</option>
<option value="218" <?php if ($formData["country"]=="218" ) echo 'selected'; ?> >Togo</option>
<option value="221" <?php if ($formData["country"]=="221" ) echo 'selected'; ?> >Tokelau</option>
<option value="224" <?php if ($formData["country"]=="224" ) echo 'selected'; ?> >Tonga</option>
<option value="227" <?php if ($formData["country"]=="227" ) echo 'selected'; ?> >Trinidad and Tobago</option>
<option value="223" <?php if ($formData["country"]=="223" ) echo 'selected'; ?> >Tunisia</option>
<option value="226" <?php if ($formData["country"]=="226" ) echo 'selected'; ?> >Turkey</option>
<option value="222" <?php if ($formData["country"]=="222" ) echo 'selected'; ?> >Turkmenistan</option>
<option value="215" <?php if ($formData["country"]=="215" ) echo 'selected'; ?> >Turks and Caicos Islands</option>
<option value="228" <?php if ($formData["country"]=="228" ) echo 'selected'; ?> >Tuvalu</option>
<option value="232" <?php if ($formData["country"]=="232" ) echo 'selected'; ?> >Uganda</option>
<option value="231" <?php if ($formData["country"]=="231" ) echo 'selected'; ?> >Ukraine</option>
<option value="2" <?php if ($formData["country"]=="2" ) echo 'selected'; ?> >United Arab Emirates</option>
<option value="233" <?php if ($formData["country"]=="233" ) echo 'selected'; ?> >United Kingdom</option>
<option value="235" <?php if ($formData["country"]=="235" ) echo 'selected'; ?> >United States</option>
<option value="236" <?php if ($formData["country"]=="236" ) echo 'selected'; ?> >Uruguay</option>
<option value="234" <?php if ($formData["country"]=="234" ) echo 'selected'; ?> >USA Minor Outlying Islands</option>
<option value="237" <?php if ($formData["country"]=="237" ) echo 'selected'; ?> >Uzbekistan</option>
<option value="244" <?php if ($formData["country"]=="244" ) echo 'selected'; ?> >Vanuatu</option>
<option value="240" <?php if ($formData["country"]=="240" ) echo 'selected'; ?> >Venezuela</option>
<option value="243" <?php if ($formData["country"]=="243" ) echo 'selected'; ?> >Vietnam</option>
<option value="241" <?php if ($formData["country"]=="241" ) echo 'selected'; ?> >Virgin Islands (British)</option>
<option value="242" <?php if ($formData["country"]=="242" ) echo 'selected'; ?> >Virgin Islands (USA)</option>
<option value="245" <?php if ($formData["country"]=="245" ) echo 'selected'; ?> >Wallis and Futuna Islands</option>
<option value="67" <?php if ($formData["country"]=="67" ) echo 'selected'; ?> >Western Sahara</option>
<option value="247" <?php if ($formData["country"]=="247" ) echo 'selected'; ?> >Yemen</option>
<option value="249" <?php if ($formData["country"]=="249" ) echo 'selected'; ?> >Yugoslavia</option>
<option value="252" <?php if ($formData["country"]=="252" ) echo 'selected'; ?> >Zaire</option>
<option value="251" <?php if ($formData["country"]=="251" ) echo 'selected'; ?> >Zambia</option>
<option value="253" <?php if ($formData["country"]=="253" ) echo 'selected'; ?> >Zimbabwe</option>
                      </select></td></tr>
<tr><td>Do you wish to get product updates from Bazaar International?</td><td><input type="radio" name="optin" id = "optin" value="Yes" <?php if($formData["optin"] == "Yes") echo "checked"; ?>>
              &nbsp;Yes&nbsp;&nbsp;&nbsp;&nbsp;
              <input type="radio" name="optin" id="optin" value="No" <?php if($formData["optin"] == "No") echo "checked"; ?>>
              &nbsp;No</td></tr>
<tr><td><input type = "submit" name="submit"/></td></tr>
</table>
</form>
</body>
</html>