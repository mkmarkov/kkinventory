package KKDataTypes;

public class Stock {
	StockItemDataType item;
	ItemCategoryDataType category;
	
	public Stock(StockItemDataType item, ItemCategoryDataType category) {
		this.item = item;
		this.category = category;
	}
	public Stock() {
	}
	
	public StockItemDataType getItem() {
		return item;
	}
	public void setItem(StockItemDataType item) {
		this.item = item;
	}
	public ItemCategoryDataType getCategory() {
		return category;
	}
	public void setCategory(ItemCategoryDataType category) {
		this.category = category;
	}
	
	
}
