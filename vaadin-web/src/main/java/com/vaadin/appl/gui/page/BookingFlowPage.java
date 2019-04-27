package com.vaadin.appl.gui.page;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.appl.gui.view.bookingflow.GuestsView;
import com.vaadin.appl.gui.view.bookingflow.OffersListView;
import com.vaadin.appl.gui.view.bookingflow.PaymentView;
import com.vaadin.appl.gui.view.bookingflow.SearchHotelsView;
import com.vaadin.appl.gui.view.component.BookingFlowView;
import com.vaadin.appl.security.listener.ReservationsLogoutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Kastalski Sergey
 */
@SpringUI(path = BookingFlowPage.PATH)
@Theme("mytheme")
@SpringViewDisplay
@Title("Hotels")
@Push
public class BookingFlowPage extends UI implements ViewDisplay {
	
	public static final String PATH = "/hotels";
	
	private static final long serialVersionUID = -88698381221894001L;
	
	private Panel viewsDisplayer;
	
	private Map<String, Button> topButtons;
	
	@Override
	protected void init(final VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		setContent(layout);
		
		
		topButtons = new LinkedHashMap<>();
		topButtons.put(SearchHotelsView.NAME, new Button("Search", new SearchClickListener()));
		topButtons.put(OffersListView.NAME, new Button("Offers", new OffersClickListener()));
		topButtons.put(GuestsView.NAME, new Button("Guests", new GuestsClickListener()));
		topButtons.put(PaymentView.NAME, new Button("Payment", new PaymentClickListener()));
		
		final CssLayout navigationBar = new CssLayout();
		navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		navigationBar.addComponents(topButtons.values().toArray(new Button[topButtons.values().size()]));
		
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final Link adminLink = new Link();
		if (auth instanceof AnonymousAuthenticationToken) {
			adminLink.setCaption("Log-in");
			adminLink.setResource(new ExternalResource(request.getContextPath() + LoginPage.PATH));
		} else {
			adminLink.setCaption(auth.getName());
			adminLink.setResource(new ExternalResource(request.getContextPath() + ReservationsPage.PATH));
		}
		
		final HorizontalLayout adminLayout = new HorizontalLayout();
		adminLayout.setMargin(false);
		adminLayout.setWidth(220, Unit.PIXELS);
		adminLayout.addComponent(adminLink);
		adminLayout.setComponentAlignment(adminLink, Alignment.TOP_RIGHT);
		
		final Button logoutButton = new Button("Log-out", new ReservationsLogoutListener());
		adminLayout.addComponent(logoutButton);
		adminLayout.setComponentAlignment(logoutButton, Alignment.TOP_RIGHT);
		
		final HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setWidth(layout.getWidth(), layout.getWidthUnits());
		topLayout.addComponent(navigationBar);
		topLayout.addComponent(adminLayout);
		topLayout.setComponentAlignment(adminLayout, Alignment.TOP_RIGHT);
		
		layout.addComponent(topLayout);
		
		viewsDisplayer = new Panel();
		viewsDisplayer.setSizeFull();
		layout.addComponent(viewsDisplayer);
		layout.setExpandRatio(viewsDisplayer, 1.0f);
	}
	
	@Override
	public void showView(final View view) {
		if (viewsDisplayer == null) {
			return;
		}
		if (view instanceof BookingFlowView) {
			((BookingFlowView) view).setTopButtons(topButtons);
		}
		viewsDisplayer.setContent((Component) view);
	}
	
	private static class SearchClickListener implements ClickListener {
		
		private static final long serialVersionUID = 7070854794302844358L;

		@Override
		public void buttonClick(final ClickEvent event) {
			getCurrent().getNavigator().navigateTo(SearchHotelsView.NAME);
		}
	}
	
	private static class OffersClickListener implements ClickListener {
		
		private static final long serialVersionUID = -7133805073311019924L;

		@Override
		public void buttonClick(final ClickEvent event) {
			getCurrent().getNavigator().navigateTo(OffersListView.NAME);
		}
	}
	
	private static class GuestsClickListener implements ClickListener {
		
		private static final long serialVersionUID = -6850068246286955405L;
		
		@Override
		public void buttonClick(final ClickEvent event) {
			getCurrent().getNavigator().navigateTo(GuestsView.NAME);
		}
	}
	
	private static class PaymentClickListener implements ClickListener {
		
		private static final long serialVersionUID = -5447507753423622179L;
		
		@Override
		public void buttonClick(final ClickEvent event) {
			getCurrent().getNavigator().navigateTo(PaymentView.NAME);
		}
	}
	
}
