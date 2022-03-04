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



/**
 * ユーザー情報の操作を行うコントローラー
 * 
 * @author ishida fuya
 *
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
	
	/**
	 * 初期表示を行う
	 * 
	 * 
	 *
	 */
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
	
	
	/**
	 * ユーザー情報の登録処理
	 * 
	 * @param name,email,password,zipcode,address,telephone,confirmationPassword（パスワードの一致確認に使用）
	 * 
	 * 
	 */
	/**
	 * メールアドレスの重複確認
	 * 
	 * @param email
	 * 入力されたメールアドレスの内容を検索し、ユーザー情報にすでに登録されている場合
	 * エラーメッセージを返す
	 */
	@RequestMapping("/registerUser")
	public String registerUser(@Validated UserForm form,BindingResult result,Model model) {
		System.out.println("確認" +  form.getEmail());
		System.out.println("確認" +  userservice.emailCheck(form.getEmail()));
		
		//メールアドレスの重複確認
		if(! userservice.emailCheck(form.getEmail()).isEmpty()) {
			
			result.rejectValue("email",null,"そのメールアドレスはすでに使われています");
			
		}
		
		//パスワードと確認用パスワードの一致確認
		if(! form.getPassword().equals(form.getConfirmationPassword())) {
			result.rejectValue("password",null,"パスワードと確認用パスワードが不一致です");
		}
	
		//入力内容に不備がある場合、エラーメッセージを返す
		if(result.hasErrors()) {
			
			return index();
		}
		
		//上記の確認後、不備がなければ登録処理を行う。
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

