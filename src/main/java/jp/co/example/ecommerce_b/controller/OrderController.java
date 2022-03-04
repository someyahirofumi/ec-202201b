package jp.co.example.ecommerce_b.controller;



import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.service.OrderService;

/**
 * 
 * @author honda yuki
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private HttpSession session;
	/**
	 * 
	 * @param model Orderオブジェクトを格納するモデル
	 * @return ログインしてなければログイン画面、注文情報がなければ商品一覧画面、あれば注文確認画面へ遷移
	 */
	@RequestMapping("/confirm")
	public String showOrderConfirm(Model model) {
		if (session.getAttribute("userId") == null) {
			return "redirect:/login";
		}
		//仮データを入れて検証
		Order order = orderService.showCart(1, 0);
		if (order == null) {
			return "item_list_curry";
		}
		//System.out.println(order);
		//System.out.println(order.getOrderItemList().get(0).getSubTotal());
		model.addAttribute("order", order);
		
		return "order_confirm";
	}
	
	@RequestMapping("/post")
	public String order() {
		return "redirect:/completion";
	}
	
}
