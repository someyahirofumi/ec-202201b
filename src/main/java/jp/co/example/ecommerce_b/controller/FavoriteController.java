package jp.co.example.ecommerce_b.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	@RequestMapping("/add")
	public String favorite(Integer itemId) {
		Integer userId = (Integer) session.getAttribute("userId");
		favoriteService.favorite(userId, itemId);
		//favoriteService.favorite(1, 1);
		
		return "favorite_list";
	}
}
