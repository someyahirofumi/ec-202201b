package jp.co.example.ecommerce_b.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Favorite;
import jp.co.example.ecommerce_b.service.FavoriteService;

/**
 * 
 * @author hondayuki
 *
 */
@Controller
@RequestMapping("/favorite")
public class FavoriteController {
	@Autowired
	private FavoriteService favoriteService;
	
	@Autowired
	private HttpSession session;
	
	/**
	 * 商品をお気に入りに追加します
	 * 
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping("/add/{itemId}")
	public String favorite(@PathVariable("itemId") Integer itemId, Model model) {
		session.setAttribute("userId", 1);
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId != null && favoriteService.confirmFavorite(userId, itemId).isEmpty()) {
			favoriteService.favorite(userId, itemId);
		}
		//favoriteService.favorite(1, 1);
		
		return showList(model);
	}
	
	/**
	 * お気に入り一覧を表示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String showList(Model model) {
		session.setAttribute("userId", 1);
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "refirect:/login";
		}
		List<Favorite> favoriteList = favoriteService.showList(userId);
		model.addAttribute("favoriteList", favoriteList);
		
		return "favorite_list";
	}
	
	/**
	 * お気に入りを解除
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/remove")
	public String remove(Integer itemId) {
		Integer userId = (Integer) session.getAttribute("userId");
		favoriteService.remove(userId, itemId);
		System.out.println(userId + " " + itemId);
		
		return "redirect:/favorite/list";
	}
}
