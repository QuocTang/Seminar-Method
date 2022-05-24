<?php
	require "dbConn.php";

	$item_code = $_POST['item_code'];
	// $item_code = "35000000001000000";

	$query = "DELETE FROM real_product WHERE item_code = '$item_code'";

	if(mysqli_query($connect, $query)){
		echo "Succ";
	}else{
		echo "Err";
	}
?>
<?php
	require "dbConn.php";

	$item_code = $_POST['item_code'];
	// $item_code = "35000000001000000";

	$query = "DELETE FROM theory_product WHERE item_code = '$item_code'";

	if(mysqli_query($connect, $query)){
		echo "ess";
	}else{
		echo "or";
	}
?>