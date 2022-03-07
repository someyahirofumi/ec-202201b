'use strict'

let $totalPrice=document.getElementById('total-price');
let $sizeM = document.getElementById('sizeM');
let $sizeL = document.getElementById('sizeL');
let $sizeMprice=document.getElementById('sizeMprice');
let $sizeLprice=document.getElementById('sizeLprice');
let $toppinglist =document.querySelectorAll(".toppingCount");

function removeComma(number) {
    var removed = number.replace(/,/g, '');
    return parseInt(removed, 10);
}
$sizeM.addEventListener('click',()=>{
	$totalPrice.innerText=removeComma($totalPrice.innerText)-removeComma($sizeLprice.innerText)+removeComma($sizeMprice.innerText);
})
$sizeL.addEventListener('click',()=>{
$totalPrice.innerText=removeComma($totalPrice.innerText)-removeComma($sizeMprice.innerText)+removeComma($sizeLprice.innerText);                                                                                                                                         
})
$toppinglist.forEach(function(target){
	
	target.addEventListener('change',()=>{
	
	

	let count =0;
	for(let i = 0; i<$toppinglist.length;i++){
		if($toppinglist[i].checked){
			count ++;
		}
		console.log(count);
	}
	if($sizeM.checked){
		$totalPrice.innerText= removeComma($totalPrice.innerText)+(200*count);
	}else if($sizeL.checked){
		$totalPrice.innerText= removeComma($totalPrice.innerText)+(300*count);
	}
	
})
	
})

