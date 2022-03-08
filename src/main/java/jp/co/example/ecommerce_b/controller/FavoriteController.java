package jp.co.example.ecommerce_b.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	@RequestMapping("/add/{itemId}")
	public String favorite(@PathVariable("itemId") Integer itemId) {
		session.setAttribute("userId", 1);
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId != null && favoriteService.confirmFavorite(userId, itemId).isEmpty()) {
			favoriteService.favorite(userId, itemId);
		}
		//favoriteService.favorite(1, 1);
		
		return "favorite_list";
	}
}
