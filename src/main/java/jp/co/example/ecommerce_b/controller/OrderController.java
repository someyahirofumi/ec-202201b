package jp.co.example.ecommerce_b.controller;



import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.form.UpdateOrderForm;
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
	
	@ModelAttribute
	public UpdateOrderForm setUpUpdateOrderForm() {
		return new UpdateOrderForm();
	}
	/**
	 * 
	 * @param model Orderオブジェクトを格納するモデル
	 * @return ログインしてなければログイン画面、注文情報がなければ商品一覧画面、あれば注文確認画面へ遷移
	 */
	@RequestMapping("/confirm")
	public String showOrderConfirm(UpdateOrderForm form, Model model) {
		session.setAttribute("userId", 1);
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/login";
		}
		//仮データを入れて検証
		Order order = orderService.showCart(userId, 0);
		if (order == null) {
			return "item_list_curry";
		}
		//配達日時をマップに格納
		Map<String, Integer> deliveryTimeMap = new LinkedHashMap<>();
		for (int i = 10; i <= 18; i++) {
			deliveryTimeMap.put(i + "時", i);
		}
		//formの初期値を設定
		if (form.getDeliveryTime() == null) {
			form.setDeliveryTime("10");
		}
		if (form.getPaymentMethod() == null) {
			form.setPaymentMethod("1");
		}
		
		model.addAttribute("deliveryTimeMap", deliveryTimeMap);
		model.addAttribute("order", order);
		
		return "order_confirm";
	}
	
	/**
	 * 入力値から注文テーブルを更新する
	 * 
	 * @param form 入力値
	 * @param result エラー文格納オブジェクト
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/post")
	public String order(
			@Validated UpdateOrderForm form,
			BindingResult result,
			Model model) throws ParseException {
		//送信時の日付をlong型で取得
		long today = new Date().getTime();
		
		//入力された日付と時刻を文字列として結合したあとにlong型でフォーマット
		String delivaryDateTime = form.getDeliveryDate() + " " + form.getDeliveryTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh");
		long delivaryDateTimeLong = sdf.parse(delivaryDateTime).getTime();
		
		//時刻を比較して時間差を抽出
		long diff = delivaryDateTimeLong - today;
		TimeUnit time = TimeUnit.HOURS;
		long difference = time.convert(diff, TimeUnit.MILLISECONDS);
		
		//時間差が3時間より小さけれればエラー文を格納
		if (difference < 3) {
			result.rejectValue("deliveryDate", null, "今から3時間後の日時をご入力ください");
		}
		//1つでもエラーがあれば注文完了画面へ
		if (result.hasErrors()) {
			return showOrderConfirm(form, model);
		}
		//java.sql.Dateへ現時点の日付を変換
		java.sql.Date sqlToday = new java.sql.Date(today);
		Timestamp todayTimestamp = new Timestamp(delivaryDateTimeLong);
		
		//orderオブジェクトにformの値をコピー
		Order order = orderService.showCart((Integer) session.getAttribute("userId"), 0);
		if (order == null) {
			return "redirect:/item/showList";
		}
		BeanUtils.copyProperties(form, order);
		
		//コピーできなかった値を手動でコピー
		order.setId(order.getId());
		order.setOrderDate(sqlToday);
		order.setDelivaryTime(todayTimestamp);
		order.setPaymentMethod(form.getIntPaymentMethod());
		if (order.getPaymentMethod() == 1) {
			order.setStatus(1);
		} else if (order.getPaymentMethod() == 2) {
			order.setStatus(2);
		}
		
		orderService.orderUpdate(order);
		return "redirect:/order/completion";
	}
	
	/**
	 * 注文完了画面を表示する
	 * 
	 * @return
	 */
	@RequestMapping("/completion")
	public String complete() {
		return "order_finished";
	}
	
	@RequestMapping("/history")
	public String showHistrory() {
		return "order_history";
	}
}
