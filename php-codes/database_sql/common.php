<?php
function linkdb()
{
	$ww=mysql_connect('localhost','',''); //database server, username, password
	return $ww;
}
?>