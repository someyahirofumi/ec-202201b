package jp.co.example.ecommerce_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author honda yuki
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	@RequestMapping("/confirm")
	public String showOrderConfirm() {
		return "order_confirm";
	}
}
