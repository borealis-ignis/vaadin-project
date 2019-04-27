package com.vaadin.appl.gui.view.bookingflow;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.vaadin.appl.gui.page.BookingFlowPage;
import com.vaadin.appl.gui.session.SessionAttributes;
import com.vaadin.appl.gui.view.component.BookingFlowView;
import com.vaadin.appl.gui.view.component.SelectedItemPanel;
import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.appl.model.dto.PaymentCardDto;
import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.appl.service.util.OffersUtil;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Kastalski Sergey
 */
@Service
@SpringView(name = PaymentView.NAME, ui = BookingFlowPage.class)
public class PaymentView extends VerticalLayout implements BookingFlowView {
	
	private static final long serialVersionUID = 6655572084233346859L;
	
	public final static String NAME = "payment";
	
	
	public PaymentView() {
		Page.getCurrent().setTitle("Payment Page");
	}
	
	@Override
	public void enter(final ViewChangeEvent event) {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		
		addComponent(mainLayout);
		
		final UI ui = getUI();
		
		@SuppressWarnings("unchecked")
		final List<HotelOfferDto> offers = (List<HotelOfferDto>) ui.getSession().getAttribute(SessionAttributes.OFFERS.toString());
		final Long offerID = (Long) ui.getSession().getAttribute(SessionAttributes.OFFER_ID.toString());
		final HotelsSearchCriteriaDto criteria = (HotelsSearchCriteriaDto) ui.getSession().getAttribute(SessionAttributes.HOTELS_SEARCH_CRITERIA.toString());
		if (offers == null || offerID == null || criteria == null) {
			ui.access(() -> {
				ui.getNavigator().navigateTo(SearchHotelsView.NAME);
			});
			return;
		}
		
		final HorizontalLayout subMainLayout = new HorizontalLayout();
		
		mainLayout.addComponent(new SelectedItemPanel(criteria, OffersUtil.getOffer(offerID, offers)));
		mainLayout.addComponent(subMainLayout);
		
		final ReservationDto reservation = (ReservationDto) ui.getSession().getAttribute(SessionAttributes.RESERVATION.toString());
		
		final Layout paymentLayout = createPaymentLayout(ui);
		final Layout reservationLayout = createReservationLayout(reservation);
		
		subMainLayout.addComponents(paymentLayout, reservationLayout);
	}
	
	private Layout createPaymentLayout(final UI ui) {
		final Layout paymentLayout = new VerticalLayout();
		paymentLayout.setWidth(400, Unit.PIXELS);
		
		final TextField cardNumber = new TextField("Card Number");
		cardNumber.setWidth(200, Unit.PIXELS);
		cardNumber.setMaxLength(19);
		cardNumber.addValueChangeListener(event -> {
			final String value = event.getValue();
			final String oldValue = event.getOldValue();
			if (value == null || value.length() < 4 || RegExUtils.removeAll(value, " ").equals(RegExUtils.removeAll(oldValue, " "))) {
				return;
			}
			final StringBuilder sb = new StringBuilder();
			final char[] chars = RegExUtils.removeAll(value, " ").toCharArray();
			for (int i = 0; i < chars.length; i++) {
				sb.append(chars[i]);
				if ((i + 1) % 4 == 0 && i < 15) {
					sb.append(' ');
				}
			}
			cardNumber.setValue(sb.toString());
		});
		final DateField cardExpDate = new DateField("Expire Date");
		cardExpDate.setWidth(130, Unit.PIXELS);
		cardExpDate.setDateFormat("MM/yyyy");
		cardExpDate.setRangeStart(LocalDate.now());
		final PasswordField secureNumber = new PasswordField("CVV/CVC Number");
		secureNumber.setWidth(50, Unit.PIXELS);
		secureNumber.setMaxLength(3);
		final TextField cardHolder = new TextField("Card Holder");
		cardHolder.setWidth(200, Unit.PIXELS);
		
		final Binder<PaymentCardDto> binder = new Binder<>();
		binder.forField(cardNumber)
				.withValidator(value -> value != null, "Card Number is not set")
				.withValidator(value -> value.length() == 19, "Length of Card Number should be 16 characters")
				.bind(PaymentCardDto::getCardNumber, PaymentCardDto::setCardNumber);
		binder.forField(cardExpDate)
				.withValidator(value -> value != null, "Expire date is not set")
				.bind(PaymentCardDto::getExpireDate, PaymentCardDto::setExpireDate);
		binder.forField(secureNumber)
				.withValidator(value -> value != null, "CVC/CVV number is not set")
				.withValidator(value -> value.length() == 3, "Length of CVC/CVV number should be 3 characters")
				.bind(PaymentCardDto::getSecureCode, PaymentCardDto::setSecureCode);
		binder.forField(cardHolder)
				.withValidator(value -> value != null, "Card Holder is not set")
				.bind(PaymentCardDto::getCardHolder, PaymentCardDto::setCardHolder);
		binder.setBean(new PaymentCardDto());
		
		final Button payButton = new Button("Pay");
		payButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		payButton.setIcon(VaadinIcons.COINS);
		payButton.setWidth(100, Unit.PIXELS);
		payButton.addClickListener(event -> {
			final BinderValidationStatus<PaymentCardDto> validationStatus = binder.validate();
			if (!validationStatus.isOk()) {
				return;
			}
			final PaymentCardDto paymentData = binder.getBean();
			
			ui.getSession().setAttribute(SessionAttributes.PAYMENT_DATA.toString(), paymentData);
			ui.getSession().setAttribute(SessionAttributes.NAVIGATE_TO.toString(), ConfirmationBookingView.NAME);
			
			ui.getNavigator().navigateTo(SpinnerView.NAME);
		});
		
		paymentLayout.addComponents(cardNumber, secureNumber, cardExpDate, cardHolder, payButton);
		
		return paymentLayout;
	}
	
	private Layout createReservationLayout(final ReservationDto reservation) {
		final Layout reservationLayout = new VerticalLayout();
		reservationLayout.addStyleName("border");
		reservationLayout.addStyleName("padding-top");
		reservationLayout.addStyleName("margin-top");
		reservationLayout.setWidth(480, Unit.PIXELS);
		
		final Label reservationIDPod = new Label("Reservation ID: <b style='color: #197de1;'>" + reservation.getId() + "</b>");
		reservationIDPod.setContentMode(ContentMode.HTML);
		
		final Label pricePod = new Label("Price: <span style='color: #197de1;' class='bold'>" + reservation.getPrice() + " " + StringUtils.upperCase(reservation.getCurrency()) + "</span>");
		pricePod.setContentMode(ContentMode.HTML);
		
		final Label guestsCaptionLabel = new Label("<br//>Guests: ");
		guestsCaptionLabel.setContentMode(ContentMode.HTML);
		
		reservationLayout.addComponents(reservationIDPod, pricePod, guestsCaptionLabel);
		
		reservation.getGuests().forEach(guest -> {
			final Label guestLabel = new Label(guest.getTitle() + " " + guest.getGivenName() + " " + guest.getSurname() + " (" + guest.getBirthDate() + ")");
			reservationLayout.addComponent(guestLabel);
		});
		
		reservationLayout.addComponent(new Label());
		
		return reservationLayout;
	}
	
	@Override
	public void setTopButtons(final Map<String, Button> topButtons) {
		boolean enabled = true;
		for (final Entry<String, Button> entry : topButtons.entrySet()) {
			entry.getValue().setEnabled(enabled);
			if (NAME.equals(entry.getKey())) {
				enabled = false;
			}
		}
	}
	
}
