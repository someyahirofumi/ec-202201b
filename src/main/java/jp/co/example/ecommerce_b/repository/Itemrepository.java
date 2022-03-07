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

@Repository
public class Itemrepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Item> ITEM_ROW_MAPPER = new BeanPropertyRowMapper<>(Item.class);

	/**
	 * 全件検索 全商品一覧の表示
	 */

	public List<Item> findAll() {
		String sql = "SELECT id,name,description,price_m,price_l,image_path FROM items";
		List<Item> itemList=template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 全件検索　安い順
	 */
	public List<Item> findAllLow(){
		String sql = "SELECT id, name, description, price_m, price_l, image_path FROM items ORDER BY price_m ASC";
		List<Item> lowList=template.query(sql, ITEM_ROW_MAPPER);
		return lowList;
		
	}
	
	
	/**
	 * 全件検索　高い順
	 */
	public List<Item> findAllHigh(){
		String sql = "SELECT id, name, description, price_m, price_l, image_path FROM items ORDER BY price_m DESC";
		List<Item> highList=template.query(sql, ITEM_ROW_MAPPER);
		return highList;
		
	}
	/**
	 * 商品検索
	 */
	public List<Item> search(String name) {
		String sql="SELECT id,name,description,price_m,price_l,image_path FROM items WHERE name LIKE :name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+name+"%");
		List <Item> searchItem =template.query(sql, param, ITEM_ROW_MAPPER);
		return searchItem;
	}
	
	/**
	 *検索結果 安い順 
	 * @param name
	 * @return
	 */
	public List<Item> search1(String name) {
		String sql="SELECT id,name,description,price_m,price_l,image_path FROM items WHERE name LIKE :name ORDER BY price_m DESC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+name+"%");
		List <Item> searchItem1 =template.query(sql, param, ITEM_ROW_MAPPER);
		return searchItem1;
	}
	}

