package KKDataTypes;

public class ItemDataType {
	ItemCategoryDataType cat = new ItemCategoryDataType();
	StockItemDataType item = new StockItemDataType();

	public ItemDataType() {
		item.ItemCategory="";
		item.ItemCode="";
	}

	public ItemDataType(int catid, String cat, String subcat) {
		this.cat.setItemCatID(catid);
		this.cat.ItemCategory = cat;
		this.cat.ItemSubcategory = subcat;
	}

	public ItemDataType(int ItemID, String ItemCategory, String ItemCode, String ItemVariation, String Color,
			double price, int stock, String ImageName) {
		item.setImageName(ImageName);
		item.setItemCategory(ItemCategory);
		item.setItemCode(ItemCode);
		item.setItemID(ItemID);
		item.setColor(Color);
		item.setItemVariation(ItemVariation);
		item.setPrice(price);
		item.setStock(stock);
	}

	public ItemCategoryDataType getCat() {
		return cat;
	}

	public void setCat(ItemCategoryDataType cat) {
		this.cat = cat;
	}

	public StockItemDataType getItem() {
		return item;
	}

	public void setItem(StockItemDataType item) {
		this.item = item;
	}

}
