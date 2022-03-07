package jp.co.example.ecommerce_b.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



/**
 * ユーザー登録時パスワードのハッシュ化、ログイン認証用設定
 * 
 * @author ishida fuya
 *
 */

@Configuration

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        //ログインページへのアクセスは全員許可する。
        http.formLogin()
            .loginPage("")
            .permitAll();

        http.authorizeRequests()
            .anyRequest().authenticated();
    }
	
	
	
	/**
     * bcryptアルゴリズムでハッシュ化する実装を返す.
     * @return bcryptアルゴリズムでハッシュ化する実装オブジェクト
     */
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
