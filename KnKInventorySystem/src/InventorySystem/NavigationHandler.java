package InventorySystem;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class NavigationHandler {

	public String navigateToAdmin() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/KnKInventorySystem/faces/admin.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "admin";
	}

	public String navigateToInventory() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/KnKInventorySystem/faces/InventoryPage.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "inventory";
	}
}
