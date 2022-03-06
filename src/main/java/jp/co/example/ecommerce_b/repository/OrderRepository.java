package jp.co.example.ecommerce_b.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
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
			int nowId = rs.getInt("order_id");
			int nowOrderItemId = rs.getInt("order_item_id");

			if (beforeId != nowId) {
				Order order = new Order();
				order.setId(nowId);
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
		String sql = "SELECT" + " o.id as order_id," + " total_price," + " order_item_id," + " item_id,"
				+ " i.name as item_name," + " i.price_M as item_price_M," + " i.price_L as item_price_L,"
				+ " image_path," + " quantity," + " size," + " topping_id," + " t.name as topping_name,"
				+ " t.price_M as topping_price_M," + " t.price_L as topping_price_L" + " FROM orders as o"
				+ " LEFT OUTER JOIN order_items as oi ON o.id = oi.order_id"
				+ " LEFT OUTER JOIN order_toppings as ot ON oi.id=ot.order_item_id"
				+ " LEFT OUTER JOIN items as i ON oi.item_id=i.id"
				+ " LEFT OUTER JOIN toppings as t ON ot.topping_id=t.id" + " WHERE user_id=:userId AND status=:status";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);

		List<Order> orderList = template.query(sql, param, ORDER_ORDERITEM_ORDERTOPPING_EXTRACTOR);
		if (orderList.isEmpty()) {
			return null;
		}
		return orderList.get(0);
	}

	private static final RowMapper<Order> ORDER_ROW_MAPPER = new BeanPropertyRowMapper<>(Order.class);

	public void intoCart(Order order) {
		String sql = "INSERT INTO orders(user_id,pre_id,status,total_price)" + "   VALUES"
				+ "   (:userId,:preId,:status,:totalPrice);";

		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", order.getUserId())
				.addValue("preId", order.getPreId()).addValue("status", order.getStatus())
				.addValue("totalPrice", order.getTotalPrice());
		template.update(sql, param);

	}

	public void insertItem(OrderItem item) {
		// 注文商品への登録

		String sql2 = "INSERT INTO order_items(item_id,order_id,quantity,size) VALUES (:itemId,:orderId,:quantity,:size);";
		SqlParameterSource param2 = new MapSqlParameterSource().addValue("itemId", item.getItemId())
				.addValue("orderId", item.getOrderId()).addValue("quantity", item.getQuantity())
				.addValue("size", item.getSize());
		template.update(sql2, param2);
	}

	public void insertTopping(List<OrderTopping> list) {
		// 注文トッピングへの登録
		String sql3 = "INSERT INTO order_toppings(topping_id,order_item_id) VALUES(:toppingId,:orderItemId);";

		for (OrderTopping topping : list) {
			SqlParameterSource param3 = new MapSqlParameterSource().addValue("toppingId", topping.getToppingId())
					.addValue("orderItemId", topping.getOrderItemId());
			template.update(sql3, param3);
		}
	}
//userIdからorderIdを検索
	public int getOrderId(Integer userId) {
		String sql = "SELECT id FROM orders WHERE user_id =:userId AND status=0;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);

		if (orderList.isEmpty()) {
			return 0;
		} else {
			return orderList.get(0).getId();
		}
	}
//preIdからorderIdを検索
	public int getOrderId2(String preId) {
		String sql = "SELECT id FROM orders WHERE pre_id =:preId;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("preId", preId);
		List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);
		if (orderList.isEmpty()) {
			return 0;
		} else {
			return orderList.get(0).getId();
		}
	}
	
	//カート内の合計金額取得(userIdをもとに検索)
	//ログインユーザー用
	public int getTotalPrice(Integer userId) {
		String sql = "SELECT total_price FROM orders WHERE user_id = :userId;";
		SqlParameterSource param = new  MapSqlParameterSource().addValue("userId", userId);
		List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);
		if (orderList.isEmpty()) {
			return 0;
		} else {
			return orderList.get(0).getTotalPrice();
		}
	}
	//カート内の合計金額取得(preIdをもとに検索)
	//非ログインユーザー用
	public int getNotLoginTotalPrice(String preId) {
		String sql = "SELECT total_price FROM orders WHERE pre_id = :preId;";
		SqlParameterSource param = new  MapSqlParameterSource().addValue("preId", preId);
		List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);
		if (orderList.isEmpty()) {
			return 0;
		} else {
			return orderList.get(0).getTotalPrice();
		}
	}
	
	//カートに商品追加時、合計金額の変更update
	public void updateOrder(Order order) {
		if(order.getUserId() != null) {
		String sql ="UPDATE orders SET total_price =:totalPrice WHERE user_id=:userId AND status=0;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("totalPrice", order.getTotalPrice()).addValue("userId", order.getUserId());
		template.update(sql, param);
	}else if(order.getPreId() != null) {
		String sql ="UPDATE orders SET total_price =:totalPrice WHERE pre_id=:preId AND status=0;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("totalPrice", order.getTotalPrice()).addValue("preId", order.getPreId());
		template.update(sql, param);

	}
		
	}
	private static final RowMapper<OrderItem> ORDERITEM_ROW_MAPPER = new BeanPropertyRowMapper<>(OrderItem.class);
	//オーダーIDをもとにしてitemIdを取得
	//→最後にinsertしたidを取得
	
	public int getItemId(Integer orderId) {
//		System.out.println("repositoryでのorderID"+orderId);
		String sql ="SELECT MAX(id) as id FROM order_items WHERE order_id = :orderId ;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId);
		List<OrderItem> list = template.query(sql,param,ORDERITEM_ROW_MAPPER);
//		System.out.println(list);
		if (list.isEmpty()) {
			return 0;
		} else {
			return list.get(0).getId();
		}
	
		
	}
	
	//ログインユーザー用カートリスト取得メソッド
	//全てのテーブルのデータが入っていないと動かない＝ダミーデータを入れておく必要がある？トッピングのテーブル検索は分ける必要あり？
	public Order getCartList(Integer userId){
		String sql = 
				"SELECT o.id as order_id,o.total_price,oi.id as order_item_id,oi.quantity,oi.size,i.name as item_name,"
				        + "	ot.id as topping_id,i.price_m as item_price_M,i.price_l as item_price_L,"
						+ " i.id as item_id,i.image_path,t.name as topping_name,t.price_m as topping_price_M,t.price_l as topping_price_L"
						+ " FROM "
						+ " orders as o"
						+ " LEFT OUTER JOIN order_items as oi ON o.id=oi.order_id"
						+ " LEFT OUTER JOIN order_toppings as ot ON oi.id=ot.order_item_id"
						+ " INNER JOIN items as i ON oi.item_id= i.id"
						+ " INNER JOIN toppings as t ON ot.topping_id=t.id"
						+ " WHERE"
						+ " o.user_id=:userId AND o.status=0;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Order> cartList=new ArrayList<>();
		cartList= template.query(sql, param,ORDER_ORDERITEM_ORDERTOPPING_EXTRACTOR);
		 if (cartList.isEmpty()) {
				return null;
			} else {
				return cartList.get(0);
			}
	}
	
	//非ログインユーザー用カートリスト取得メソッド
	public Order getNotLoginCartList(String preId){
		String sql = 
				"SELECT o.id as order_id,o.total_price,oi.id as order_item_id,oi.quantity,oi.size,i.name as item_name,"
				        + "	ot.id as topping_id,i.price_m as item_price_M,i.price_l as item_price_L,"
						+ " i.id as item_id,i.image_path,t.name as topping_name,t.price_m as topping_price_M,t.price_l as topping_price_L"
						+ " FROM "
						+ " orders as o"
						+ " LEFT OUTER JOIN order_items as oi ON o.id=oi.order_id"
						+ " LEFT OUTER JOIN order_toppings as ot ON oi.id=ot.order_item_id"
						+ " INNER JOIN items as i ON oi.item_id= i.id"
						+ " INNER JOIN toppings as t ON ot.topping_id=t.id"
						+ " WHERE"
						+ " o.pre_id=:preId AND o.status=0;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("preId", preId);
		List<Order> cartList=new ArrayList<>();
		cartList= template.query(sql, param,ORDER_ORDERITEM_ORDERTOPPING_EXTRACTOR);
		 if (cartList.isEmpty()) {
				return null;
			} else {
				return cartList.get(0);
			}
		
	}
	
}
