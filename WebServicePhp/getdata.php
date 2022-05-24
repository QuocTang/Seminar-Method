<?php
	require "dbConn.php";
	// SELECT r.product_id, SUM(real_value) as real_value, SUM(theory_value) as theory_value, time FROM real_product as r, theory_product as t WHERE r.item_code = t.item_code GROUP BY r.product_id
	
	$query = "SELECT r.item_code, r.real_value, t.theory_value, t.theory_value - r.real_value as gap, r.time from real_product as r, theory_product as t WHERE r.item_code = t.item_code";

	$data = mysqli_query($connect, $query);

	$json_array = array();
	while($row = mysqli_fetch_assoc($data)){
		$json_array[] = $row;
	}
	echo json_encode($json_array);
?>
