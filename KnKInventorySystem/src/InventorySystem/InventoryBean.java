package InventorySystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
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
	boolean adminPanelEnabled = false;;
	private boolean expandTable = false;

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
			dbconn.addUserHistory(Employee, selectedItem.getItem().ItemCode, selectedItem.getItem().ItemVariation,selectedItem.getItem().ItemCategory,
					Quantity, OrderDetails, "Remove", selectedItem.getItem().ItemID);
			ItemDataType parent = (ItemDataType) selectedNode.getParent().getData();
			parent.getItem().Stock -= Quantity;
		}
		historyList = dbconn.getUserHistory(Employee,null,null);
	}

	public void search(String ItemCode, String ItemVariation,String Color, int qty) {
		stockList.clear();
		categories.clear();
		root.getChildren().clear();
		stockList = dbconn.SearchStock(ItemCode, ItemVariation,Color, qty);
		categories = dbconn.getCategories();
		expandTable = true;
		loadTree();
	}

	public void loadHistory(String Employee) {
		historyList = dbconn.getUserHistory(Employee,null,null);
	}

	public void setselected() {
		selectedItem = (ItemDataType) selectedNode.getData();
		collapsingORexpanding(selectedNode, true);
	}

	public void init(String login) {
		if (stockList.isEmpty()) {
			stockList.clear();
			categories.clear();
			root.getChildren().clear();
			stockList = dbconn.SearchStock("", "","", 0);
			categories = dbconn.getCategories();
			historyList = dbconn.getUserHistory(login,null,null);
			Iterator<LogDataType> itr = historyList.iterator();
			while (itr.hasNext()) {
				LogDataType temp = itr.next();
				String Action = temp.Action.trim();
				if (!Action.equals("Remove"))
					itr.remove();
			}
			adminPanelEnabled = dbconn.isAdminPanelEnabled(login);
			loadTree();
		}
	}

	public boolean isAdminPanelEnabled() {
		return adminPanelEnabled;
	}

	public void setAdminPanelEnabled(boolean adminPanelEnabled) {
		this.adminPanelEnabled = adminPanelEnabled;
	}

	public void loadTree() {

		root.getChildren().clear();
		Iterator<ItemCategoryDataType> catItr = categories.iterator();
		while (catItr.hasNext()) {
			ItemCategoryDataType temp = catItr.next();
			root.getChildren().add(new DefaultTreeNode(
					new ItemDataType(temp.getItemCatID(), temp.getItemCategory(), temp.getItemSubcategory())));
			if (expandTable)
				root.getChildren().get(root.getChildren().size() - 1).setExpanded(true);
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
		calculateTotalStock();
		expandTable = false;
	}

	public void calculateTotalStock() {
		Iterator<TreeNode> itr = root.getChildren().iterator();
		while (itr.hasNext()) {
			int sum = 0;
			TreeNode currNode = itr.next();
			ItemDataType currCat = (ItemDataType) currNode.getData();
			Iterator<TreeNode> catItr = currNode.getChildren().iterator();
			while (catItr.hasNext()) {
				ItemDataType currItem = (ItemDataType) catItr.next().getData();
				sum += currItem.getItem().Stock;
			}
			currCat.getItem().setStock(sum);
		}
	}

	public StreamedContent getImage() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			String filepath = InventoryConfig.prop.getProperty("downloadPath");
			try{
			return new DefaultStreamedContent(
					new FileInputStream(new File(filepath, selectedItem.getItem().ImageName)));
			}
			catch(Exception e)
			{
				System.out.println("Error streaming image");
			}
		}
		return null;
	}
	
	public void collapsingORexpanding(TreeNode n, boolean option) {
	    if(n.getChildren().size() == 0) {
	        n.setSelected(false);
	    }
	    else {
	        for(TreeNode s: n.getChildren()) {
	            collapsingORexpanding(s, option);
	        }
	        n.setExpanded(option);
	        n.setSelected(false);
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
