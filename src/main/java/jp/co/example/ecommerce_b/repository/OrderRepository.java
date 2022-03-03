package jp.co.example.ecommerce_b.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;
import jp.co.example.ecommerce_b.domain.Topping;

/**
 * 
 * @author honda yuki
 *
 */
@Repository
public class OrderRepository {
	
	private static final ResultSetExtractor<List<Order>> ORDER_ORDERITEM_ORDERTOPPING_EXTRACTOR = (rs) -> {
		List<Order> orderList = new ArrayList<>();
		List<OrderItem> orderItemList = null;
		List<OrderTopping> orderToppingList = null;
		int beforeId = 0;
		int beforeOrderItemId = 0;
		
		while (rs.next()) {
			int nowId = rs.getInt("id");
			int nowOrderItemId = rs.getInt("order_item_id");
			
			if (beforeId != nowId) {
				Order order = new Order();
				order.setId(nowId);
				order.setUserId(rs.getInt("user_id"));
				order.setTotalPrice(rs.getInt("total_price"));
				orderItemList = new ArrayList<>();
				order.setOrderItemList(orderItemList);
				orderList.add(order);
			}
			
			if (beforeOrderItemId != nowOrderItemId) {
				OrderItem orderItem = new OrderItem();
				orderItem.setItemId(rs.getInt("item_id"));
				orderItem.setQuantity(rs.getInt("quantity"));
				orderItem.setSize(rs.getString("size").toCharArray()[0]);
				Item item = new Item();
				item.setName(rs.getString("item_name"));
				item.setPriceM(rs.getInt("item_price_M"));
				item.setPriceL(rs.getInt("item_price_L"));
				item.setImagePath(rs.getString("image_path"));
				orderItem.setItem(item);
				orderToppingList = new ArrayList<>();
				orderItem.setOrderToppingList(orderToppingList);
				orderItemList.add(orderItem);
			}
			
			if (rs.getInt("topping_id") > 0) {
				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setToppingId(rs.getInt("topping_id"));
				Topping topping = new Topping();
				topping.setName(rs.getString("topping_name"));
				topping.setPriceM(rs.getInt("topping_price_M"));
				topping.setPriceL(rs.getInt("topping_price_L"));
				orderTopping.setTopping(topping);
				orderToppingList.add(orderTopping);
			}
			
			beforeId = nowId;
			beforeOrderItemId = nowOrderItemId;
		}
		
		return orderList;
	};
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public Order findByUserIdAndStatus(Integer userId, Integer status) {
		String sql = "SELECT"
				+ " o.id as order_id,"
				+ " total_price,"
				+ " order_item_id"
				+ " item_id"
				+ " i.name as item_name,"
				+ " i.price_M as item_price_M,"
				+ " i.price_L as item_price_L,"
				+ " i.image_path as img_path,"
				+ " quantity,"
				+ " size,"
				+ " topping_id"
				+ " t.name as topping_name,"
				+ " t.price_M as topping_price_M,"
				+ " t.price_L as topping_price_L"
				+ " FROM"
				+ " orders as o"
				+ " LEFT OUTER JOIN order_items as oi ON o.id = oi.order_id"
				+ " LEFT OUTER JOIN order_toppings as ot ON oi.id=ot.order_item_id"
				+ " LEFT OUTER JOIN items as i ON oi.item_id=i.id"
				+ " LEFT OUTER JOIN toppings as t ON ot.topping_id=t.id"
				+ " WHERE user_id=:userId AND status=:status";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);
		
		List<Order> orderList = template.query(sql, param, ORDER_ORDERITEM_ORDERTOPPING_EXTRACTOR);
		if (orderList.isEmpty()) {
			return null;
		}
		return orderList.get(0);
	}
	
}
