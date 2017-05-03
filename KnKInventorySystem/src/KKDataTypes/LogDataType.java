package KKDataTypes;

import java.util.Date;

public class LogDataType {

	public String Employee;
	public int LogID;
	public String ItemCode;
	public String ItemVariation;
	public String Action;
	public String OrderDetails;
	public Date timestamp;
	public int Quantity;
	public int MarkedAsError;
	public int ItemID;
	public String ItemCategory;
	
	public LogDataType(String employee, int logID, String itemCode, String itemVariation,String ItemCategory, String action,
			String orderDetails, Date timestamp, int quantity, int markedAsError, int ItemID) {
		super();
		Employee = employee;
		LogID = logID;
		ItemCode = itemCode;
		ItemVariation = itemVariation;
		Action = action;
		OrderDetails = orderDetails;
		this.timestamp = timestamp;
		Quantity = quantity;
		MarkedAsError = markedAsError;
		this.ItemID = ItemID;
		this.ItemCategory = ItemCategory;
	}

	public LogDataType() {
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public String getItemCategory() {
		return ItemCategory;
	}

	public void setItemCategory(String itemCategory) {
		ItemCategory = itemCategory;
	}

	public String getEmployee() {
		return Employee;
	}

	public void setEmployee(String employee) {
		Employee = employee;
	}

	public int getLogID() {
		return LogID;
	}

	public void setLogID(int logID) {
		LogID = logID;
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

	public String getAction() {
		return Action;
	}

	public void setAction(String action) {
		Action = action;
	}

	public String getOrderDetails() {
		return OrderDetails;
	}

	public void setOrderDetails(String orderDetails) {
		OrderDetails = orderDetails;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int quantity) {
		Quantity = quantity;
	}

	public int getMarkedAsError() {
		return MarkedAsError;
	}

	public void setMarkedAsError(int markedAsError) {
		MarkedAsError = markedAsError;
	}

}
