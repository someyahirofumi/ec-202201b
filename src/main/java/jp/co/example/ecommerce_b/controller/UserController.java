package jp.co.example.ecommerce_b.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Users;
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
		Users user = new Users();
		
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
	@RequestMapping("")
	public String toLogin() {
		
		//ログイン状態の確認
		
		//ログイン済みの場合
		if(session.getAttribute("userInfo") != null) {
			return "/item_list_curry";
		}
		
		//未ログインの場合
		return "/login";
	}
	
	
	@Autowired
	private HttpSession session;
	
	
	
	/**
	 * ログイン処理
	 * 
	 * @param email,password
	 * @return ユーザー情報
	 * 入力内容に不備がある場合、エラーメッセージを返す
	 * 
	 */
	@RequestMapping("/login")
	public String Login(String email,String password,Model model) {
		
		
		
		//入力されたメールアドレス、パスワードからユーザー情報を検索
		//検索結果が０件のとき、エラーメッセージを表示
		
		//入力されたパスワードの値とハッシュ化されたパスワードの値の一致を確認する
	    BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
	    
	    
	    //if(bcpe.matches(password, userservice.searchPassword(email))) {
	    	
	    //}
	    
		if(userservice.Login(email, password) == null) {
			System.out.println("失敗");
			model.addAttribute("errorMessage","メールアドレス、またはパスワードが間違っています");
			return toLogin();
		}
		
		//sessionにユーザー情報を格納(idだけで良い？)
		System.out.println(userservice.Login(email, password));
		session.setAttribute("userInfo",userservice.Login(email, password));
		
		//遷移先の判定
		//(sessionスコープにuuidが存在するかの確認)
		if(session.getAttribute("preID") != null) {
			return "/item_detail";
		}
		
		
		//仮で注文一覧画面を表示
		//return "/item_list_curry";
		System.out.println("成功");
		return "/login";
	}
}

