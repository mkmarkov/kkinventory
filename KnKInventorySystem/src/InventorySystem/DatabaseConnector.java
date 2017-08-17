package InventorySystem;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
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

	private final String GetStockJDBCCall = "{ call getStock (?,?,?)}"; // ItemCode,ItemVariation,Qty
	private final String AddStockJDBCCall = "{ call addStock (?,?,?,?,?,?,?)}"; // ItemCode,ItemVariation,Color,Stock,ImageName,ItemCatID,price
	private final String deleteStock = "{ call deleteStock (?) }"; // ItemID;
	private final String AddStockQuantityJDBCCall = "{ call addStockQuantity (?,?)}"; // ItemID,Quantity
	private final String RemoveStockQuantityJDBCCall = "{ call removeStockQuantity (?,?)}"; // ItemID,Quantity
	private final String GetUserHistoryJDBCCall = "{ call getUserHistory (?,?,?)}"; // Employee,DateFrom,DateTo
	private final String GetHistoryByItem = "{ call getHistoryByItem (?,?,?)}"; // ItemID,DateFrom,DateTo
	private final String GetHistoryByCategory = "{ call getHistoryByCategory (?,?,?)}"; // Category,DateFrom,DateTo
	private final String GetHistoryByOrder = "{ call getHistoryByOrder (?,?,?)}"; // Order,DateFrom,DateTo
	private final String AddUserHistoryJDBCCall = "{ call addUserHistory (?,?,?,?,?,?,?,?)}"; // Employee,ItemCode,ItemVaraitionm,ItemCategory,Quantity,OrderDetails,Action,ItemID
	private final String ChangeCategory = "{ call ChangeCategory (?,?,?)}"; // ItemCatID,ItemCategory,ItemSubCategory
	private final String AddCategory = "{ call AddCategory (?,?,?)}"; // ItemCatID,ItemCategory,ItemSubCategory
	private final String DeleteCategory = "{ call DeleteCategory (?)}"; // ItemCatID,ItemCategory,ItemSubCategory
	private final String GetCategories = "{ call GetCategories }";
	private final String markHistoryAsError = "{ call MarkUserHistoryAsError (?)}";// LogID
	private final String ReturnStock = "{ call ReturnStock (?,?)}";// ItemID,Quantity
	private final String getUserHistory_error = "{ call getUserHistory_error }";
	private final String approveMarkedError = "{ call approveMarkedError (?) }";
	private final String addLogin = "{ call addLogin (?,?,?) }";
	private final String removeLogin = "{ call removeLogin (?) }";
	private final String getLogins_all = "{ call getLogins_all }";
	private final String getRights = "{ call getRights (?) }";
	private final String editItem = "{ call updateItem(?,?,?,?,?,?,?)}";
	private final String getAvgPrice="{ call getAvgPrice(?)}";
	Connection conn;

	public void ConnectToDB() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(InventoryConfig.prop.getProperty("dbConnectionString"),
					InventoryConfig.prop.getProperty("dbUser"), InventoryConfig.prop.getProperty("dbPassword"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isAdminPanelEnabled(String Employee) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(getRights);
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
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public double getAvgPrice(String cat) {
		ConnectToDB();
		ResultSet rs = null;
		double result;
		CallableStatement call=null;
		try {
			call = conn.prepareCall(getAvgPrice);
			call.setString(1,cat);
			rs = call.executeQuery();
			rs.next();
			result = rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				call.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return result;
	}

	public List<Logins> getLogins_all() {
		ConnectToDB();
		List<Logins> temp = new ArrayList<>();
		try {
			CallableStatement call = conn.prepareCall(getLogins_all);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				temp.add(new Logins(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Password"),
						rs.getInt("AdminPanelEnabled")));
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

	public Boolean editItem(int ItemID, String ItemCode, String ItemVariation, String ItemCategory, int Quantity,
			double price, String ImageName) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(editItem);
			call.setInt(1, ItemID);
			call.setString(2, ItemCode);
			call.setString(3, ItemVariation);
			call.setString(4, ItemCategory);
			call.setDouble(5, price);
			call.setDouble(6, Quantity);
			call.setString(7, ImageName);
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

	public Boolean addLogin(String Username, String Password, int AdminPanelEnabled) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(addLogin);
			call.setString(1, Username);
			call.setString(2, Password);
			call.setInt(3, AdminPanelEnabled);
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

	public Boolean AddCategory(String ItemCategory, String ItemSubcategory,int showOrder) {
		ConnectToDB();
		try {
			CallableStatement call = conn.prepareCall(AddCategory);
			call.setString(1, ItemCategory);
			call.setString(2, ItemSubcategory);
			call.setInt(3, showOrder);
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

	public boolean addUserHistory(String Employee, String ItemCode, String ItemVariation, String ItemCategory,
			int Quantity, String OrderDetails, String Action, int ItemID) {
		ConnectToDB();
		CallableStatement call;
		try {
			call = conn.prepareCall(AddUserHistoryJDBCCall);
			call.setString(1, Employee);
			call.setString(2, ItemCode);
			call.setString(3, ItemVariation);
			call.setString(4, ItemCategory);
			call.setInt(5, Quantity);
			call.setString(6, OrderDetails);
			call.setString(7, Action);
			call.setInt(8, ItemID);
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

	public List<LogDataType> getUserHistory(String Employee, Date DateFrom, Date DateTo) {
		ConnectToDB();
		List<LogDataType> list = new ArrayList<>();
		CallableStatement call;
		try {
			call = conn.prepareCall(GetUserHistoryJDBCCall);
			call.setString(1, Employee);
			call.setDate(2, DateFrom);
			call.setDate(3, DateTo);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				LogDataType temp = new LogDataType(rs.getString("Employee"), rs.getInt("LogID"),
						rs.getString("ItemCode"), rs.getString("ItemVariation"), rs.getString("ItemCategory"),
						rs.getString("Action"), rs.getString("OrderDetails"), rs.getDate("Timestamp"),
						rs.getInt("Quantity"), rs.getInt("MarkedAsError"), rs.getInt("ItemID"));
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

	public List<LogDataType> getHistoryByItem(String ItemCode, Date DateFrom, Date DateTo) {
		ConnectToDB();
		List<LogDataType> list = new ArrayList<>();
		CallableStatement call;
		try {
			call = conn.prepareCall(GetHistoryByItem);
			call.setString(1, ItemCode);
			call.setDate(2, DateFrom);
			call.setDate(3, DateTo);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				LogDataType temp = new LogDataType(rs.getString("Employee"), rs.getInt("LogID"),
						rs.getString("ItemCode"), rs.getString("ItemVariation"), rs.getString("ItemCategory"),
						rs.getString("Action"), rs.getString("OrderDetails"), rs.getDate("Timestamp"),
						rs.getInt("Quantity"), rs.getInt("MarkedAsError"), rs.getInt("ItemID"));
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

	public List<LogDataType> getHistoryByOrder(String Order, Date DateFrom, Date DateTo) {
		ConnectToDB();
		List<LogDataType> list = new ArrayList<>();
		CallableStatement call;
		try {
			call = conn.prepareCall(GetHistoryByOrder);
			call.setString(1, Order);
			call.setDate(2, DateFrom);
			call.setDate(3, DateTo);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				LogDataType temp = new LogDataType(rs.getString("Employee"), rs.getInt("LogID"),
						rs.getString("ItemCode"), rs.getString("ItemVariation"), rs.getString("ItemCategory"),
						rs.getString("Action"), rs.getString("OrderDetails"), rs.getDate("Timestamp"),
						rs.getInt("Quantity"), rs.getInt("MarkedAsError"), rs.getInt("ItemID"));
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

	public List<LogDataType> getHistoryByCategory(String Category, Date DateFrom, Date DateTo) {
		ConnectToDB();
		List<LogDataType> list = new ArrayList<>();
		CallableStatement call;
		try {
			call = conn.prepareCall(GetHistoryByCategory);
			call.setString(1, Category);
			call.setDate(2, DateFrom);
			call.setDate(3, DateTo);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				LogDataType temp = new LogDataType(rs.getString("Employee"), rs.getInt("LogID"),
						rs.getString("ItemCode"), rs.getString("ItemVariation"), rs.getString("ItemCategory"),
						rs.getString("Action"), rs.getString("OrderDetails"), rs.getDate("Timestamp"),
						rs.getInt("Quantity"), rs.getInt("MarkedAsError"), rs.getInt("ItemID"));
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

	public List<LogDataType> getUserHistory_error() {
		ConnectToDB();
		List<LogDataType> list = new ArrayList<>();
		CallableStatement call;
		try {
			call = conn.prepareCall(getUserHistory_error);
			ResultSet rs = call.executeQuery();
			while (rs.next()) {
				LogDataType temp = new LogDataType(rs.getString("Employee"), rs.getInt("LogID"),
						rs.getString("ItemCode"), rs.getString("ItemVariation"), rs.getString("ItemCategory"),
						rs.getString("Action"), rs.getString("OrderDetails"), rs.getDate("Timestamp"),
						rs.getInt("Quantity"), rs.getInt("MarkedAsError"), rs.getInt("ItemID"));
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
			call.setDouble(6, price);
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

	public List<StockItemDataType> SearchStock(String ItemCode, String ItemVariation, int qty) {
		ConnectToDB();
		List<StockItemDataType> list = new ArrayList<>();
		try {
			CallableStatement call = conn.prepareCall(GetStockJDBCCall);
			call.setString(1, ItemCode);
			call.setString(2, ItemVariation);
			call.setInt(3, qty);
			ResultSet rs = call.executeQuery();

			while (rs.next()) {
				StockItemDataType temp = new StockItemDataType(rs.getString("ItemCode"), rs.getString("ItemVariation"),
						rs.getString("Color"), rs.getInt("Stock"), rs.getDouble("Price"), rs.getInt("ItemID"),
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
