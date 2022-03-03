package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Topping;


@Repository
public class Itemrepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Item> ITEM_ROW_MAPPER=new BeanPropertyRowMapper<>(Item.class);
	private static final RowMapper<Topping> TOPPING_ROW_MAPPER=new BeanPropertyRowMapper<>(Topping.class);
	
	
	public Item findByItemId(Integer itemId) {
		
		String sql = "SELECT id,name,description,price_m,price_l,image_path FROM items WHERE id=:itemId;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
		return item;
		
	}
	
	public List<Topping> findAll(){
		String sql = "SELECT id,name,price_m,price_l FROM toppings ; ";
		List<Topping> list=template.query(sql, TOPPING_ROW_MAPPER);
		return list;
	}
	
	


}
