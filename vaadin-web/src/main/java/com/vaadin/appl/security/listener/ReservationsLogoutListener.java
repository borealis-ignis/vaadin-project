package com.vaadin.appl.security.listener;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * @author Kastalski Sergey
 */
public class ReservationsLogoutListener implements ClickListener {
	
	private static final long serialVersionUID = 2879586259538181532L;
	
	private static final String LOGOUT_URL = "/logout";
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		SecurityContextHolder.clearContext();
		Page.getCurrent().open(VaadinService.getCurrentRequest().getContextPath() + LOGOUT_URL, null);
	}
	
}
