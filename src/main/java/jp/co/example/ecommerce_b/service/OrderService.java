package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;
import jp.co.example.ecommerce_b.repository.OrderRepository;

/**
 * 
 * @author honda yuki
 *
 */
@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	/**
	 * 
	 * @param userId ユーザーID
	 * @param status 注文状態
	 * @return カート情報が入ったOrderオブジェクト
	 */
	public Order showCart(Integer userId, Integer status) {
		return orderRepository.findByUserIdAndStatus(userId, status);
	}
	
	
	public void intoCart(Order order) {
		orderRepository.intoCart(order);
	}
	
	public int getOrderId(Integer userId) {
		return orderRepository.getOrderId(userId);
	}
	public int getOrderId2(String preId) {
		return orderRepository.getOrderId2(preId);
	}
	
	public void insertItem(OrderItem item) {
		orderRepository.insertItem(item);
	}
	
	public void insertTopping(List<OrderTopping> list) {
		orderRepository.insertTopping(list);
	}
	
	public void updateOrder(Order order) {
		orderRepository.updateOrder(order);
	}
	
	public int getItemId(Integer orderId) {
		return orderRepository.getItemId(orderId);
		
	}
}
