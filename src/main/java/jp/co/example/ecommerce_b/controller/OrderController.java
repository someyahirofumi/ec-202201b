package jp.co.example.ecommerce_b.controller;



import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.UUID;

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
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;
import jp.co.example.ecommerce_b.form.IntoCartForm;
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
		order.setDeliveryTime(todayTimestamp);
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
	
	/**
	 * 注文履歴を表示する
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/history")
	public String showHistrory(Model model) {
		//セッションからuserIdを取得
		Integer userId = (Integer) session.getAttribute("userId");
		
		//List<Order> orderList = orderService.getHistory(1);
		List<Order> orderList = orderService.getHistory(userId);
		model.addAttribute("orderList", orderList);
		//System.out.println(orderList);
		return "order_history";
	}
	@RequestMapping("/intoCart")
	
	public String intoCart(IntoCartForm form,Model model,Integer itemId) {
		//動作確認用
		session.setAttribute("userId", 2);
		
		//仮登録
		itemId=1;
		//必要なオブジェクト生成
		Order order = new Order();
		OrderItem item = new OrderItem();
		
		List<OrderTopping> toppingList = new ArrayList<>();

		//orderId初期化
		int orderId=0;
		
		//小計金額計算(カートの状態、ログイン状態は関係なしの共通処理)
		int total =0;
		if(form.getPriceM() != null) {
			total += form.getPriceM();
			//トッピングが選択されている場合の処理
			if(!(form.getToppingId().isEmpty())) {
			total += form.getToppingId().size()*200;
			}
			item.setSize('M');
		}else if(form.getPriceL() != null) {
			total += form.getPriceL();
			if(!(form.getToppingId().isEmpty())) {
			total += form.getToppingId().size()*300;
			}
			item.setSize('L');
		}
		total *= form.getQuantity();
		//orderオブジェクトに小計金額とステータスをセット
		order.setTotalPrice(total);
		order.setStatus(0);
		//ログインしているか否か判定
		if(session.getAttribute("userId") !=null) {
			//ログインしている場合の処理
			//userIdをorderオブジェクトにセット
			order.setUserId((Integer)session.getAttribute("userId") );
			//カート内に商品があるか否かの判定のためuserIdでordersテーブルを検索
			 orderId=orderService.getOrderId((int)session.getAttribute("userId"));
			if(orderId ==0) {
				//カートが空の状態時の処理
				//orderテーブルへのinsert処理(カートへの新規追加)
				orderService.intoCart(order);
				//直前でinsertしたorderのIDを取得
				orderId=orderService.getOrderId((int)session.getAttribute("userId"));
			}else {
				//カートが空じゃない場合の処理
				//既にカートに入っている合計金額を取得
				Integer preTotal= orderService.getTotalPrice((Integer)session.getAttribute("userId"));
				//今カートに追加した商品の金額をカートリスト合計と合わせる
				total += preTotal;
				order.setTotalPrice(total);
				orderService.updateOrder(order);
			}
			//orderIdをセット
			item.setOrderId(orderId);
			
		}else {
			//ログインしていない状態の処理
			//userIdを0でセット(userIdはNot Null設定のため)
			order.setUserId(0);
			
			//preIdが発行されていない＝カートに商品がない状態
			if(session.getAttribute("preId") ==null) {
				//カートが空の状態時の処理
				//新規にpreIdを作成オブジェクトにセット
				
				UUID uuID= UUID.randomUUID();
				String uuid = uuID.toString();
				
				session.setAttribute("preId", uuid);
				order.setPreId(uuid);
				//orderテーブルへのinsert処理(カートへの新規追加)
				orderService.intoCart(order);
				//直前でinsertしたorderのIDを取得
				 orderId=orderService.getOrderId2(session.getAttribute("preId").toString());
			}else {
				//カートが空じゃない場合の処理
				//*注意　動作確認時、一度preId発行した後にDBでデータ削除すると正常な動作できなくなる
				//発行済みのpreIdをもとにorderIdを取得
				 orderId=orderService.getOrderId2(session.getAttribute("preId").toString());
				//過去にカート追加した際発行されたpreIdをorderオブジェクトにセット
				order.setPreId(session.getAttribute("preId").toString());
				//既にカートに入っている合計金額を取得
				Integer preTotal= orderService.getNotLoginTotalPrice((String)session.getAttribute("preId"));
				//今カートに追加した商品の金額をカートリスト合計と合わせる
				total += preTotal;
				order.setTotalPrice(total);
				orderService.updateOrder(order);
				//合計金額の更新
				orderService.updateOrder(order);
			}
			//orderIdをセット
			item.setOrderId(orderId);
		
		}
		
//	　　　itemIdをセット
		item.setItemId(itemId);
		
		
		//個数をセット
		item.setQuantity(form.getQuantity());
		//orderテーブルへのinsert処理
		orderService.insertItem(item);
		//formのトッピングリストから情報を一件ずつ取得し、新たなリストに格納
		
		int itemIdd = orderService.getItemId(orderId);
		
		for(Integer id:form.getToppingId()) {
			OrderTopping topping = new OrderTopping();
			topping.setToppingId(id);
			topping.setOrderItemId(itemIdd);
			toppingList.add(topping);
			}
		//トッピングが選択されていた場合にorderToppingテーブルにinsert処理
			
		if (!toppingList.isEmpty()) {
		orderService.insertTopping(toppingList);
		}
		return toCartList(model);
	}
	
	

	

	@RequestMapping("toCartList")
	
	public String toCartList(Model model) {
		
		Order order = new Order();
		//ログイン状態の条件分岐
		if(session.getAttribute("userId") !=null) {
			//ログインしている
			order=orderService.getCartList((Integer)session.getAttribute("userId"));
			
		}else if(session.getAttribute("preId") != null) {
			//非ログイン
			order=orderService.getNotLoginCartList((String)session.getAttribute("preId"));
		}
		//orderListをrequestスコープに格納(orderList)
		if(order == null) {
			model.addAttribute("cartNullMessage","カートに商品がありません");
		}else {
		model.addAttribute("cart",order);
		}
		return "cart_list";
	}
	
	@RequestMapping("/deleteCart")
	public String deleteCart(Integer orderId,Integer subTotalPrice,Model model) {
		
		Order order = new Order();
		
//		cartItem=orderService.getOrderItem(orderId);
	
		int total=0;
		//ログイン状態の分岐
		if(session.getAttribute("userId") != null) {
			//ログインユーザーの処理
			total=orderService.getTotalPrice((Integer)session.getAttribute("userId"));
			order.setUserId((Integer)session.getAttribute("userId"));
			
		}else if(session.getAttribute("preId") != null) {
			total=orderService.getNotLoginTotalPrice((String)session.getAttribute("preId"));
			order.setPreId((String)session.getAttribute("preId"));
		}
		total -= subTotalPrice;
		order.setTotalPrice(total);
		orderService.updateOrder(order);
		orderService.deleteCart(orderId);
		return toCartList(model);
	}
}
