<?php 
include_once ('config.php');
/*
 *displays results 
 *Author : Ravi K Shakya
 */


if($_SERVER["REQUEST_METHOD"] == "GET") {
    $orgid = $_GET['orgid'];    
    $error = array();
    
    if(empty($orgid)){
        $error['orgid'] = "No orgid was provided";
    }   
    
    if(count($error) == 0){
        //print 'mongodb://'.$UNAME.':'.$PWD.'@'.$HOST.':'.$PORT.'/'.$DB;
        $con = new Mongo('mongodb://'.$UNAME.':'.$PWD.'@'.$HOST.':'.$PORT.'/'.$DB);
        
        if($con){

        $db = $con->$DB;
        $people = $db->$COLLECTION;
        
        $criteria = array(
            'org_id' => $orgid
        );
        
        $project = array( 
                'allow_urls' => 1,
                'block_urls' => 1,
                'block_cats' => 1,
                'org_id' => 1,
                'user_id' => 1
            );
        
        $cursor = $people->find($criteria, $project)->limit(1);
        foreach ($cursor as $doc) {
            var_dump($doc);
            }
    
        }
    }
    
}else if ($_SERVER["REQUEST_METHOD"] == "POST"){
    $white = $_POST['whitelist'];
    $black = $_POST['blacklist'];
    $userid = $_POST['user_id'];

    $arrwhite = array_map('trim', explode(",", $white));
    $arrblack = array_map('trim', explode(",", $black));
   
    $blkcats = array();
    
    if ($_POST['chk1']) { array_push ($blkcats, intval($_POST['chk1'])); }
if ($_POST['chk2']) { array_push ($blkcats, intval($_POST['chk2'])); }
if ($_POST['chk3']) { array_push ($blkcats, intval($_POST['chk3'])); }
if ($_POST['chk4']) { array_push ($blkcats, intval($_POST['chk4'])); }
if ($_POST['chk5']) { array_push ($blkcats, intval($_POST['chk5'])); }
if ($_POST['chk6']) { array_push ($blkcats, intval($_POST['chk6'])); }
if ($_POST['chk7']) { array_push ($blkcats, intval($_POST['chk7'])); }
if ($_POST['chk8']) { array_push ($blkcats, intval($_POST['chk8'])); }
if ($_POST['chk9']) { array_push ($blkcats, intval($_POST['chk9'])); }
if ($_POST['chk10']) { array_push ($blkcats, intval($_POST['chk10'])); }
if ($_POST['chk11']) { array_push ($blkcats, intval($_POST['chk11'])); }
if ($_POST['chk12']) { array_push ($blkcats, intval($_POST['chk12'])); }
if ($_POST['chk13']) { array_push ($blkcats, intval($_POST['chk13'])); }
if ($_POST['chk14']) { array_push ($blkcats, intval($_POST['chk14'])); }
if ($_POST['chk15']) { array_push ($blkcats, intval($_POST['chk15'])); }
if ($_POST['chk16']) { array_push ($blkcats, intval($_POST['chk16'])); }
if ($_POST['chk17']) { array_push ($blkcats, intval($_POST['chk17'])); }
if ($_POST['chk18']) { array_push ($blkcats, intval($_POST['chk18'])); }
if ($_POST['chk19']) { array_push ($blkcats, intval($_POST['chk19'])); }
if ($_POST['chk20']) { array_push ($blkcats, intval($_POST['chk20'])); }
if ($_POST['chk21']) { array_push ($blkcats, intval($_POST['chk21'])); }
if ($_POST['chk22']) { array_push ($blkcats, intval($_POST['chk22'])); }
if ($_POST['chk23']) { array_push ($blkcats, intval($_POST['chk23'])); }
if ($_POST['chk24']) { array_push ($blkcats, intval($_POST['chk24'])); }
if ($_POST['chk25']) { array_push ($blkcats, intval($_POST['chk25'])); }
if ($_POST['chk26']) { array_push ($blkcats, intval($_POST['chk26'])); }
if ($_POST['chk27']) { array_push ($blkcats, intval($_POST['chk27'])); }
if ($_POST['chk28']) { array_push ($blkcats, intval($_POST['chk28'])); }
if ($_POST['chk29']) { array_push ($blkcats, intval($_POST['chk29'])); }
if ($_POST['chk30']) { array_push ($blkcats, intval($_POST['chk30'])); }
if ($_POST['chk31']) { array_push ($blkcats, intval($_POST['chk31'])); }
if ($_POST['chk32']) { array_push ($blkcats, intval($_POST['chk32'])); }
if ($_POST['chk33']) { array_push ($blkcats, intval($_POST['chk33'])); }
if ($_POST['chk34']) { array_push ($blkcats, intval($_POST['chk34'])); }
if ($_POST['chk35']) { array_push ($blkcats, intval($_POST['chk35'])); }
if ($_POST['chk36']) { array_push ($blkcats, intval($_POST['chk36'])); }
if ($_POST['chk37']) { array_push ($blkcats, intval($_POST['chk37'])); }
if ($_POST['chk38']) { array_push ($blkcats, intval($_POST['chk38'])); }
if ($_POST['chk39']) { array_push ($blkcats, intval($_POST['chk39'])); }
if ($_POST['chk40']) { array_push ($blkcats, intval($_POST['chk40'])); }
if ($_POST['chk41']) { array_push ($blkcats, intval($_POST['chk41'])); }
if ($_POST['chk42']) { array_push ($blkcats, intval($_POST['chk42'])); }
if ($_POST['chk43']) { array_push ($blkcats, intval($_POST['chk43'])); }
if ($_POST['chk44']) { array_push ($blkcats, intval($_POST['chk44'])); }
if ($_POST['chk45']) { array_push ($blkcats, intval($_POST['chk45'])); }
if ($_POST['chk46']) { array_push ($blkcats, intval($_POST['chk46'])); }
if ($_POST['chk47']) { array_push ($blkcats, intval($_POST['chk47'])); }
if ($_POST['chk48']) { array_push ($blkcats, intval($_POST['chk48'])); }
if ($_POST['chk49']) { array_push ($blkcats, intval($_POST['chk49'])); }
if ($_POST['chk50']) { array_push ($blkcats, intval($_POST['chk50'])); }
if ($_POST['chk51']) { array_push ($blkcats, intval($_POST['chk51'])); }
if ($_POST['chk52']) { array_push ($blkcats, intval($_POST['chk52'])); }
if ($_POST['chk53']) { array_push ($blkcats, intval($_POST['chk53'])); }
if ($_POST['chk54']) { array_push ($blkcats, intval($_POST['chk54'])); }
if ($_POST['chk55']) { array_push ($blkcats, intval($_POST['chk55'])); }
if ($_POST['chk56']) { array_push ($blkcats, intval($_POST['chk56'])); }
if ($_POST['chk57']) { array_push ($blkcats, intval($_POST['chk57'])); }
if ($_POST['chk58']) { array_push ($blkcats, intval($_POST['chk58'])); }
if ($_POST['chk59']) { array_push ($blkcats, intval($_POST['chk59'])); }
if ($_POST['chk60']) { array_push ($blkcats, intval($_POST['chk60'])); }
if ($_POST['chk61']) { array_push ($blkcats, intval($_POST['chk61'])); }
if ($_POST['chk62']) { array_push ($blkcats, intval($_POST['chk62'])); }
if ($_POST['chk63']) { array_push ($blkcats, intval($_POST['chk63'])); }
if ($_POST['chk64']) { array_push ($blkcats, intval($_POST['chk64'])); }
if ($_POST['chk65']) { array_push ($blkcats, intval($_POST['chk65'])); }
if ($_POST['chk100']) { array_push ($blkcats, intval($_POST['chk100'])); }
if ($_POST['chk101']) { array_push ($blkcats, intval($_POST['chk101'])); }

$con = new Mongo('mongodb://'.$UNAME.':'.$PWD.'@'.$HOST.':'.$PORT.'/'.$DB);
       
        if($con){
        // Select Database
        $db = $con->$DB;
        // Select Collection
        $usrs = $db->$COLLECTION;
        
        $criteria = array(
            'user_id' => intval($userid)
        );
        
     
        
        $updates = array(
                         '$set' => array("allow_urls" => $arrwhite, "block_urls" => $arrblack, "block_cats" => $blkcats)
                         );
        
        $res = $usrs->update($criteria, $updates);
        if(!$res){
            print "Error updating";
        }else{
            print "Record successfully updated.";

        }
        
    }


}
?>

<html>
            <head>
            <title>Web filtering configuration</title>
            </head>
            <body>                
<?php        
        foreach($cursor as $k=>$v){
            
                foreach($v['allow_urls'] as $key=>$value) { $all_urls .= $value.','; }
                $all_urls = rtrim($all_urls, ",");
            
                foreach($v['block_urls'] as $ky=>$val) { $blk_urls .= $val.','; }
                $blk_urls = rtrim($blk_urls, ",");
            
         
            
                $cats = $v['block_cats'];
              
            
        
?>
<h3>Web filtering configuration</h3>
                <form action="" method="POST">
                    <table>
                    <tr><td>Organization ID :</td> <td><?php echo $v[org_id]; ?></td><td><input type = 'hidden' name = 'user_id' id = 'user_id' value= <?php echo $v[user_id]; ?> ></td></tr>
                    <tr><td>Whitelist : </td> <td><textarea name="whitelist"cols="30" rows="4"><?php echo $all_urls; ?></textarea></td><td>entry separated by comma</td></tr>
                    <tr><td>Blacklist : </td> <td><textarea name="blacklist"cols="30" rows="4"><?php echo $blk_urls; ?></textarea></td><td>entry separated by comma</td></tr>
                    <tr><td>Blocked Categories : </td> <td>&nbsp;</td><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td><td><input type="checkbox" name="chk14" value=14 <?php if(in_array(14, $cats, true)) echo 'checked'; ?> >Advertisements</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk64" value=64 <?php if(in_array(64, $cats, true)) echo 'checked'; ?> >Alcohol</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk6" value=6 <?php if(in_array(6, $cats, true)) echo 'checked'; ?> >Art</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk49" value=49 <?php if(in_array(49, $cats, true)) echo 'checked'; ?> >Blogs</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk62" value=62 <?php if(in_array(62, $cats, true)) echo 'checked'; ?> >Botnets</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk11" value=11 <?php if(in_array(11, $cats, true)) echo 'checked'; ?> >Business</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk7" value=7 <?php if(in_array(7, $cats, true)) echo 'checked'; ?> >Comedy and Humor</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk20" value=20 <?php if(in_array(20, $cats, true)) echo 'checked'; ?> >Cult and Occult</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk33" value=33 <?php if(in_array(33, $cats, true)) echo 'checked'; ?> >Education</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk32" value=32 <?php if(in_array(32, $cats, true)) echo 'checked'; ?> >Educational Institutions</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk8" value=8 <?php if(in_array(8, $cats, true)) echo 'checked'; ?> >Entertainment</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk23" value=23 <?php if(in_array(23, $cats, true)) echo 'checked'; ?> >Fashion and Style</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk12" value=12 <?php if(in_array(12, $cats, true)) echo 'checked'; ?> >Financial Services</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk24" value=24 <?php if(in_array(24, $cats, true)) echo 'checked'; ?> >Food and Restaurants</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk36" value=36 <?php if(in_array(36, $cats, true)) echo 'checked'; ?> >Gambling</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk37" value=37 <?php if(in_array(37, $cats, true)) echo 'checked'; ?> >Games</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk25" value=25 <?php if(in_array(25, $cats, true)) echo 'checked'; ?> >Gay or Lesbian</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk26" value=26 <?php if(in_array(26, $cats, true)) echo 'checked'; ?> >Government and Politics</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk39" value=39 <?php if(in_array(39, $cats, true)) echo 'checked'; ?> >Hacking</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk42" value=42 <?php if(in_array(42, $cats, true)) echo 'checked'; ?> >Health</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk44" value=44 <?php if(in_array(44, $cats, true)) echo 'checked'; ?> >Hobbies</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk22" value=22 <?php if(in_array(22, $cats, true)) echo 'checked'; ?> >Home and Garden</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk45" value=45 <?php if(in_array(45, $cats, true)) echo 'checked'; ?> >Hunting</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk31" value=31 <?php if(in_array(31, $cats, true)) echo 'checked'; ?> >Illegal Drugs</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk38" value=38 <?php if(in_array(38, $cats, true)) echo 'checked'; ?> >Illegal or Questionable</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk18" value=18 <?php if(in_array(18, $cats, true)) echo 'checked'; ?> >Image and Video Search</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk30" value=30 <?php if(in_array(30, $cats, true)) echo 'checked'; ?> >Industry Associations</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk16" value=16 <?php if(in_array(16, $cats, true)) echo 'checked'; ?> >Information Technology</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk47" value=47 <?php if(in_array(47, $cats, true)) echo 'checked'; ?> >Internet Auctions</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk59" value=59 <?php if(in_array(59, $cats, true)) echo 'checked'; ?> >Invalid URLs</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk13" value=13 <?php if(in_array(13, $cats, true)) echo 'checked'; ?> >Job Search</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk27" value=27 <?php if(in_array(27, $cats, true)) echo 'checked'; ?> >Kids</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk3" value=3 <?php if(in_array(3, $cats, true)) echo 'checked'; ?> >Lingerie and Swimsuit</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk61" value=61 <?php if(in_array(61, $cats, true)) echo 'checked'; ?> >Malicious Websites</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk43" value=43 <?php if(in_array(43, $cats, true)) echo 'checked'; ?> >News and Media</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk2" value=2 <?php if(in_array(2, $cats, true)) echo 'checked'; ?> >Nudity</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk57" value=57 <?php if(in_array(57, $cats, true)) echo 'checked'; ?> >Parked Domains</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk50" value=50 <?php if(in_array(50, $cats, true)) echo 'checked'; ?> >Personal Sites</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk5" value=5 <?php if(in_array(5, $cats, true)) echo 'checked'; ?> >Personals and Dating</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk63" value=63 <?php if(in_array(63, $cats, true)) echo 'checked'; ?> >Phishing</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk1" value=1 <?php if(in_array(1, $cats, true)) echo 'checked'; ?> >Pornography</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk15" value=15 <?php if(in_array(15, $cats, true)) echo 'checked'; ?> >Proxy Avoidance</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk40" value=40 <?php if(in_array(40, $cats, true)) echo 'checked'; ?> >Racism and Hate</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk28" value=28 <?php if(in_array(28, $cats, true)) echo 'checked'; ?> >Real Estate</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk34" value=34 <?php if(in_array(34, $cats, true)) echo 'checked'; ?> >Reference</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk29" value=29 <?php if(in_array(29, $cats, true)) echo 'checked'; ?> >Religion</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk35" value=35 <?php if(in_array(35, $cats, true)) echo 'checked'; ?> >Science</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk19" value=19 <?php if(in_array(19, $cats, true)) echo 'checked'; ?> >Search Engines</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk4" value=4 <?php if(in_array(4, $cats, true)) echo 'checked'; ?> >Sex Education</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk48" value=48 <?php if(in_array(48, $cats, true)) echo 'checked'; ?> >Shopping</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk21" value=21 <?php if(in_array(21, $cats, true)) echo 'checked'; ?> >Society and Lifestyles</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk53" value=53 <?php if(in_array(53, $cats, true)) echo 'checked'; ?> >Sports</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk10" value=10 <?php if(in_array(10, $cats, true)) echo 'checked'; ?> >Streaming Media</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk41" value=41 <?php if(in_array(41, $cats, true)) echo 'checked'; ?> >Tasteless</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk9" value=9 <?php if(in_array(9, $cats, true)) echo 'checked'; ?> >Television and Radio</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk65" value=65 <?php if(in_array(65, $cats, true)) echo 'checked'; ?> >Tobacco</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk54" value=54 <?php if(in_array(54, $cats, true)) echo 'checked'; ?> >Travel</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk58" value=58 <?php if(in_array(58, $cats, true)) echo 'checked'; ?> >Uncategorized</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk46" value=46 <?php if(in_array(46, $cats, true)) echo 'checked'; ?> >Vehicles</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk55" value=55 <?php if(in_array(55, $cats, true)) echo 'checked'; ?> >Violence</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk56" value=56 <?php if(in_array(56, $cats, true)) echo 'checked'; ?> >Weapons</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk60" value=60 <?php if(in_array(60, $cats, true)) echo 'checked'; ?> >Web and Email Spam</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk52" value=52 <?php if(in_array(52, $cats, true)) echo 'checked'; ?> >Web Chat</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk51" value=51 <?php if(in_array(51, $cats, true)) echo 'checked'; ?> >Web Email</td><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td><td><input type="checkbox" name="chk17" value=17 <?php if(in_array(17, $cats, true)) echo 'checked'; ?> >Web Hosting</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                    <tr><td>Blocked Web 2.0 apps : </td> <td>&nbsp;</td><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td><td><input type="checkbox" name="chk100" value=100 <?php if(in_array(100, $cats, true)) echo 'checked'; ?> >Dropbox</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td><td><input type="checkbox" name="chk101" value=101 <?php if(in_array(101, $cats, true)) echo 'checked'; ?> >Facebook</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td><td><input  name="submitForm" id="submitForm" type="submit" value="Save" /></td></tr>
                    </table>
<?php } ?>
                </form>
            </body>
</html>
