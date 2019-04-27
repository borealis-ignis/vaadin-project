package com.vaadin.appl.gui.view.bookingflow;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.vaadin.appl.gui.page.BookingFlowPage;
import com.vaadin.appl.gui.session.SessionAttributes;
import com.vaadin.appl.gui.view.component.BookingFlowView;
import com.vaadin.appl.gui.view.component.ReservatitonPanel;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.appl.model.enums.ReservationStatus;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Kastalski Sergey
 */
@Service
@SpringView(name = ConfirmationBookingView.NAME, ui = BookingFlowPage.class)
public class ConfirmationBookingView extends VerticalLayout implements BookingFlowView {
	
	private static final long serialVersionUID = -8071279527991683296L;
	
	public final static String NAME = "confirmation";
	
	
	public ConfirmationBookingView() {
		Page.getCurrent().setTitle("Confirmation Page");
	}
	
	@Override
	public void enter(final ViewChangeEvent event) {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		
		addComponent(mainLayout);
		
		final UI ui = getUI();
		
		final ReservationDto reservation = (ReservationDto) ui.getSession().getAttribute(SessionAttributes.RESERVATION.toString());
		final HotelsSearchCriteriaDto criteria = (HotelsSearchCriteriaDto) ui.getSession().getAttribute(SessionAttributes.HOTELS_SEARCH_CRITERIA.toString());
		if (reservation == null || criteria == null) {
			ui.access(() -> {
				ui.getNavigator().navigateTo(SearchHotelsView.NAME);
			});
			return;
		}
		
		
		final ReservationStatus status = reservation.getStatus();
		final String colorClass = (status == ReservationStatus.CONFIRMED)? "green" : "red";
		final Label reservationLabel = new Label("Reservation is <span class='bold " + colorClass + " font16'>" + status.getValue() + "</span>");
		reservationLabel.setContentMode(ContentMode.HTML);
		
		
		mainLayout.addComponent(new ReservatitonPanel(criteria, reservation));
		
		mainLayout.addComponent(reservationLabel);
		
		if (status == ReservationStatus.CONFIRMED) {
			final Label priceLabel = new Label("Price: <span class='bold green'>" + reservation.getPrice() + " " + StringUtils.upperCase(reservation.getCurrency()) + "</span>");
			priceLabel.setContentMode(ContentMode.HTML);
			mainLayout.addComponent(priceLabel);
		}
		
		mainLayout.addComponent(new Label("Guests:"));
		reservation.getGuests().forEach(guest -> {
			final Label guestLabel = new Label(guest.getTitle() + " " + guest.getGivenName() + " " + guest.getSurname() + " (" + guest.getBirthDate() + ")");
			mainLayout.addComponent(guestLabel);
		});
	}
	
	@Override
	public void setTopButtons(final Map<String, Button> topButtons) {
		boolean enabled = true;
		for (final Entry<String, Button> entry : topButtons.entrySet()) {
			entry.getValue().setEnabled(enabled);
			enabled = false;
		}
	}

}
