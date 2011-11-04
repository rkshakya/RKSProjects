<?php
require_once("class_mysql.php");

class class_edit extends class_mysql {

/* constructor */
	function class_edit(){
		global $config;		
        $this->database=$config["dbname"];
        $this->class_mysql();             //call the base constructor
		}

}
?>