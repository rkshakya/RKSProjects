<?php session_start();?>
<!DOCTYPE html>
<html>
    <head>
        <title>IC Backoffice Application</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
    <script>
        function validate_login(){
            if(document.frm.login.value==""){
                alert("Enter Login Name!");
                document.frm.login.focus();
                return false;
            }
            if(document.frm.password.value==""){
                alert("Enter Password!");
                document.frm.password.focus();
                return false;
            }
        }
    </script>
    </head>
    

    <body>
	<div id = "head">Welcome to backoffice application. You can use it to set Database, Mailer and default value settings.</div>
        <div id="main">	    
                     <form action="check_login.php" method="POST" name="frm" onsubmit="return validate_login()">
			<h3>Login</h3 >
                            <table cellspacing="5px">
                                <tbody>
                                    <tr>
                                        <td style="color: red; text-align: center;" colspan="2">
                                            <?php 
                                                if(isset($_SESSION['id']))
                                                    echo $_SESSION['id'];
                                            ?>
                                        </td>
                                    </tr>
                                   <tr>
                                        <th style="color: black;"><label for="login_name">Login Name:</label></th>
                                        <td><input type="text" id="login_name" name="login"  width: 150px;"/></td>
                                    </tr>
                                    <tr>
                                        <th style="color: black;"><label for="pass">Password:</label></th>
                                        <td><input type="password" id="pass" name="password"  width: 150px;"/></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td><input type="submit" value="Login" style="width: 60px; " />       
                                        	<input type="reset" value="Reset" style="width: 60px;"/></td>
                                    </tr>
                                   
                                </tbody>
                            </table>
                        </form>
                <br style="clear:both;" />
        </div><!--close main-->	
    </body>
</html>
<?php unset($_SESSION['id']); ?>
