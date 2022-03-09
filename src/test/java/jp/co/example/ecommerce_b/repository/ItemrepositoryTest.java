package jp.co.example.ecommerce_b.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import jp.co.example.ecommerce_b.domain.Item;

@SpringBootTest
class ItemrepositoryTest {

	@Autowired
	private Itemrepository itemRepository;

	@Autowired
	NamedParameterJdbcTemplate template;


	@Test
	public void FindAllItemList_18items() {
		System.out.println("全件検索するテスト開始");
		List<Item> itemList = itemRepository.findAllItemList();
		assertEquals(18, itemList.size(), "件数が一致しません");
		assertEquals("カツカレー", itemList.get(0).getName(), "安い順に並んでいません");
		assertEquals("えびナスカレー", itemList.get(17).getName(), "安い順に並んでいません");
		System.out.println("全件検索するテスト終了");
	}
	/*@Before
	public void testInsert() {
		System.out.println("DB初期化処理開始");
		Item item = new Item();
		item.setName(null);
		item.setDescription(null);
		item.setPriceM(null);
		System.out.println("インサートが完了しました。");
		System.out.println("DB初期化処理終了");
	}
	@Test
	public void FindAllItemList_0items() {
		System.out.println("全件検索するテスト開始");
		List<Item> item = itemRepository.findAllItemList();
		assertEquals(0,item.size(), "件数が一致しません");
		System.out.println("全件検索するテスト終了");
	}*/
	
}
