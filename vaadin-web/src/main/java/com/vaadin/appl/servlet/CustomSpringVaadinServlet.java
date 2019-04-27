package com.vaadin.appl.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.stereotype.Component;

import com.vaadin.appl.servlet.uiprovider.CustomSpringUIProvider;
import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.internal.UIScopeImpl;
import com.vaadin.spring.internal.VaadinSessionScope;
import com.vaadin.spring.server.SpringVaadinServlet;

/**
 * @author Kastalski Sergey
 */
@Component("vaadinServlet")
public class CustomSpringVaadinServlet extends SpringVaadinServlet {
	
	private static final long serialVersionUID = -2921493053348196091L;
	
	
	@Override
	protected void servletInitialized() throws ServletException {
		final VaadinServletService service = getService();
		service.addSessionInitListener(new SessionInitListener() {
			
			private static final long serialVersionUID = 1627217102374012193L;

			@Override
			public void sessionInit(final SessionInitEvent sessionInitEvent) throws ServiceException {
				final VaadinSession session = sessionInitEvent.getSession();
				final List<UIProvider> uiProviders = new ArrayList<>(session.getUIProviders());
				for (final UIProvider provider : uiProviders) {
					if (DefaultUIProvider.class.getCanonicalName().equals(provider.getClass().getCanonicalName())) {
						session.removeUIProvider(provider);
					}
				}
				
				final CustomSpringUIProvider uiProvider = new CustomSpringUIProvider(session);
				session.addUIProvider(uiProvider);
			}
		});
		service.addSessionDestroyListener(new SessionDestroyListener() {
			
			private static final long serialVersionUID = 6980870036334647645L;

			@Override
			public void sessionDestroy(final SessionDestroyEvent event) {
				final VaadinSession session = event.getSession();
				
				UIScopeImpl.cleanupSession(session);
				VaadinSessionScope.cleanupSession(session);
			}
		});
	}
	
}
