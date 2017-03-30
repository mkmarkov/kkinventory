package InventorySystem;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import KKDataTypes.ItemCategoryDataType;
import KKDataTypes.LogDataType;
import KKDataTypes.Logins;
import KKDataTypes.StockItemDataType;

public class DatabaseConnector {

	private final String GetStockJDBCCall = "{ call getStock (?,?)}"; // ItemCode,ItemVariation
	private final String AddStockJDBCCall = "{ call addStock (?,?,?,?,?,?,?)}"; // ItemCode,ItemVariation,Color,Stock,ImageName,ItemCatID,price
	private final String deleteStock = "{ call deleteStock (?) }"; // ItemID;
	private final String AddStockQuantityJDBCCall = "{ call addStockQuantity (?,?)}"; // ItemID,Quantity
	private final String RemoveStockQuantityJDBCCall = "{ call removeStockQuantity (?,?)}"; // ItemID,Quantity
	private final String GetUserHistoryJDBCCall = "{ call getUserHistory (?)}"; // Employee
	private final String AddUserHistoryJDBCCall = "{ call addUserHistory (?,?,?,?,?,?,?)}"; // Employee,ItemCode,ItemVaraition,Quantity,OrderDetails,Action,ItemID
	private final String ChangeCategory = "{ call ChangeCategory (?,?,?)}"; // ItemCatID,ItemCategory,ItemSubCategory
	private final String AddCategory = "{ call AddCategory (?,?)}"; // ItemCatID,ItemCategory,ItemSubCategory
	private final String DeleteCategory = "{ call DeleteCategory (?)}"; // ItemCatID,ItemCategory,ItemSubCategory
	private final String GetCategories = "{ call GetCategories }";
	private final String markHistoryAsError = "{ call MarkUserHistoryAsError (?)}";// LogID
	private final String ReturnStock = "{ call ReturnStock (?,?)}";// ItemID,Quantity
	private final String getUserHistory_error = "{ call getUserHistory_error }";
	private final String approveMarkedError = "{ call approveMarkedError (?) }";
	private final String addLogin = "{ call addLogin (?,?) }";
	private final String removeLogin = "{ call removeLogin (?) }";
	private final String getLogins_all = "{ call getLogins_all }";
	Connection conn;

	public void ConnectToDB() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=kkDatabase", "sa",
					"3e235fa9");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Logins> getLogins_all() {
		ConnectToDB();
		List<Logins> temp = new ArrayList<>();
		try {
			CallableStatement call = conn.prepareCall(getLogins_all);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				temp.add(new Logins(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Password")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	public Boolean addLogin(String Username, String Password) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(addLogin);
			call.setString(1, Username);
			call.setString(2, Password);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public Boolean removeLogin(int UserID) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(removeLogin);
			call.setInt(1, UserID);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public Boolean approveMarkedError(int LogID) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(approveMarkedError);
			call.setInt(1, LogID);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public Boolean MarkHistoryAsError(int LogID) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(markHistoryAsError);
			call.setInt(1, LogID);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public Boolean ReturnStock(int ItemID, int Quantity) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(ReturnStock);
			call.setInt(1, ItemID);
			call.setInt(2, Quantity);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Boolean deleteStock(int ItemID) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(deleteStock);
			call.setInt(1, ItemID);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Boolean deleteCategory(int ItemCatID) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(DeleteCategory);
			call.setInt(1, ItemCatID);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Boolean AddCategory(String ItemCategory, String ItemSubcategory) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(AddCategory);
			call.setString(1, ItemCategory);
			call.setString(2, ItemSubcategory);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Boolean changeCategory(int ItemCatID, String ItemCategory, String ItemSubcategory) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(ChangeCategory);
			call.setInt(1, ItemCatID);
			call.setString(2, ItemCategory);
			call.setString(3, ItemSubcategory);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean addUserHistory(String Employee, String ItemCode, String ItemVariation, int Quantity,
			String OrderDetails, String Action, int ItemID) {
		ConnectToDB();
		CallableStatement call;
		try {
			call = conn.prepareCall(AddUserHistoryJDBCCall);
			call.setString(1, Employee);
			call.setString(2, ItemCode);
			call.setString(3, ItemVariation);
			call.setInt(4, Quantity);
			call.setString(5, OrderDetails);
			call.setString(6, Action);
			call.setInt(7, ItemID);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public List<LogDataType> getUserHistory(String Employee) {
		ConnectToDB();
		List<LogDataType> list = new ArrayList<>();
		CallableStatement call;
		try {
			call = conn.prepareCall(GetUserHistoryJDBCCall);
			call.setString(1, Employee);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				LogDataType temp = new LogDataType(rs.getString("Employee"), rs.getInt("LogID"),
						rs.getString("ItemCode"), rs.getString("ItemVariation"), rs.getString("Action"),
						rs.getString("OrderDetails"), rs.getDate("Timestamp"), rs.getInt("Quantity"),
						rs.getInt("MarkedAsError"), rs.getInt("ItemID"));
				list.add(temp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<LogDataType> getUserHistory_error() {
		ConnectToDB();
		List<LogDataType> list = new ArrayList<>();
		CallableStatement call;
		try {
			call = conn.prepareCall(getUserHistory_error);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				LogDataType temp = new LogDataType(rs.getString("Employee"), rs.getInt("LogID"),
						rs.getString("ItemCode"), rs.getString("ItemVariation"), rs.getString("Action"),
						rs.getString("OrderDetails"), rs.getDate("Timestamp"), rs.getInt("Quantity"),
						rs.getInt("MarkedAsError"), rs.getInt("ItemID"));
				list.add(temp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public boolean RemoveStockQuantity(int ItemID, int Quantity) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(RemoveStockQuantityJDBCCall);
			call.setInt(1, ItemID);
			call.setInt(2, Quantity);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Boolean AddStockQuantity(int ItemID, int Quantity) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(AddStockQuantityJDBCCall);
			call.setInt(1, ItemID);
			call.setInt(2, Quantity);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Boolean AddStock(String ItemCode, String ItemVariation, String Color, int Stock, String ImageName,
			double price, String ItemCategory) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(AddStockJDBCCall);
			call.setString(1, ItemCode);
			call.setString(2, ItemVariation);
			call.setString(3, Color);
			call.setInt(4, Stock);
			call.setString(5, ImageName);
			call.setFloat(6, (float) price);
			call.setString(7, ItemCategory);
			call.executeUpdate();
			if (call.getUpdateCount() >= 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public List<ItemCategoryDataType> getCategories() {
		ConnectToDB();
		List<ItemCategoryDataType> list = new ArrayList<>();
		try {
			CallableStatement call = conn.prepareCall(GetCategories);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				ItemCategoryDataType temp = new ItemCategoryDataType();
				temp.setItemCategory(rs.getString("ItemCategory"));
				temp.setItemCatID(rs.getInt("ItemCatID"));
				temp.setItemSubcategory(rs.getString("ItemSubcat"));
				list.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<StockItemDataType> SearchStock(String ItemCode, String ItemVariation) {
		ConnectToDB();
		List<StockItemDataType> list = new ArrayList<>();
		try {
			CallableStatement call = conn.prepareCall(GetStockJDBCCall);
			call.setString(1, ItemCode);
			call.setString(2, ItemVariation);
			ResultSet rs = call.executeQuery();

			while (rs.next()) {
				StockItemDataType temp = new StockItemDataType(rs.getString("ItemCode"), rs.getString("ItemVariation"),
						rs.getString("Color"), rs.getInt("Stock"), rs.getFloat("Price"), rs.getInt("ItemID"),
						rs.getString("ItemCatеgory"), rs.getString("ImageName"));
				list.add(temp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
