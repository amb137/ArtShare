<?php

error_reporting(0);
require "init.php";

$fields = "`username`,`title`, `materials`, `description`";
$username = $_POST["username"];

function resultToArray($result) {
    $rows = array();
    while($row = $result->fetch_assoc()) {
        $rows[] = $row;
    }
    return $rows;
}

$query = "SELECT DISTINCT $fields FROM `artwork` WHERE `username` = '".$username."';";

$result = $con->query($query);
$rows = resultToArray($result);
$result->free();


echo json_encode(array("artwork"=>$rows));

?>