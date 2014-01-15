<?php require_once('header.php');
/*
 *displays search bar
 *Author : Ravi K Shakya
 */
?>
<html>
<head>
<title>Search page</title>
</head>
<body>
<form action="results.php" method="POST">
What skill are you looking for?
<input type="text" id="searchterm" name="searchterm"  />
<input  name="submitForm" id="submitForm" type="submit" value="Search" />
</form>
</body>
</html>