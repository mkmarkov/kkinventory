package KKDataTypes;

public class Logins {
	int UserID;
	String Username;
	String Password;
	int AdminPanelEnabled;
	public Logins(int userID, String username, String password,int AdminPanelEnabled) {
		UserID = userID;
		Username = username;
		Password = password;
		this.AdminPanelEnabled = AdminPanelEnabled;
	}

	public Logins() {
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public int getAdminPanelEnabled() {
		return AdminPanelEnabled;
	}

	public void setAdminPanelEnabled(int adminPanelEnabled) {
		AdminPanelEnabled = adminPanelEnabled;
	}

	@Override
	public String toString() {
		return Username;
	}
}
