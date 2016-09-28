<?php

error_reporting(0);
require "init.php";

$name = $_POST["name"];
$password = $_POST["password"];
$email = $_POST["email"];

// $sql1 = "SELECT * FROM `data` WHERE `email` = '".$email."';";
// $res = mysql_query($sql1);
// if($res && mysql_num_rows($res) > 0){
// 	echo 'Error: username already taken';
// }

// else {
$sql = "INSERT INTO `data` (`id`,`name`, `password`, `email`) VALUES (NULL,'".$name."','".$password."','".$email."');";
if(!mysqli_query($con, $sql)){
	echo '{"message":"Unable to save the data to the database."}';
}
// }

?>