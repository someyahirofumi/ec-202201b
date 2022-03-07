'use strict';

/*let $totalPrice=document.getElementById('total-price');
let $sizeM = document.getElementById('sizeM');
let $sizeL = document.getElementById('sizeL');
let $sizeMprice=document.getElementById('sizeMprice');
let $sizeLprice=document.getElementById('sizeLprice');
let $toppinglist =document.querySelectorAll(".toppingCount");


$sizeM.addEventListener('change',()=>{
	$totalPrice.innerText=removeComma($totalPrice.innerText)-removeComma($sizeLprice.innerText)+removeComma($sizeMprice.innerText);
})
$sizeL.addEventListener('change',()=>{
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
	
})*/

$(function(){
  calc_price();
  $(".size").on("change", function () {
	
    calc_price();
  });

  $(".toppingCount").on("change", function () {
	
    calc_price();
  });

  $("#quantity").on("change", function () {
	
    calc_price();
  });

function removeComma(number) {
    var removed = number.replace(/,/g, '');
    return parseInt(removed, 10);
}
function calc_price(){
	 let size = $(".size:checked").val();
    let topping_count = $("#topping input:checkbox:checked").length;
    let quantity = $("#quantity").val();
    let size_price = 0;
    let topping_price = 0;
    if (size === "M") {
      size_price =removeComma( $("#priceM").val());
      topping_price = 200 * topping_count;
    } else {
      size_price = removeComma( $("#priceL").val());
      topping_price = 300 * topping_count;
    }
    let price = (size_price + topping_price) * quantity;
    $("#total-price").text(price.toLocaleString());
  }
	

});
