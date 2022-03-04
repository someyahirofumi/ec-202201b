package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.User;

/**
 * ユーザー情報の操作処理の設定
 * 
 * @author ishida fuya
 *
 */
@Repository
public class UserRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<User>USER_ROW_MAPPER = (rs,i) ->{
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setZipcode(rs.getString("zipcode"));
		user.setTelephone(rs.getString("telephone"));
		user.setPassword(rs.getString("password"));
		
		return user;
	};
	
	/**
	 * ユーザー情報の検索処理
	 * @param email
	 * @return 
	 *
	 */
	public List<User> emailCheck(String email) {
		String sql = "SELECT id,name,email,zipcode,telephone,password FROM users WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource ().addValue("email", email);
		
		//検索結果がない場合エラーになる。
		//エラー文：Incorrect result size: expected 1, actual 0
		//User user= template.queryForObject(sql, param, USER_ROW_MAPPER);
		List<User>userList = template.query(sql, param, USER_ROW_MAPPER);
		return userList;
	}
	
	
	
	/**
	 * ユーザー情報の追加処理
	 * 
	 * @author ishida fuya
	 *
	 */
	public User registerUser(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String insertsql = "INSERT INTO users(name,email,password,zipcode,address,telephone) "
				+ "VALUES(:name,:email,:password,:zipcode,:address,:telephone)";
		
		template.update(insertsql, param);
		return user;
	}

}
