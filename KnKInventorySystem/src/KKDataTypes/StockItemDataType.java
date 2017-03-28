package KKDataTypes;

public class StockItemDataType {

	public String ItemCode;
	public String ItemVariation;
	public String Color;
	public int Stock;
	public double price;
	public int ItemID;
	public String ItemCategory;
	public String ImageName;
	
	public StockItemDataType(String itemCode, String itemVariation, String color, int stock, double price, int itemID,
			String ItemCategory, String imageName) {
		ItemCode = itemCode;
		ItemVariation = itemVariation;
		Color = color;
		Stock = stock;
		this.price = price;
		ItemID = itemID;
		this.ItemCategory = ItemCategory;
		ImageName = imageName;
	}

	public StockItemDataType() {

	}

	public String getItemCode() {
		return ItemCode;
	}

	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}

	public String getItemVariation() {
		return ItemVariation;
	}

	public void setItemVariation(String itemVariation) {
		ItemVariation = itemVariation;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public int getStock() {
		return Stock;
	}

	public void setStock(int stock) {
		Stock = stock;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public String getImageName() {
		return ImageName;
	}

	public void setImageName(String imageName) {
		ImageName = imageName;
	}

	public String getItemCategory() {
		return ItemCategory;
	}

	public void setItemCategory(String itemCategory) {
		ItemCategory = itemCategory;
	}
	

}
