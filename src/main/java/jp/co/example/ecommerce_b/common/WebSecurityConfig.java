package jp.co.example.ecommerce_b.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    UserDetailsService userDetailsService;
	
	
	//セキュリティ対象外を指定
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers( "/css/**"
						, "/img/**"
						, "/js/**"
						, "/fonts/**");
	}
	
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests() // 認可に関する設定
		
		//ログイン前に閲覧可能なページを指定する。
		//①ログイン初期表示画面（/user/）②ユーザー登録画面（/user/toRegisterUser)③ログイン画面（/login)→各画面から遷移する？
		//
	    .antMatchers("/user/","/user/toRegisterUser","/login").permitAll() 
		.anyRequest().authenticated(); // それ以外のパスは認証が必要

		
	http.formLogin() // ログインに関する設定
		.loginPage("/user/") // ログイン画面に遷移させるパス(ログイン認証が必要なパスを指定してかつログインされていないとこのパスに遷移される)
		.loginProcessingUrl("/user/login") // ログインボタンを押した際に遷移させるパス(ここに遷移させれば自動的にログインが行われる)
	    .usernameParameter("email") // 認証時に使用するユーザ名のリクエストパラメータ名(今回はメールアドレスを使用)
	    .passwordParameter("password")// 認証時に使用するパスワードのリクエストパラメータ名
	    .defaultSuccessUrl("/user/toRegisterUser");//ログイン成功時の遷移先（仮でユーザー登録画面を指定）
	
	
        
        
    }
	
	
	/**
     * bcryptアルゴリズムでハッシュ化する実装を返します.
     * @return bcryptアルゴリズムでハッシュ化する実装オブジェクト
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
    		return new BCryptPasswordEncoder();
    }
    
    /**
	 * 「認証」に関する設定.<br>
	 * 認証ユーザを取得する「UserDetailsService」の設定や<br>
	 * パスワード照合時に使う「PasswordEncoder」の設定
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		
	}
    

}
