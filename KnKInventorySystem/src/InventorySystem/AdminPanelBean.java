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
import KKDataTypes.Logins;
import KKDataTypes.StockItemDataType;

@ManagedBean
@SessionScoped
public class AdminPanelBean {
	TreeNode selectedNode = new DefaultTreeNode();
	TreeNode root = new DefaultTreeNode();
	List<LogDataType> historyList = new ArrayList<>();
	List<Logins> loginsList = new ArrayList<>();
	Logins selectedLogin = new Logins();
	LogDataType selectedHistory = new LogDataType();
	DatabaseConnector dbconn = new DatabaseConnector();
	List<ItemCategoryDataType> categories = new ArrayList<>();
	List<StockItemDataType> stockList = new ArrayList<>();
	StockItemDataType selectedStock = new StockItemDataType();
	ItemCategoryDataType selectedCategory = new ItemCategoryDataType();
	ItemDataType selectedItem = new ItemDataType();

	public void init() {
		historyList = dbconn.getUserHistory_error();
		loginsList = dbconn.getLogins_all();
		stockList.clear();
		categories.clear();
		root.getChildren().clear();
		stockList = dbconn.SearchStock("", "");
		categories = dbconn.getCategories();
		loadTree();
	}

	public void approveMarkedError() {
		if (dbconn.ReturnStock(selectedHistory.ItemID, selectedHistory.Quantity))
			dbconn.approveMarkedError(selectedHistory.LogID);
	}

	public void addUser(String Username, String Password) {
		dbconn.addLogin(Username, Password);
	}

	public void removeUser() {
		dbconn.removeLogin(selectedLogin.getUserID());
	}

	public void addStockQuantity(int Quantity) {
		dbconn.AddStockQuantity(selectedStock.ItemCode, Quantity);
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

	public void setselected() {
		selectedItem = (ItemDataType) selectedNode.getData();
	}

	///////////////////////////////////////////////////

	public ItemDataType getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ItemDataType selectedItem) {
		this.selectedItem = selectedItem;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public List<LogDataType> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<LogDataType> historyList) {
		this.historyList = historyList;
	}

	public List<Logins> getLoginsList() {
		return loginsList;
	}

	public void setLoginsList(List<Logins> loginsList) {
		this.loginsList = loginsList;
	}

	public Logins getSelectedLogin() {
		return selectedLogin;
	}

	public void setSelectedLogin(Logins selectedLogin) {
		this.selectedLogin = selectedLogin;
	}

	public LogDataType getSelectedHistory() {
		return selectedHistory;
	}

	public void setSelectedHistory(LogDataType selectedHistory) {
		this.selectedHistory = selectedHistory;
	}

	public List<StockItemDataType> getStockList() {
		return stockList;
	}

	public void setStockList(List<StockItemDataType> stockList) {
		this.stockList = stockList;
	}

	public StockItemDataType getSelectedStock() {
		return selectedStock;
	}

	public void setSelectedStock(StockItemDataType selectedStock) {
		this.selectedStock = selectedStock;
	}

	public ItemCategoryDataType getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(ItemCategoryDataType selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

}
