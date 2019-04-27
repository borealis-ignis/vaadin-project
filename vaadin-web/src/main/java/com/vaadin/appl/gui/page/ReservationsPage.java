package com.vaadin.appl.gui.page;

import com.vaadin.annotations.Theme;
import com.vaadin.appl.security.listener.ReservationsLogoutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Kastalski Sergey
 */
@SpringUI(path = ReservationsPage.PATH)
@Theme("mytheme")
@SpringViewDisplay
public class ReservationsPage extends UI implements ViewDisplay {
	
	public static final String PATH = "/reservations";
	
	private static final long serialVersionUID = 1487626195984834901L;
	
	private VerticalLayout dataLayout;
	
	
	@Override
	protected void init(final VaadinRequest request) {
		final VerticalLayout mainLayout = new VerticalLayout();
		setContent(mainLayout);
		
		final HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setSizeFull();
		mainLayout.addComponent(topLayout);
		mainLayout.setComponentAlignment(topLayout, Alignment.MIDDLE_CENTER);
		
		final HorizontalLayout buttonsTopLayout = new HorizontalLayout();
		buttonsTopLayout.setSpacing(false);
		buttonsTopLayout.setMargin(false);
		topLayout.addComponent(buttonsTopLayout);
		topLayout.setComponentAlignment(buttonsTopLayout, Alignment.TOP_RIGHT);
		
		final Button hotelsSearchButton = new Button("Go to Hotels Search", (listener) -> {
			Page.getCurrent().setLocation(request.getContextPath() + BookingFlowPage.PATH);
		});
		hotelsSearchButton.addStyleName("small-margin-right");
		buttonsTopLayout.addComponent(hotelsSearchButton);
		buttonsTopLayout.setComponentAlignment(hotelsSearchButton, Alignment.TOP_RIGHT);
		
		final Button logoutButton = new Button("Log-out", new ReservationsLogoutListener());
		buttonsTopLayout.addComponent(logoutButton);
		buttonsTopLayout.setComponentAlignment(logoutButton, Alignment.TOP_RIGHT);
		buttonsTopLayout.addStyleName("margin-top");
		buttonsTopLayout.addStyleName("padding-right");
		
		dataLayout = new VerticalLayout();
		mainLayout.addComponent(dataLayout);
		mainLayout.setComponentAlignment(dataLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(false);
		mainLayout.setMargin(false);
	}
	
	@Override
	public void showView(final View view) {
		if (dataLayout == null) {
			return;
		}
		dataLayout.addComponent((Component) view);
	}
	
}
