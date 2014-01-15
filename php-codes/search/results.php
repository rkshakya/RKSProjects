<?php require_once('header.php');
include_once ('config.php');
/*
 *displays results 
 *Author : Ravi K Shakya
 */

if(isset($_POST) and $_POST['submitForm'] == "Search" ) {
    $srchterm = mysql_escape_string($_POST['searchterm']);    
    $error = array();
    //print $srchterm;
    //Validation
    if(empty($srchterm)){
        $error['search'] = "No search term was provided";
    }   
    
    if(count($error) == 0){
        $con = new Mongo('mongodb://'.$UNAME.':'.$PWD.'@'.$HOST.':'.$PORT.'/'.$DB);
        if($con){
        // Select Database
        $db = $con->$DB;
        // Select Collection
        $people = $db->$COLLECTION;
        $qry = array('text' => $COLLECTION, //this is the name of the collection where we are searching
        'search' => $srchterm, //the string to search
        'limit' => $LIMIT, //the number of results, by default is 1000
        'project' => Array( //the fields to retrieve from db
                'First Name' => 1,
                'Middle Name' => 1,
                'Last Name' => 1,
                'src' => 1,
                'University' => 1,
                'Picture URL' => 1,
                'Image URL'=> 1
            )
        );
        //print_r ($qry);
        $r = $db->command($qry);
        //print_r($r);
        
?>        
        <html>
            <head>
            <title>Search Results</title>
            </head>
            <body>
                Search Results for term
                <form action="" method="POST">
                    <input type="text" id="searchterm" name="searchterm"  value = "<?php echo $_POST['searchterm']; ?>"/>
                    <input  name="submitForm" id="submitForm" type="submit" value="Search" />
                </form>
                <table border = '1'>
<?php        
        foreach($r['results'] as $k=>$v){
?>
        
            <tr><td>
            <?php if(isset($v['obj']['Picture URL'])) {
            ?>
                <img src = "<?php print $v['obj']['Picture URL']; ?>">
                
             <?php   } elseif (isset($v['obj']['Image URL'])) {
             ?>
                <img src = "<?php print $v['obj']['Image URL']; ?>">
            <?php  } else { ?>
                Image here
            <?php } ?>
              </td>
                <td><table border = '1'><tr><td>Name : <a href = 'viewDetails.php?id=<?php echo $v['obj']['_id'];?>&src=<?php echo $v['obj']['src'];?>'><?php echo $v['obj']['First Name'].' '.$v['obj']['Middle Name']. ' ' .$v['obj']['Last Name']; ?></a></td></tr><tr><td>University: <?php echo $v['obj']['University']; ?></td></tr><tr><td><a href = 'viewDetails.php?id=<?php echo $v['obj']['_id'];?>&src=<?php echo $v['obj']['src'];?>'>View details</a></td></tr><tr><td>Source : <?php echo $v['obj']['src']; ?></td></tr><tr><td>ID : <?php echo $v['obj']['_id']; ?></td></tr></table></td></tr>
       
<?php        
            }        
        } else {
        die("Mongo DB not installed");
        }
    }else{
?>
 <html>
            <head>
            <title>Search Results</title>
            </head>
            <body>
                <h3><?php echo $error['search']; ?></h3>
                <form action="" method="POST">
                    <input type="text" id="searchterm" name="searchterm"  value = "<?php echo $srchterm; ?>"/>
                    <input  name="submitForm" id="submitForm" type="submit" value="Search" />
                </form>
<?php
    }
}
?>
 </table>
</body>
        </html>