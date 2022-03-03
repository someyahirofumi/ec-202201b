package jp.co.example.ecommerce_b.form;

import java.util.List;

public class IntoCartForm {
	

	private Integer totalPrice;
	private Integer itemId;
	private Integer quantity;
	private Character size;
	private List<Integer> toppingId;
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Character getSize() {
		return size;
	}
	public void setSize(Character size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "IntoCartForm [totalPrice=" + totalPrice + ", itemId=" + itemId + ", quantity=" + quantity + ", size="
				+ size + ", toppingId=" + toppingId + "]";
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public void setToppingId(List<Integer> toppingId) {
		this.toppingId = toppingId;
	}
	public List<Integer> getToppingId() {
		return toppingId;
	}
	
}
