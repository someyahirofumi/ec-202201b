'use strict'

const $totalPrice=document.getElementById('total-price');
const $sizeM = document.getElementById('sizeM');
const $sizeL = document.getElementById('sizeL');
const $sizeMprice=document.getElementById('sizeMprice');
const $sizeLprice=document.getElementById('sizeLprice');
$sizeM.addEventListener('click',()=>{
	$totalPrice.innerText = $sizeMprice.innerText;
})
$sizeL.addEventListener('click',()=>{
	$totalPrice.innerText = $sizeLprice.innerText;
})
