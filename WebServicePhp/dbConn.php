<?php
 header("Access-Control-Allow-Origin: *");
	$connect = mysqli_connect("localhost", "root", "", "rfid-inventory-final");
	mysqli_query($connect, "SET NAMES 'utf8'");
?>