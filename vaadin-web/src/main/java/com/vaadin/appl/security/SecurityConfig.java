package com.vaadin.appl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import com.vaadin.appl.gui.page.BookingFlowPage;
import com.vaadin.appl.gui.page.LoginPage;
import com.vaadin.appl.gui.page.ReservationsPage;

/**
 * @author Kastalski Sergey
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private String adminLogin;
	
	private String adminPassword;
	
	
	@Autowired
	public SecurityConfig(
			@Value("${security.admin.login}") final String adminLogin,
			@Value("${security.admin.password}") final String adminPassword) {
		super();
		this.adminLogin = adminLogin;
		this.adminPassword = adminPassword;
	}
	
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
					.antMatchers("/VAADIN/**", "/HEARTBEAT/**", "/UIDL/**", "/vaadinServlet/**", "/resources/**", "/image/**",
							BookingFlowPage.PATH + "/**", 
							LoginPage.PATH + "/**").permitAll()
					.antMatchers(ReservationsPage.PATH + "/**").hasRole(RolesEnum.ADMIN_ROLE.getValue())
					.anyRequest().authenticated()
					.and().logout()
						.invalidateHttpSession(true)
	                	.clearAuthentication(true)
						.permitAll()
						.logoutSuccessUrl(BookingFlowPage.PATH)
					.and().exceptionHandling().authenticationEntryPoint((rq, rs, ex) -> {
						final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
						redirectStrategy.sendRedirect(rq, rs, BookingFlowPage.PATH);
					});
	}
	
	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		auth.inMemoryAuthentication().passwordEncoder(encoder).withUser(adminLogin).password(adminPassword).roles(RolesEnum.ADMIN_ROLE.getValue());
	}
}
