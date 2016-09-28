<?php

error_reporting(0);
require "init.php";

$email = $_POST["email"];
$password = $_POST["password"];

$sql = "SELECT * FROM `data` WHERE `email`='".$email."' AND `password`='".$password."';";
$result = mysqli_query($con, $sql);
$response = array();

while($row = mysqli_fetch_array($result)){
	$response = array("id"=>$row[0],"name"=>$row[1],"password"=>$row[2],"email"=>$row[3]);
}

echo json_encode(array("data"=>$response));

?>