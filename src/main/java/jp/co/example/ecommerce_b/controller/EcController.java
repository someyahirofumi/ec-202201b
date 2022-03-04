package jp.co.example.ecommerce_b.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.jdi.connect.Connector.IntegerArgument;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.Topping;
import jp.co.example.ecommerce_b.form.IntoCartForm;
import jp.co.example.ecommerce_b.service.Itemservice;

@Controller
@RequestMapping("")
public class EcController {
	
	@Autowired 
	private Itemservice service;
	
	
	
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	public IntoCartForm setUpForm() {
		return new IntoCartForm();
	}
	
	@RequestMapping("")
	public String index() {
		return "login";
	}
	
	
	/**　送られてきたitemIDをもとにして商品を取得するメソッド
	 * トッピング全件取得のsqlも実行し、トッピングリストをitemオブジェクトに格納
	 * @param itemId　商品一覧画面より遷移するときに送られてくる Id
	 * @param model　
	 * @return　商品詳細画面へフォワード
	 */
	@RequestMapping("/toItemDetail")
	public String toItemDetail(Integer itemId,Model model) {
//		System.out.println("システム起動");
//		Item item = service.findByItemId(itemId);
		Item item = service.findByItemId(1);
		
		List<Topping> toppingList = service.toppingFindAll();
		item.setToppingList(toppingList);
		model.addAttribute("item",item);
//		System.out.println(item);
//		System.out.println(toppingList);
//		
		return"item_detail";
	}
	
	@RequestMapping("/intoCart")
	public String intoCart(IntoCartForm form,Model model,Integer itemId) {
		Order order = new Order();
	    //ログインしていない状態でカートに追加した場合
		if(session.getAttribute("userId") == null) {ß
		UUID uuID= UUID.randomUUID();
		String uuid = uuID.toString();
		
		session.setAttribute("preId", uuID);
		order.setPreId(uuid);
		}else if(session.getAttribute("userId") != null) {
			order.setUserId((Integer)session.getAttribute("userId") );
		}
		
		System .out .println(form.getPriceM());
		System .out .println(form.getToppingId().size());
		//小計金額の計算
		int total =0;
		if(form.getPriceM() != null) {
			total += form.getPriceM();
			total += form.getToppingId().size()*200;
		}else if(form.getPriceL() != null) {
			total += form.getPriceL();
			total += form.getToppingId().size()*300;
		}
		total *= form.getQuantity();
		
		order.setTotalPrice(total);
		order.setStatus(0);
		
		return toItemDetail(1,model);
		
		
	}

}
