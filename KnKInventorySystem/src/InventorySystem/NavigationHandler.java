package InventorySystem;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean
public class NavigationHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void navigateToInventory() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/KnKInventorySystem/faces/Inventory/inv.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void navigateToUserManagement() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/KnKInventorySystem/faces/admin/userManagement.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void navigateToReports() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/KnKInventorySystem/faces/admin/Reports.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void navigateToItemManagement() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/KnKInventorySystem/faces/admin/ItemManagement.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String redirect(String pag) {
		if (pag.equals("inv")) {
			System.out.println(pag);
			return "inv";
		}
		if (pag.equals("reports")) {
			System.out.println(pag);
			return "reports";
		}
		return null;
	}

}
