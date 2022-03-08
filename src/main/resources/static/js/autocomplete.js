'use strict'

$(function(){
  $.ajax({
    url: 'http://localhost:8080/ec-202201b/item/autocomplete',
    type: 'post',
    dataType: 'json'
  }).done(function(data){
    $('#autocomplete').autocompete({
      source: data.items
    });
  }).fail(function(){
    alert('エラー')
  });
});