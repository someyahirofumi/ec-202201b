package jp.co.example.ecommerce_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class EcController {
	
	@RequestMapping("")
	public String index() {
		return "login";
	}

}
