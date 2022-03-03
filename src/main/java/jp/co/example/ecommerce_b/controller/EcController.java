package jp.co.example.ecommerce_b.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Topping;
import jp.co.example.ecommerce_b.service.Itemservice;

@Controller
@RequestMapping("")
public class EcController {
	
	@Autowired 
	private Itemservice service;
	
	@RequestMapping("")
	public String index() {
		return "login";
	}
	
	
	@RequestMapping("/toItemDetail")
	public String toItemDetail(Integer itemId,Model model) {
		System.out.println("システム起動");
//		Item item = service.findByItemId(itemId);
		Item item = service.findByItemId(1);
		
		List<Topping> toppingList = service.toppingFindAll();
		item.setToppingList(toppingList);
		model.addAttribute("item",item);
//		System.out.println(item);
//		System.out.println(toppingList);
//		
		return"item_detail";
		
		
		
		
	}

}
