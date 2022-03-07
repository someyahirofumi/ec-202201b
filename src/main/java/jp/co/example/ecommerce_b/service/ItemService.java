package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.repository.Itemrepository;

@Service
@Transactional
public class ItemService {
	
	@Autowired
	private Itemrepository itemrepository;
	/**
	 * 全件検索 全商品一覧の表示
	 */
	public List<Item> itemList(){
		return itemrepository.findAll();
	}
	
	/**
	 * 全件検索　安い順
	 */
	public List<Item> lowList(){
		return itemrepository.findAllLow();
	}
	
	/**
	 * 全件検索　安い順
	 */
	public List<Item> highList(){
		return itemrepository.findAllHigh();
	}
	
	/**
	 * 商品検索
	 */
	public List<Item> search(String name) {
		return itemrepository.search(name);
	}
	
	/**
	 * 検索結果　安い順
	 */
	public List<Item> search1(String name) {
		return itemrepository.search1(name);
	}
}
