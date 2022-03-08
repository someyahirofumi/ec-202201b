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

$(function() {
	calc_price();
	$(".size").on("change", function() {

		calc_price();
	});

	$(".toppingCount").on("change", function() {

		calc_price();
	});

	$("#quantity").on("change", function() {

		calc_price();
	});

	function removeComma(number) {
		var removed = number.replace(/,/g, '');
		return parseInt(removed, 10);
	}
	function calc_price() {
		let size = $(".size:checked").val();
		let topping_count = $("#topping input:checkbox:checked").length;
		let quantity = $("#quantity").val();
		let size_price = 0;
		let topping_price = 0;
		if (size === "M") {
			size_price = removeComma($("#priceM").val());
			topping_price = 200 * topping_count;
		} else {
			size_price = removeComma($("#priceL").val());
			topping_price = 300 * topping_count;
		}
		let price = (size_price + topping_price) * quantity;
		$("#total-price").text(price.toLocaleString());
	}

	$("#intoCartForm").on('submit', function(e) {
		e.preventDefault();
		console.log("非同期通信処理");
		let toppingList = [];
		$('[class="toppingCount"]:checked').each(function() {
			toppingList.push(($(this).val()))
		});
		console.log(toppingList);
		console.log(toppingList.join());



		$.ajax({
			url: 'http://localhost:8080/ec-202201b/intoCart/insert',
			type: 'post',
			dataType: 'text',
			data: {
				priceM: $('#priceM').val(),
				priceL: $('#priceL').val(),
				itemId: $('#itemNumber').val(),
				size: $('input:radio[name="size"]:checked').val(),
				toppingId: toppingList.join(),
				quantity: $('#quantity').val(),
			},

			async: true,
		}).done(function() {
			console.log("処理成功");
			$('.popup').addClass('show').fadeIn();
		}).fail(function(XMLHttpRequest, textStatus, errorThrown) {
			alert("正しい結果をえられませんでした");
			console.log("XMLHttpRequest : " + XMLHttpRequest.status);
			console.log("textStatus     : " + textStatus);
			console.log("errorThrown    : " + errorThrown.message);
		})


	});
	$('#close').on('click', function() {
		$('.popup').removeClass('show').fadeOut();
	});


});

