package InventorySystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import KKDataTypes.ItemCategoryDataType;
import KKDataTypes.ItemDataType;
import KKDataTypes.LogDataType;
import KKDataTypes.StockItemDataType;
import login.SessionUtils;

@ManagedBean
@SessionScoped
public class InventoryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	String search_ItemCode, search_ItemVariation, search_Color;
	int search_qty;

	public void rowindex (int indedx)
	{
		System.out.println(indedx);
	}
	public void init() {
		 if (stockList.isEmpty()) {
		stockList.clear();
		categories.clear();
		root.getChildren().clear();
		stockList = dbconn.SearchStock("", "", 0);
		categories = dbconn.getCategories();
		historyList = dbconn.getUserHistory(SessionUtils.getUserName(), null, null);
		adminPanelEnabled = dbconn.isAdminPanelEnabled(SessionUtils.getUserName());
		loadTree();
		}
	}

	public void expandRow() {
		if (selectedItem.getItem().getItemCode() == null)
			if (selectedNode.isExpanded() == false) {
				selectedNode.setExpanded(true);
			} else {
				selectedNode.setExpanded(false);
			}
	}

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

	public void removeStock(int Quantity, String OrderDetails) {
		if (Quantity > selectedItem.getItem().getStock()) {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Недостатъчна наличност", ""));
			return;
		}
		if (dbconn.RemoveStockQuantity(selectedItem.getItem().ItemID, Quantity)) {
			selectedItem.getItem().Stock -= Quantity;
			dbconn.addUserHistory(SessionUtils.getUserName(), selectedItem.getItem().ItemCode,
					selectedItem.getItem().ItemVariation, selectedItem.getItem().ItemCategory, Quantity, OrderDetails,
					"Remove", selectedItem.getItem().ItemID);
			ItemDataType parent = (ItemDataType) selectedNode.getParent().getData();
			parent.getItem().Stock -= Quantity;
		}
		historyList = dbconn.getUserHistory(SessionUtils.getUserName(), null, null);
	}

	public void search() {
		stockList.clear();
		categories.clear();
		root.getChildren().clear();
		stockList = dbconn.SearchStock(search_ItemCode, search_ItemVariation, search_qty);
		categories = dbconn.getCategories();
		loadTree();
		RequestContext.getCurrentInstance().update("form:stocktable");
	}

	public void loadHistory() {
		historyList = dbconn.getUserHistory(SessionUtils.getUserName(), null, null);
	}

	public void setselected() {
		selectedItem = (ItemDataType) selectedNode.getData();
		expandRow();
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
		Iterator<LogDataType> itr_his = historyList.iterator();
		while (itr_his.hasNext()) {
			LogDataType temp = itr_his.next();
			String Action = temp.Action.trim();
			if (!Action.equals("Remove"))
				itr_his.remove();
		}
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
			String filepath = InventoryConfig.prop.getProperty("downloadPath")+ selectedItem.getItem().ImageName;
			try {
				return new DefaultStreamedContent(
						new FileInputStream(new File(filepath)));
			} catch (Exception e) {
				System.out.println("Error streaming image");
			}
		}
		return null;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////

	public TreeNode getRoot() {
		return root;
	}

	public int getSearch_qty() {
		return search_qty;
	}

	public void setSearch_qty(int search_qty) {
		this.search_qty = search_qty;
	}

	public String getSearch_ItemCode() {
		return search_ItemCode;
	}

	public void setSearch_ItemCode(String search_ItemCode) {
		this.search_ItemCode = search_ItemCode;
	}

	public String getSearch_ItemVariation() {
		return search_ItemVariation;
	}

	public void setSearch_ItemVariation(String search_ItemVariation) {
		this.search_ItemVariation = search_ItemVariation;
	}

	public String getSearch_Color() {
		return search_Color;
	}

	public void setSearch_Color(String search_Color) {
		this.search_Color = search_Color;
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
