package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.repository.UserRepository;

/**
 * ユーザー情報の操作処理の設定
 * 
 * @author ishida fuya
 *
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userrepository;
	
	/**
	 * メールアドレス重複チェック
	 * 
	 * @author ishida fuya
	 *
	 */
	public List<User> emailCheck(String email) {
		return userrepository.emailCheck(email);
	}
	
	/**
	 * ユーザー情報の追加処理
	 * 
	 * 
	 *
	 */
	public User resgisterUser(User user) {
		return userrepository.registerUser(user);
	}
	
	/**
	 * ログイン処理
	 * 
	 * 
	 *
	 */
	public List<User> Login(String email,String password) {
		return userrepository.Login(email,password);
	}


}
