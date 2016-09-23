<?php

error_reporting(0);

$db_name = "my_database";
$mysql_user = "my_database";
$mysql_pass = "abc123";
$host_name = "localhost";

$con = mysqli_connect($host_name, $mysql_user, $mysql_pass, $db_name);

if(!$con){
	die('Error: Unable to connect to the database');
}

?>