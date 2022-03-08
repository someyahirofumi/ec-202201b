package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Favorite;
import jp.co.example.ecommerce_b.domain.Item;

/**
 * 
 * @author hondayuki
 *
 */
@Repository
public class FavoriteRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Favorite> FAVORITE_ROW_MAPPER = (rs, i) -> {
		Favorite favorite = new Favorite();
		favorite.setUserId(rs.getInt("user_id"));
		favorite.setItemId(rs.getInt("item_id"));
		Item item = new Item();
		item.setId(rs.getInt("item_id"));
		item.setImagePath(rs.getString("image_path"));
		item.setName(rs.getString("name"));
		favorite.setItem(item);
		
		return favorite;
	};
	
	public void insert(Integer userId, Integer itemId) {
		String sql = "INSERT INTO favorites VALUES"
				+ " (user_id, item_id)"
				+ " (:userId, :itemId)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("itemId", itemId);
		
		template.update(sql, param);
	}
}
