package com.vaadin.appl.gui.page;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Kastalski Sergey
 */
@SpringUI(path = LoginPage.PATH)
@Theme("mytheme")
@SpringViewDisplay
public class LoginPage extends UI implements ViewDisplay {
	
	public static final String PATH = "/loginpage";
	
	private static final long serialVersionUID = 405550262179685561L;
	
	private VerticalLayout dataLayout;
	
	
	@Override
	protected void init(final VaadinRequest request) {
		dataLayout = new VerticalLayout();
		dataLayout.setSizeFull();
		
		setContent(dataLayout);
	}
	
	@Override
	public void showView(final View view) {
		if (dataLayout == null) {
			return;
		}
		dataLayout.addComponent((Component) view);
	}
	
}
