package administrator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import InventorySystem.DatabaseConnector;
import KKDataTypes.LogDataType;

@ManagedBean
@SessionScoped
public class reportsBean {

	DatabaseConnector dbconn = new DatabaseConnector();
	List<LogDataType> userReport = new ArrayList<>();
	FacesContext context = FacesContext.getCurrentInstance();
	public Date dateFrom;
	public Date dateTo;
	public String reportSearchType;
	public int total = 0;

	public void getReport(String search) {
		java.sql.Date dTo = null;
		java.sql.Date dfrom = null;
		if (dateFrom != null)
			dfrom = new java.sql.Date(dateFrom.getTime());
		if (dateTo != null)
			dTo = new java.sql.Date(dateTo.getTime());
		if (reportSearchType.equals("usr"))
			userReport = dbconn.getUserHistory(search, dfrom, dTo);
		if (reportSearchType.equals("cat"))
			userReport = dbconn.getHistoryByCategory(search, dfrom, dTo);
		if (reportSearchType.equals("item"))
			userReport = dbconn.getHistoryByItem(search, dfrom, dTo);
		if (reportSearchType.equals("order"))
			userReport = dbconn.getHistoryByOrder(search, dfrom, dTo);
		if (userReport.size() > 0)
			calculateTotal();
	}

	public void clearReport() {
		userReport.clear();
		total = 0;
	}

	public void calculateTotal() {
		total = 0;
		Iterator<LogDataType> itr = userReport.iterator();
		while (itr.hasNext()) {
			total += itr.next().Quantity;
		}
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public DatabaseConnector getDbconn() {
		return dbconn;
	}

	public void setDbconn(DatabaseConnector dbconn) {
		this.dbconn = dbconn;
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
