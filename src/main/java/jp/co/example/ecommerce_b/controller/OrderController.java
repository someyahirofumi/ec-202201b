package jp.co.example.ecommerce_b.controller;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;
import jp.co.example.ecommerce_b.domain.Topping;
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
	
	@RequestMapping("/intoCart")
	public String intoCart(IntoCartForm form,Model model,Integer itemId) {
		System .out .println(session.getAttribute("preId"));
		//仮登録
		itemId=1;
		//必要なオブジェクト生成
		Order order = new Order();
		OrderItem item = new OrderItem();
		OrderTopping topping = new OrderTopping();
		List<OrderTopping> toppingList = new ArrayList<>();

		//orderId初期化
		int orderId=0;
		
		//小計金額計算(カートの状態、ログイン状態は関係なしの共通処理)
		int total =0;
		if(form.getPriceM() != null) {
			total += form.getPriceM();
			total += form.getToppingId().size()*200;
			item.setSize('M');
		}else if(form.getPriceL() != null) {
			total += form.getPriceL();
			total += form.getToppingId().size()*300;
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
		System.out.println("コントローラでのorderId"+orderId);
		int itemIdd = orderService.getItemId(orderId);
		for(Integer id:form.getToppingId()) {
			topping.setToppingId(id);
			topping.setOrderItemId(itemIdd);
			toppingList.add(topping);
		//トッピングが選択されていた場合にorderToppingテーブルにinsert処理
		if (!toppingList.isEmpty()) {
		orderService.insertTopping(toppingList);
		}
		
	}
		return "cart_list";
	
}
}
