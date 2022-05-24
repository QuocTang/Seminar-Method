<?php
	require "dbConn.php";

	$item_code = $_POST['item_code'];
	$theory_value = $_POST['theory_value'];
	$product_id = $_POST['product_id'];

	$query = "INSERT INTO theory_product VALUES('$item_code', '$theory_value', '$product_id')";

	if(mysqli_query($connect, $query)){
		echo "Success";
	}else{
		echo "Error";
	}

?>