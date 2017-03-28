package InventorySystem;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class NavigationHandler {

	public String navigateToAdmin(){
		return "admin";
	}
}
