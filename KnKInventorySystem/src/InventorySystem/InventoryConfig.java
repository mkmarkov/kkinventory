package InventorySystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@ApplicationScoped
public class InventoryConfig {
	static Properties prop = new Properties();
	static InputStream input = null;

	public static Properties getProp() {
		return prop;
	}

	public static void setProp(Properties prop) {
		InventoryConfig.prop = prop;
	}

	public static void init() {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			prop.load(ec.getResourceAsStream("/WEB-INF/properties.cfg"));
			System.out.println(prop.getProperty("dbUser"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
