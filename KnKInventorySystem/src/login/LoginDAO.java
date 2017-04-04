package login;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginDAO {
	private final static String getLogins = "{call getLogins (?,?) }";
	private final static String getRights = "{ call getRights (?) }";

	public static boolean validate(String user, String password) {
		Connection con = null;
		CallableStatement ps = null;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareCall(getLogins);
			ps.setString(1, user);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				//result found, means valid inputs
				return true;
			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return false;
		} finally {
			DataConnect.close(con);
		}
		return false;
	}
	
	public static boolean isAdminPanelEnabled(String Employee) {
		Connection con = null;
		try {
			con = DataConnect.getConnection();
			CallableStatement call = con.prepareCall(getRights);
			call.setString(1, Employee);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) == 1)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

}