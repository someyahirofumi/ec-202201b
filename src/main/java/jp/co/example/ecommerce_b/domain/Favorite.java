package jp.co.example.ecommerce_b.domain;

/**
 * 
 * @author honda yuki
 *
 */
public class Favorite {
	/** ID */
	private Integer id;
	/** ユーザーID */
	private Integer user_id;
	/** アイテムID */
	private Integer item_id;
	/** 商品 */
	private Item item;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getItem_id() {
		return item_id;
	}
	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	@Override
	public String toString() {
		return "Favorite [id=" + id + ", user_id=" + user_id
				+ ", item_id=" + item_id + ", item=" + item
				+ "]";
	}
}
