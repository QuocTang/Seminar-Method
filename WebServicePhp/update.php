<?php
	require "dbConn.php";

	$item_code = $_POST['item_code'];
	$real_value = $_POST['real_value'];
	$theory_value = $_POST['theory_value'];

	$query = "UPDATE audit_report SET item_code = '$item_code', real_value = '$real_value', theory_value = '$theory_value' WHERE item_code = '$item_code'";

	if(mysqli_query($connect, $query)){
		echo "Success";
	}else{
		echo "Error";
	}
?>
