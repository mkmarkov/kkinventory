package KKDataTypes;

public class ItemCategoryDataType {
	int ItemCatID;
	String ItemCategory;
	String ItemSubcategory;

	public ItemCategoryDataType() {
	}

	public ItemCategoryDataType(int itemCatID, String itemCategory, String ItemSubcategory) {
		ItemCatID = itemCatID;
		ItemCategory = itemCategory;
		this.ItemSubcategory = ItemSubcategory;
	}

	public int getItemCatID() {
		return ItemCatID;
	}

	public void setItemCatID(int itemCatID) {
		ItemCatID = itemCatID;
	}

	public String getItemCategory() {
		return ItemCategory;
	}

	public void setItemCategory(String itemCategory) {
		ItemCategory = itemCategory;
	}

	public String getItemSubcategory() {
		return ItemSubcategory;
	}

	public void setItemSubcategory(String ItemSubcategory) {
		this.ItemSubcategory = ItemSubcategory;
	}
	@Override
	public String toString() {
		return ItemCategory;
	}
}
