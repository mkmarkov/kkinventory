package InventorySystem;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import KKDataTypes.LogDataType;
import KKDataTypes.Logins;

@ManagedBean
@SessionScoped
public class AdminPanelBean {
	List<LogDataType> historyList = new ArrayList<>();
	List<Logins> loginsList = new ArrayList<>();
	Logins selectedLogin = new Logins();
	LogDataType selectedHistory = new LogDataType();
	DatabaseConnector dbconn = new DatabaseConnector();

	public void init() {
		historyList = dbconn.getUserHistory_error();
		loginsList = dbconn.getLogins_all();
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
}
