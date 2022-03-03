package jp.co.example.ecommerce_b.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Topping;

@Controller
@RequestMapping("")
public class EcController {
	
	@RequestMapping("")
	public String index() {
		return "login";
	}
	
	
	@RequestMapping("/toItemDetail")
	public String toItemDetail(Integer itemId,Model model) {
		Item item = service.findByItemId(itemId);
		List<Topping> toppingList = service.findAll();
		item.setToppingList(toppingList);
		model.addAttribute("item",item);
		return"item_detail";
		
		
		
		
	}

}
