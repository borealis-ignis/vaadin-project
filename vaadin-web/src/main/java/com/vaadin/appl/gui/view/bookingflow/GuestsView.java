package com.vaadin.appl.gui.view.bookingflow;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.vaadin.appl.gui.page.BookingFlowPage;
import com.vaadin.appl.gui.session.SessionAttributes;
import com.vaadin.appl.gui.view.component.BookingFlowView;
import com.vaadin.appl.gui.view.component.SelectedItemPanel;
import com.vaadin.appl.model.dto.GuestDto;
import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.appl.model.enums.GuestType;
import com.vaadin.appl.service.util.OffersUtil;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Kastalski Sergey
 */
@Service
@SpringView(name = GuestsView.NAME, ui = BookingFlowPage.class)
public class GuestsView extends VerticalLayout implements BookingFlowView {
	
	private static final long serialVersionUID = -7846749411757721755L;
	
	public final static String NAME = "guests";
	
	
	public GuestsView() {
		Page.getCurrent().setTitle("Guests Page");
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
		
		
		mainLayout.addComponent(new SelectedItemPanel(criteria, OffersUtil.getOffer(offerID, offers)));
		
		final List<Binder<GuestDto>> guests = new ArrayList<>();
		
		for (int i = 1; i <= criteria.getAdultsCount(); i++) {
			guests.add(addGuestPod(mainLayout, "Adult #" + i, GuestType.ADULT));
		}
		for (int i = 1; i <= criteria.getChildrenCount(); i++) {
			guests.add(addGuestPod(mainLayout, "Child #" + i, GuestType.CHILD));
		}
		
		
		
		final Button buttonPayment = new Button("Go to Payment", new PayClickListener(ui, guests));
		buttonPayment.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		buttonPayment.setIcon(VaadinIcons.MONEY);
		buttonPayment.setWidth(180, Unit.PIXELS);
		
		mainLayout.addComponent(buttonPayment);
	}
	
	private Binder<GuestDto> addGuestPod(final Layout mainLayout, final String captionText, final GuestType guestType) {
		final int adultMinAge = 18;
		final int childMinAge = 2;
		final int minAge = (guestType == GuestType.ADULT)? adultMinAge : childMinAge;
		
		final Label caption = new Label(captionText);
		final ComboBox<String> titleField = new ComboBox<>("Title");
		titleField.setItems("Mr.", "Mrs.");
		final TextField firstNameField = new TextField("Given Name");
		final TextField lastNameField = new TextField("Surname");
		final DateField birthDateField = new DateField("Birth Date");
		birthDateField.setDateFormat("dd/MM/yyyy");
		birthDateField.setDefaultValue(LocalDate.now().minus(minAge, ChronoUnit.YEARS));
		final FormLayout searchForm = new FormLayout();
		searchForm.addComponents(caption, titleField, firstNameField, lastNameField, birthDateField);
		searchForm.setWidth(500, Unit.PIXELS);
		searchForm.addStyleName("border");
		searchForm.setMargin(false);
		
		mainLayout.addComponent(searchForm);
		
		final Binder<GuestDto> binder = new Binder<>();
		binder.forField(titleField)
				.withValidator((value) -> StringUtils.isNotBlank(value), "Title should be set")
				.bind(GuestDto::getTitle, GuestDto::setTitle);
		binder.forField(firstNameField)
				.withValidator((value) -> StringUtils.isNotBlank(value), "Given name should be set")
				.bind(GuestDto::getGivenName, GuestDto::setGivenName);
		binder.forField(lastNameField)
				.withValidator((value) -> StringUtils.isNotBlank(value), "Surname should be set")
				.bind(GuestDto::getSurname, GuestDto::setSurname);
		binder.forField(birthDateField)
				.withValidator((value) -> value != null, "Birth Date should be set")
				.withValidator((value) -> {
					if (guestType == GuestType.ADULT) {
						final LocalDate limit = LocalDate.now().minus(adultMinAge, ChronoUnit.YEARS);
						return limit.isAfter(value) || limit.isEqual(value);
					}
					return true;
				}, "Adult should be minimum " + adultMinAge + " years old")
				.withValidator((value) -> {
					if (guestType == GuestType.CHILD) {
						final LocalDate limit = LocalDate.now().minus(childMinAge, ChronoUnit.YEARS);
						return limit.isAfter(value) || limit.isEqual(value);
					}
					return true;
				}, "Child should be minimum " + childMinAge + " years old")
				.bind(GuestDto::getBirthDate, GuestDto::setBirthDate);
		
		final GuestDto guest = new GuestDto();
		guest.setGuestType(guestType);
		binder.setBean(guest);
		
		return binder;
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
	
	private class PayClickListener implements ClickListener {
		
		private static final long serialVersionUID = 3167721285595387770L;
		
		private List<Binder<GuestDto>> guestsBinders;
		
		private UI ui;
		
		public PayClickListener(final UI ui, final List<Binder<GuestDto>> guestsBinders) {
			this.ui = ui;
			this.guestsBinders = guestsBinders;
		}
		
		@Override
		public void buttonClick(final ClickEvent event) {
			final List<GuestDto> guests = new ArrayList<>();
			if (!isValid(guests)) {
				return;
			}
			
			ui.getSession().setAttribute(SessionAttributes.GUESTS.toString(), guests);
			ui.getSession().setAttribute(SessionAttributes.NAVIGATE_TO.toString(), PaymentView.NAME);
			
			ui.getNavigator().navigateTo(SpinnerView.NAME);
		}
		
		private boolean isValid(final List<GuestDto> guests) {
			for (final Binder<GuestDto> binder : guestsBinders) {
				final BinderValidationStatus<GuestDto> status = binder.validate();
				if (status.isOk()) {
					guests.add(binder.getBean());
				}
			}
			
			if (guests.size() == guestsBinders.size()) {
				return true;
			}
			return false;
		}
		
	}
	
}
