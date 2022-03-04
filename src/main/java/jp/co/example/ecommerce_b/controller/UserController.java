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
@RequestMapping("/user")
public class UserController {
	
	/**
	 * 初期表示を行う
	 * 
	 * 
	 *
	 */
	@RequestMapping("/toRegisterUser")
	public String toRegisterUser() {
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
	 * ①入力値の形式確認②メールアドレスの重複確認③パスワードと確認パスワードの一致確認
	 * 上記①〜③に不備がある場合はエラーメッセージを返す
	 */
	
	@RequestMapping("/registerUser")
	public String registerUser(@Validated UserForm form,BindingResult result,Model model) {
		
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
			
			return toRegisterUser();
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
		
		return toLogin();
	}
	
	/**
	 * ログイン画面の初期表示
	 * 
	 * @param
	 * 
	 * 
	 * 
	 */
	@RequestMapping("/")
	public String toLogin() {
		return "/login";
	}
	
	
	/**
	 * ログイン処理
	 * 
	 * @param email,password
	 * 
	 * 入力内容に不備がある場合、エラーメッセージを返す
	 * 
	 */
	@RequestMapping("/login")
	public String Login(String email,String password,Model model) {
		//入力されたメールアドレス、パスワードからユーザー情報を検索
		System.out.println(email + password);
		System.out.println(userservice.Login(email, password));
		
		//検索結果が０件のとき、エラーメッセージを表示
		if(userservice.Login(email, password).isEmpty()) {
			model.addAttribute("errorMessage","メールアドレス、またはパスワードが間違っています");
			return toLogin();
		}
		
		//仮で注文一覧画面を表示
		return "/item_list_curry";
	}
}

