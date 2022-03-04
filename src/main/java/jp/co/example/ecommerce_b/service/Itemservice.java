package jp.co.example.ecommerce_b.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Topping;
import jp.co.example.ecommerce_b.repository.Itemrepository;

@Service
@Transactional
public class Itemservice {
	
	@Autowired
	private Itemrepository itemRepository;
	
	public Item findByItemId(Integer itemId) {
		Item item=itemRepository.findByItemId(itemId);
		return item;
	}
	
	public List<Topping> toppingFindAll(){
		return itemRepository.findAll();
		
	}

}
