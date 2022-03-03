package jp.co.example.ecommerce_b.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.service.UserService;




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
	
	@Autowired
	private UserService userservice;
	@RequestMapping("/registerUser")
	public String registerUser(@Validated UserForm form,BindingResult result,Model model) {
		
		//System.out.println(userservice.emailCheck(form.getEmail()));
		//メールアドレスの重複確認
		//if(userservice.emailCheck(form.getEmail() != null)) {
			
		//}
		
		//パスワードと確認用パスワードの一致確認
		if(! form.getPassword().equals(form.getConfirmationPassword())) {
			result.rejectValue("password",null,"パスワードと確認用パスワードが不一致です");
		}
	
		if(result.hasErrors()) {
			return index();
		}
		
		//上記の確認後、問題がなければ登録処理を行う。
		User user = new User();
		
		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPassword(form.getPassword());
		user.setZipcode(form.getZipcode());
		user.setAddress(form.getAddress());
		user.setTelephone(form.getTelephone());
		
		userservice.resgisterUser(user);
		
		return "/login";
	}
	
}

