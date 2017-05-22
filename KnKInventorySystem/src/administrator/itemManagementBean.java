package administrator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.mortennobel.imagescaling.ResampleOp;

import InventorySystem.DatabaseConnector;
import InventorySystem.InventoryConfig;
import KKDataTypes.ItemCategoryDataType;
import KKDataTypes.ItemDataType;
import KKDataTypes.LogDataType;
import KKDataTypes.Logins;
import KKDataTypes.StockItemDataType;
import login.SessionUtils;

@ManagedBean
@SessionScoped
public class itemManagementBean {

	TreeNode selectedNode = new DefaultTreeNode();
	TreeNode root = new DefaultTreeNode();
	List<LogDataType> historyList = new ArrayList<>();
	List<Logins> loginsList = new ArrayList<>();
	LogDataType selectedHistory = new LogDataType();
	DatabaseConnector dbconn = new DatabaseConnector();
	List<ItemCategoryDataType> categories = new ArrayList<>();
	List<StockItemDataType> stockList = new ArrayList<>();
	StockItemDataType selectedStock = new StockItemDataType();
	String selectedCategory;
	String SelectedLogin;
	ItemDataType selectedItem = new ItemDataType();
	List<LogDataType> userReport = new ArrayList<>();
	FacesContext context = FacesContext.getCurrentInstance();
	StockItemDataType newitem = new StockItemDataType();
	public boolean newuser_adminEnabled;
	private String newItem_imageName = "";
	public Date dateFrom;
	public Date dateTo;
	public String reportSearchType;

	public void init() {
		if (!SessionUtils.getAdminPanelRights()) {
			SessionUtils.getSession().invalidate();
			return;
		}
		if (stockList.isEmpty()) {
			historyList = dbconn.getUserHistory_error();
			loginsList = dbconn.getLogins_all();
			stockList.clear();
			categories.clear();
			root.getChildren().clear();
			stockList = dbconn.SearchStock("", "", 0);
			categories = dbconn.getCategories();
			loadTree();
		}
	}

	public void reload() {
		historyList = dbconn.getUserHistory_error();
		loginsList = dbconn.getLogins_all();
		stockList.clear();
		categories.clear();
		root.getChildren().clear();
		stockList = dbconn.SearchStock("", "", 0);
		categories = dbconn.getCategories();
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
		calculateTotalStock();
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

	public void upload(FileUploadEvent event) {
		try {
			UploadedFile uploadedFile = event.getFile();
			BufferedImage image = ImageIO.read(uploadedFile.getInputstream());
			boolean doResize = Boolean.valueOf(InventoryConfig.prop.getProperty("resizeImage"));
			if (doResize) {
				int height = Integer.valueOf(InventoryConfig.prop.getProperty("resizeHeight"));
				int width = Integer.valueOf(InventoryConfig.prop.getProperty("resizeWidth"));
				ResampleOp resample = new ResampleOp(height, width);
				image = resample.filter(image, null);
			}
			newItem_imageName = uploadedFile.getFileName();
			String downloadPath = InventoryConfig.prop.getProperty("downloadPath") + uploadedFile.getFileName();
			System.out.println(downloadPath);
			ImageIO.write(image, "jpg", new File(downloadPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setselected() {
		selectedItem = (ItemDataType) selectedNode.getData();
	}

	public void addStockQuantity(int Quantity, String Employee) {
		if (dbconn.AddStockQuantity(selectedItem.getItem().ItemID, Quantity))
			dbconn.addUserHistory(Employee, selectedItem.getItem().ItemCode, selectedItem.getItem().ItemVariation,
					selectedItem.getItem().ItemCategory, Quantity, "", "Add", selectedItem.getItem().ItemID);
		else {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Грешка при добавяне на наличност", ""));
		}
		reload();
	}

	public StreamedContent getImage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			return new DefaultStreamedContent(new FileInputStream(
					new File(InventoryConfig.prop.getProperty("downloadPath"), selectedItem.getItem().ImageName)));
		}
	}

	public void deleteStock(String Employee) {
		if (dbconn.deleteStock(selectedItem.getItem().ItemID))
			dbconn.addUserHistory(Employee, selectedItem.getItem().ItemCode, selectedItem.getItem().ItemVariation,
					selectedItem.getItem().ItemCategory, selectedItem.getItem().Stock, "", "DELETE",
					selectedItem.getItem().ItemID);
		else {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Грешка при изтриване на артикул", ""));
		}
		reload();
	}

	public void addStock() {
		if (newitem.ItemCode.isEmpty() || newitem.ItemCode.trim().length() == 0) {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Грешка при добавяне на артикул", ""));
			return;
		}

		if (dbconn.AddStock(newitem.ItemCode, newitem.ItemVariation, newitem.Color, newitem.Stock, newItem_imageName,
				newitem.price, newitem.ItemCategory))
			dbconn.addUserHistory(SessionUtils.getUserName(), newitem.ItemCode, newitem.ItemVariation,
					newitem.ItemCategory, newitem.Stock, "", "NEWITEM", 0);
		else {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Грешка при добавяне на артикул", ""));
		}
		newitem = new StockItemDataType();
		reload();
	}

	public void addCategory(String Category) {
		if (dbconn.AddCategory(Category, Category))
			categories = dbconn.getCategories();
		else {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Грешка при добавяне на категория", ""));
		}
	}

	public void deleteCategory() {
		Iterator<ItemCategoryDataType> itr = categories.iterator();
		int catid = 0;
		while (itr.hasNext()) {
			ItemCategoryDataType temp = itr.next();
			if (temp.getItemCategory().trim().equals(selectedCategory))
				catid = temp.getItemCatID();
		}
		if (dbconn.deleteCategory(catid)) {
			categories = dbconn.getCategories();
		} else {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Грешка при изтриване на категория", ""));
		}
	}

	/////////////////////// SETTERS/GETTERS/////////////
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

	public LogDataType getSelectedHistory() {
		return selectedHistory;
	}

	public void setSelectedHistory(LogDataType selectedHistory) {
		this.selectedHistory = selectedHistory;
	}

	public DatabaseConnector getDbconn() {
		return dbconn;
	}

	public void setDbconn(DatabaseConnector dbconn) {
		this.dbconn = dbconn;
	}

	public List<ItemCategoryDataType> getCategories() {
		return categories;
	}

	public void setCategories(List<ItemCategoryDataType> categories) {
		this.categories = categories;
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

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public String getSelectedLogin() {
		return SelectedLogin;
	}

	public void setSelectedLogin(String selectedLogin) {
		SelectedLogin = selectedLogin;
	}

	public ItemDataType getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ItemDataType selectedItem) {
		this.selectedItem = selectedItem;
	}

	public List<LogDataType> getUserReport() {
		return userReport;
	}

	public void setUserReport(List<LogDataType> userReport) {
		this.userReport = userReport;
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public StockItemDataType getNewitem() {
		return newitem;
	}

	public void setNewitem(StockItemDataType newitem) {
		this.newitem = newitem;
	}

	public boolean isNewuser_adminEnabled() {
		return newuser_adminEnabled;
	}

	public void setNewuser_adminEnabled(boolean newuser_adminEnabled) {
		this.newuser_adminEnabled = newuser_adminEnabled;
	}

	public String getNewItem_imageName() {
		return newItem_imageName;
	}

	public void setNewItem_imageName(String newItem_imageName) {
		this.newItem_imageName = newItem_imageName;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getReportSearchType() {
		return reportSearchType;
	}

	public void setReportSearchType(String reportSearchType) {
		this.reportSearchType = reportSearchType;
	}

}
