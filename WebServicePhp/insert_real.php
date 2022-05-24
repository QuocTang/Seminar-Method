<?php
	require "dbConn.php";

	$item_code = $_POST['item_code'];
	$real_value = $_POST['real_value'];
	// $item_code = "001D";
	// $real_value = "1";
	$time = "now()";
	$product_id = "SELECT product_id FROM theory_product WHERE item_code = '$item_code'";
	$data = mysqli_query($connect, $product_id);
	$row = mysqli_fetch_assoc($data);

	$id = $row['product_id'];
	
	$query = "INSERT INTO real_product VALUES ('$item_code', '$real_value', '$id',$time)";
	$insert = mysqli_query($connect, $query);
	
	if($data && $insert){
		echo "Success";
	}else{
		echo "Error";
	}

?>