package com.vaadin.appl.gui.view.bookingflow;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.vaadin.appl.gui.page.BookingFlowPage;
import com.vaadin.appl.gui.processor.AbstractProcessor;
import com.vaadin.appl.gui.processor.HotelBookingProcessor;
import com.vaadin.appl.gui.processor.HotelSearchProcessor;
import com.vaadin.appl.gui.processor.PaymentProcessor;
import com.vaadin.appl.gui.session.SessionAttributes;
import com.vaadin.appl.gui.view.component.BookingFlowView;
import com.vaadin.appl.model.dto.GuestDto;
import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.appl.model.dto.PaymentCardDto;
import com.vaadin.appl.service.BookingService;
import com.vaadin.appl.service.PaymentService;
import com.vaadin.appl.service.SearchService;
import com.vaadin.appl.service.util.OffersUtil;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Kastalski Sergey
 */
@Service
@SpringView(name = SpinnerView.NAME, ui = BookingFlowPage.class)
public class SpinnerView extends VerticalLayout implements BookingFlowView {
	
	private static final long serialVersionUID = -7807172960163553325L;
	
	final static String NAME = "spinner";
	
	private SearchService searchService;
	
	private BookingService bookingService;
	
	private PaymentService paymentService;
	
	
	public SpinnerView(final SearchService searchService, final BookingService bookingService, final PaymentService paymentService) {
		this.searchService = searchService;
		this.bookingService = bookingService;
		this.paymentService = paymentService;
		
		Page.getCurrent().setTitle("Wait..");
	}
	
	@Override
	public void enter(final ViewChangeEvent event) {
		final UI ui = getUI();
		final VaadinSession session = ui.getSession();
		final String navigateTo = (String) session.getAttribute(SessionAttributes.NAVIGATE_TO.toString());
		if (StringUtils.isBlank(navigateTo)) {
			ui.access(() -> {
				ui.getNavigator().navigateTo(SearchHotelsView.NAME);
			});
			return;
		}
		
		session.setAttribute(SessionAttributes.NAVIGATE_TO.toString(), null);
		
		final Label waitLabel = new Label("Please Wait...");
		
		final ProgressBar spinner = new ProgressBar();
		spinner.setIndeterminate(true);
		
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.addComponents(waitLabel, spinner);
		mainLayout.setComponentAlignment(spinner, Alignment.TOP_CENTER);
		mainLayout.setComponentAlignment(waitLabel, Alignment.TOP_CENTER);
		addComponent(mainLayout);
		
		final AbstractProcessor processor;
		if (OffersListView.NAME.equals(navigateTo)) {
			final HotelsSearchCriteriaDto criteria = (HotelsSearchCriteriaDto) session.getAttribute(SessionAttributes.HOTELS_SEARCH_CRITERIA.toString());
			
			processor = new HotelSearchProcessor(searchService, ui, navigateTo, VaadinService.getCurrent(), criteria);
		} else if (PaymentView.NAME.equals(navigateTo)) {
			@SuppressWarnings("unchecked")
			final List<GuestDto> guests = (List<GuestDto>) ui.getSession().getAttribute(SessionAttributes.GUESTS.toString());
			@SuppressWarnings("unchecked")
			final List<HotelOfferDto> offers = (List<HotelOfferDto>) ui.getSession().getAttribute(SessionAttributes.OFFERS.toString());
			final Long offerID = (Long) ui.getSession().getAttribute(SessionAttributes.OFFER_ID.toString());
			final HotelOfferDto offer = OffersUtil.getOffer(offerID, offers);
			
			processor = new HotelBookingProcessor(bookingService, ui, navigateTo, offer, guests);
		} else if (ConfirmationBookingView.NAME.equals(navigateTo)) {
			final PaymentCardDto paymentData = (PaymentCardDto) ui.getSession().getAttribute(SessionAttributes.PAYMENT_DATA.toString());
			
			processor = new PaymentProcessor(paymentService, ui, navigateTo, paymentData);
		} else {
			throw new IllegalStateException("Processor not found for Booking view '" + navigateTo + "'");
		}
		
		processor.start();
	}
	
	@Override
	public void setTopButtons(final Map<String, Button> topButtons) {
		topButtons.entrySet().forEach(entry -> entry.getValue().setEnabled(false));
	}
	
}
