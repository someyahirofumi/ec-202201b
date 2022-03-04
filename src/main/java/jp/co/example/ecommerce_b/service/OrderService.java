package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Order;
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
	
	/**
	 * 
	 * @param order 宛先が格納された注文情報
	 */
	public void orderUpdate(Order order) {
		orderRepository.update(order);
	}
}
