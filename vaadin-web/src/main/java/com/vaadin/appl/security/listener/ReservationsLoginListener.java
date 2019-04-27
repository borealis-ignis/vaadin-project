package com.vaadin.appl.security.listener;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.appl.gui.page.ReservationsPage;
import com.vaadin.appl.security.RolesEnum;
import com.vaadin.appl.security.SecurityConfig;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;
import com.vaadin.ui.UI;

/**
 * @author Kastalski Sergey
 */
public class ReservationsLoginListener implements LoginListener {
	
	private static final long serialVersionUID = -73409448810411482L;
	
	
	private UI ui;
	
	private Label errorLabel;
	
	private SecurityConfig securityConfig;
	
	
	public ReservationsLoginListener(final UI ui, final Label errorLabel, final SecurityConfig securityConfig) {
		this.ui = ui;
		this.errorLabel = errorLabel;
		this.securityConfig = securityConfig;
	}
	
	@Override
	public void onLogin(final LoginEvent event) {
		final String username = event.getLoginParameter("username");
		final String password = event.getLoginParameter("password");
		
		final UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
		try {
			final Authentication auth = securityConfig.authenticationManagerBean().authenticate(authReq);
			final SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(auth);
			
			auth.getAuthorities().forEach(authority -> {
				if (authority.getAuthority().equals("ROLE_" + RolesEnum.ADMIN_ROLE.getValue())) {
					ui.getPage().setLocation(VaadinService.getCurrentRequest().getContextPath() + ReservationsPage.PATH);
				}
			});
		} catch (final Exception e) {
			errorLabel.setValue(e.getMessage());
			errorLabel.setVisible(true);
		}
	}
	
}
