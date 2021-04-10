<?php
function sumOfSimilarities($str){
	$n = strlen($str);
	$arr=[];
	$left = 0;
	$right=0;
	$result = $n;
	for($i = 1;$i<$n;$i++){
		//new calculation
		if($i>$right){
			$left = $i;
			$right = $i;
			while($right<$n && $str[$right-$left] == $str[$right]){
				$right++;
			}
			$result+= $right - $left;
			$arr[$i] = $right - $left;
			$right--;
		}
		else{//use previously calculated value
			$k = $i - $left;
			if($arr[$k]<$right-$i+1){
				$arr[$i] = $arr[$k];
			}else{
				$left = $i;
				while($right<$n && $str[$right-$left] == $str[$right]){
					$right++;
				}
				$result+= $right - $left;
				$arr[$i] = $right - $left;
				$right--;
			}
		}
	}

	return $result;
}


$handle = fopen ("php://stdin","r");

fscanf($handle, "%d\n", $steps);
$cases = [];

for($i=0;$i<$steps;$i++){
	$s1 = '';
    fscanf($handle, "%[^\n]", $s1);
    $cases[]=$s1;
}

for($i=0;$i<$steps;$i++){
	echo sumOfSimilarities($cases[$i]) . "\n";
}


fclose($handle);
?>