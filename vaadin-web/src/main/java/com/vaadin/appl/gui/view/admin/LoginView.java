package com.vaadin.appl.gui.view.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.appl.gui.page.BookingFlowPage;
import com.vaadin.appl.gui.page.LoginPage;
import com.vaadin.appl.security.SecurityConfig;
import com.vaadin.appl.security.listener.ReservationsLoginListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Kastalski Sergey
 */
@Service
@SpringView(name = LoginView.NAME, ui = LoginPage.class)
public class LoginView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -7002031340273242232L;
	
	public final static String NAME = "";
	
	@Autowired
    protected SecurityConfig securityConfig;
	
	
	public LoginView() {
		Page.getCurrent().setTitle("Login Page");
		this.setMargin(false);
	}
	
	@Override
	public void enter(final ViewChangeEvent event) {
		final Label errorLabel = new Label();
		errorLabel.setContentMode(ContentMode.HTML);
		errorLabel.addStyleNames("red", "no-margin");
		errorLabel.setVisible(false);
		
		final LoginForm loginForm = new LoginForm();
		loginForm.addLoginListener(new ReservationsLoginListener(getUI(), errorLabel, securityConfig));
		
		final Button hotelsSearchButton = new Button("Go to Hotels Search", (listener) -> {
			Page.getCurrent().setLocation(VaadinService.getCurrentRequest().getContextPath() + BookingFlowPage.PATH);
		});
		
		addComponent(errorLabel);
		addComponent(loginForm);
		addComponent(hotelsSearchButton);
	}
	
}
