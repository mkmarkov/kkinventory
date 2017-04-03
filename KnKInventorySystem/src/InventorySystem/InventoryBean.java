package InventorySystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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
	FacesContext context = FacesContext.getCurrentInstance();

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
		if (Quantity > selectedItem.getItem().getStock()) {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Недостатъчна наличност", ""));
			return;
		}
		if (dbconn.RemoveStockQuantity(selectedItem.getItem().ItemID, Quantity)) {
			selectedItem.getItem().Stock -= Quantity;
			dbconn.addUserHistory(Employee, selectedItem.getItem().ItemCode, selectedItem.getItem().ItemVariation,
					Quantity, OrderDetails, "Remove", selectedItem.getItem().ItemID);
		}
		historyList = dbconn.getUserHistory(Employee);
	}

	public void search(String ItemCode, String ItemVariation) {
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
		Iterator<LogDataType> itr = historyList.iterator();
		while (itr.hasNext()) {
			LogDataType temp = itr.next();
			String Action = temp.Action.trim();
			if (!Action.equals("Remove"))
				itr.remove();
		}
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
