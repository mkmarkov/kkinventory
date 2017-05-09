package administrator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import InventorySystem.DatabaseConnector;
import KKDataTypes.LogDataType;
import KKDataTypes.Logins;
import login.SessionUtils;

@ManagedBean
@SessionScoped
public class userManagementBean {

	List<LogDataType> historyList = new ArrayList<>();
	List<Logins> loginsList = new ArrayList<>();
	LogDataType selectedHistory = new LogDataType();
	DatabaseConnector dbconn = new DatabaseConnector();
	String SelectedLogin;
	FacesContext context = FacesContext.getCurrentInstance();
	boolean newuser_adminEnabled;
	public String reportSearchType;

	public void init() {
		if (!SessionUtils.getAdminPanelRights()) {
			SessionUtils.getSession().invalidate();
			return;
		}
		historyList = dbconn.getUserHistory_error();
		loginsList = dbconn.getLogins_all();
	}

	public void approveMarkedError() {
		if (dbconn.ReturnStock(selectedHistory.ItemID, selectedHistory.Quantity)) {
			dbconn.approveMarkedError(selectedHistory.LogID);
		} else {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Грешка при маркиране на грешка", ""));
		}
	}

	public void addUser(String Username, String Password) {
		int adminPanelEnabled;

		if (newuser_adminEnabled)
			adminPanelEnabled = 1;
		else
			adminPanelEnabled = 0;
		if (dbconn.addLogin(Username, Password, adminPanelEnabled))
			loginsList = dbconn.getLogins_all();
		else {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Грешка при добавяне на потребител", ""));
		}
	}

	public void removeUser() {
		Iterator<Logins> itr = loginsList.iterator();
		int userid = 0;
		while (itr.hasNext()) {
			Logins temp = itr.next();
			if (temp.getUsername().trim().equals(SelectedLogin))
				;
			userid = temp.getUserID();
		}
		if (dbconn.removeLogin(userid))
			loginsList = dbconn.getLogins_all();
		else {
			context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Грешка при изтриване на потребител", ""));
		}
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

	public String getSelectedLogin() {
		return SelectedLogin;
	}

	public void setSelectedLogin(String selectedLogin) {
		SelectedLogin = selectedLogin;
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public boolean isNewuser_adminEnabled() {
		return newuser_adminEnabled;
	}

	public void setNewuser_adminEnabled(boolean newuser_adminEnabled) {
		this.newuser_adminEnabled = newuser_adminEnabled;
	}

	public String getReportSearchType() {
		return reportSearchType;
	}

	public void setReportSearchType(String reportSearchType) {
		this.reportSearchType = reportSearchType;
	}

}
