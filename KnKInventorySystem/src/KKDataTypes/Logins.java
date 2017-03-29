package KKDataTypes;

public class Logins {
	int UserID;
	String Username;
	String Password;

	public Logins(int userID, String username, String password) {
		UserID = userID;
		Username = username;
		Password = password;
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

	@Override
	public String toString() {
		return Username;
	}
}
