package jp.co.example.ecommerce_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.form.UserForm;




@Controller
@RequestMapping("/register")
public class RegisterController {
	
	//最初の処理
	@RequestMapping("")
	public String index() {
		return "/register_user";
	}
	
	@ModelAttribute
	public UserForm setUpForm() {
		return new UserForm();
	}
	//入力欄の値を受け取り、その後の処理
	@RequestMapping("/registerUser")
	public String registerUser(@Validated UserForm form,BindingResult result,Model model) {
		if(result.hasErrors()) {
			return index();
		}
		return "/login";
	}
	
}

