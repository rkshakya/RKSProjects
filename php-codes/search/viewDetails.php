<?php require_once('header.php');
include_once('config.php');
/*
 *displays scholar details
 *Author : Ravi K Shakya
 */

if(isset($_GET) and $_GET['id'] and $_GET['src']) {
    $id = mysql_escape_string($_GET['id']);
    $src = mysql_escape_string($_GET['src']);    
    $error = array();
    //Validation
    if($id == '' or $src == ''){
        $error[] = "Required parameters are not present.";
    }
    
    //print ($id);
    
    if(count($error) == 0){
        $con = new Mongo('mongodb://'.$UNAME.':'.$PWD.'@'.$HOST.':'.$PORT.'/'.$DB);
        if($con){
        // Select Database
        $db = $con->$DB;
        // Select Collection
        $pub = $db->$COLLECTION;
        $qry = array("_id" => new MongoId($id));
                
        //set the fields to fetch based on src
        if ($src == 'R1'){
            $project = array('Name' , 'Diploma' , 'University' , 'Institute' , 'Skills' , 'Topics' );
        } elseif ($src == 'A1'){
            $project = array('Name' , 'University' , 'Facility' , 'Title' , 'About' , 'Skype' , 'Twitter' , 'Facebook' , 'LinkedIn' , 'Homepage' );
        } elseif ($src == 'MS1'){
            $project = array( 'Name' , 'University' , 'Institute' , 'Fields' , 'Webpage' , 'Picture URL');
        } elseif ($src == 'M1'){            
            $project = array('Name' , 'University' , 'Institute' , 'Location' , 'Biographic information' , 'Title and Institution', 'Image URL' );
        }           
        
        $result = $pub->findOne($qry, $project);
       //print_r($result);
?>        
        <html>
            <head>
            <title>Detail Information</title>
            </head>
            <body>
                Search
                <form action="results.php" method="POST">
                    <input type="text" id="searchterm" name="searchterm"/>
                    <input  name="submitForm" id="submitForm" type="submit" value="Search" />
                </form>
                <table border = '1'>
                    <tr><td>
                        <?php if(isset($result['Picture URL'])) {
                        ?>
                            <img src = "<?php print $result['Picture URL']; ?>">
                
                        <?php   } elseif (isset($result['Image URL'])) {
                        ?>
                            <img src = "<?php print $result['Image URL']; ?>">
                        <?php  } else { ?>
                            Image here
                        <?php } ?> 
                        
                    </td><td><table border = '1'>
                    
            <?php
                    print "<tr><td>Name</td><td>".$result['Name']."</td></tr>";
                    foreach($result as $k=>$v){
                        if($k == "Picture URL" or $k == 'Image URL' or $k == 'Name'){
                            continue;
                        }
                        print "<tr><td>".$k."</td><td>".$v."</td></tr>";
                    }
            ?>
                    </table></td></tr>
<?php            
        } else {
        die("Mongo DB not installed");
        }
    }
}
?>
</table>
</body>
        </html>