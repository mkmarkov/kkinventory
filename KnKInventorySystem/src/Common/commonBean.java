package Common;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class commonBean {

	public String itemCode;
	public String itemVariation;
	public String ItemColor;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemVariation() {
		return itemVariation;
	}

	public void setItemVariation(String itemVariation) {
		this.itemVariation = itemVariation;
	}

	public String getItemColor() {
		return ItemColor;
	}

	public void setItemColor(String itemColor) {
		ItemColor = itemColor;
	}

	
}
