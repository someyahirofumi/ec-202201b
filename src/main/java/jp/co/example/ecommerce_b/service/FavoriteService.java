package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.repository.FavoriteRepository;

/**
 * 
 * @author hondayuki
 *
 */
@Service
public class FavoriteService {
	@Autowired
	private FavoriteRepository favoriteRepository;
	
	public void favorite(Integer userId, Integer itemId) {
		favoriteRepository.insert(userId, itemId);
	}
}
