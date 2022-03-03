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
	
	public Order showCart(Integer userId, Integer status) {
		return orderRepository.findByUserIdAndStatus(userId, status);
	}
}
