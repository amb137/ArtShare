<?php

error_reporting(0);
require "init.php";

$username = $_POST["username"];
$image = $_POST["image"];
$title = $_POST["title"];
$materials = $_POST["materials"];
$description = $_POST["description"];

$decodedImage = base64_decode("$image");
$dir = "pictures/" . $username;

if (!is_dir($dir)) { mkdir($dir, 0777, true); }
file_put_contents("pictures/" . $username . "/" . $title . ".JPG", $decodedImage);

$sql = "INSERT INTO `artwork` (`id`,`username`,`title`,`materials`,`description`) VALUES (NULL,'".$username."','".$title."','".$materials."','".$description."');";

if(!mysqli_query($con, $sql)){
    echo '{"message":"Unable to save the data to the database."}';
}

?>