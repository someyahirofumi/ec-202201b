package jp.co.example.ecommerce_b.form;

import java.util.List;

public class IntoCartForm {
	

	
	private Integer itemId;
	private Integer quantity;
	private Character size;
	private String priceM;
	private Integer priceL;
	private List<Integer> toppingId;
	public Integer toIntPriceM() {
		return Integer.parseInt(priceM);
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
		return "IntoCartForm [ itemId=" + itemId + ", quantity=" + quantity + ", size="
				+ size + ", priceM=" + priceM + ", priceL=" + priceL + ", toppingId=" + toppingId + "]";
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
	public String getPriceM() {
		return priceM;
	}
	public void setPriceM(String priceM) {
		this.priceM = priceM;
	}
	public Integer getPriceL() {
		return priceL;
	}
	public void setPriceL(Integer priceL) {
		this.priceL = priceL;
	}
	
}
