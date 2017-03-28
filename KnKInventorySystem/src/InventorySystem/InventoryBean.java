package InventorySystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import KKDataTypes.ItemCategoryDataType;
import KKDataTypes.ItemDataType;
import KKDataTypes.LogDataType;
import KKDataTypes.StockItemDataType;

@ManagedBean
@SessionScoped
public class InventoryBean {

	TreeNode root = new DefaultTreeNode();
	List<ItemCategoryDataType> categories = new ArrayList<>();
	List<StockItemDataType> stockList = new ArrayList<>();
	List<LogDataType> historyList = new ArrayList<>();
	DatabaseConnector dbconn = new DatabaseConnector();
	ItemDataType selectedItem = new ItemDataType();
	TreeNode selectedNode = new DefaultTreeNode();
	LogDataType selectedHistory = new LogDataType();

	public void markHistoryAsError() {
		if (dbconn.MarkHistoryAsError(selectedHistory.LogID)) {
			Iterator<LogDataType> itr = historyList.iterator();
			while (itr.hasNext()) {
				if (itr.next().LogID == selectedHistory.LogID) {
					itr.remove();
					return;
				}
			}
		}
	}

	public void removeStock(String Employee, int Quantity, String OrderDetails) {
		if (Quantity <= 0)
			return;
		if (dbconn.RemoveStockQuantity(selectedItem.getItem().ItemID, Quantity))
			;
		{
			selectedItem.getItem().Stock -= Quantity;
			dbconn.addUserHistory(Employee, selectedItem.getItem().ItemCode, selectedItem.getItem().ItemVariation,
					Quantity, OrderDetails, "Remove",selectedItem.getItem().ItemID);
		}
		historyList = dbconn.getUserHistory(Employee);
	}

	public void search(String ItemCode, String ItemVariation) {
		System.out.println("Search for : " + ItemCode + " , " + ItemVariation);
		stockList.clear();
		categories.clear();
		root.getChildren().clear();
		stockList = dbconn.SearchStock(ItemCode, ItemVariation);
		categories = dbconn.getCategories();
		loadTree();
	}

	public void loadHistory(String Employee) {
		historyList = dbconn.getUserHistory(Employee);
	}

	public void setselected() {
		selectedItem = (ItemDataType) selectedNode.getData();
	}

	public void init(String login) {
		stockList.clear();
		categories.clear();
		root.getChildren().clear();
		stockList = dbconn.SearchStock("", "");
		categories = dbconn.getCategories();
		historyList = dbconn.getUserHistory(login);
		loadTree();
	}

	public void loadTree() {
		Iterator<ItemCategoryDataType> catItr = categories.iterator();
		while (catItr.hasNext()) {
			ItemCategoryDataType temp = catItr.next();
			root.getChildren().add(new DefaultTreeNode(
					new ItemDataType(temp.getItemCatID(), temp.getItemCategory(), temp.getItemSubcategory())));
		}
		Iterator<StockItemDataType> itr = stockList.iterator();
		while (itr.hasNext()) {
			StockItemDataType temp = itr.next();
			Iterator<TreeNode> rootitr = root.getChildren().iterator();
			while (rootitr.hasNext()) {
				TreeNode currNode = rootitr.next();
				ItemDataType currData = (ItemDataType) currNode.getData();
				if (currData.getCat().getItemCategory().equals(temp.getItemCategory())) {
					currNode.getChildren().add(new DefaultTreeNode(new ItemDataType(temp.ItemID, temp.ItemCategory,
							temp.ItemCode, temp.ItemVariation, temp.Color, temp.price, temp.Stock, temp.ImageName)));
				}
			}
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	public TreeNode getRoot() {
		return root;
	}

	public List<LogDataType> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<LogDataType> historyList) {
		this.historyList = historyList;
	}

	public LogDataType getSelectedHistory() {
		return selectedHistory;
	}

	public void setSelectedHistory(LogDataType selectedHistory) {
		this.selectedHistory = selectedHistory;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public void addStock(String ItemCode, String ItemVariation, String Color, int Stock, String ImageName,
			int ItemCatID, double price) {
		dbconn.AddStock(ItemCode, ItemVariation, Color, Stock, ImageName, ItemCatID, price);
	}

	public void removeStockQty(int ItemID, int Quantity) {
		dbconn.RemoveStockQuantity(ItemID, Quantity);
	}

	public void addCategory(String Cat, String Subcat) {
		dbconn.AddCategory(Cat, Subcat);

	}

	public void deleteCategory(int catid) {
		dbconn.deleteCategory(catid);
	}

	public void changecat(int catid, String cat, String subcat) {
		dbconn.changeCategory(catid, cat, subcat);

	}

	public void getUserHistory(String Employee) {
		dbconn.getUserHistory(Employee);
	}

	public void AddUserHistory(String Employee, String ItemCode, String ItemVariation, int Quantity,
			String OrderDetails, String Action,int ItemID) {
		dbconn.addUserHistory(Employee, ItemCode, ItemVariation, Quantity, OrderDetails, Action,ItemID);
	}

	public void deleteStock(int ItemID) {
		dbconn.deleteStock(ItemID);
	}

	public ItemDataType getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ItemDataType selectedItem) {
		if (selectedItem == null)
			return;
		this.selectedItem = selectedItem;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public List<StockItemDataType> getStockList() {
		return stockList;
	}

	public void setStockList(List<StockItemDataType> stockList) {
		this.stockList = stockList;
	}

}
