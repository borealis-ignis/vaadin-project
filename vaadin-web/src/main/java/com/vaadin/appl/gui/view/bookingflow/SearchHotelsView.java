package com.vaadin.appl.gui.view.bookingflow;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.vaadin.appl.gui.converter.IntegerConverter;
import com.vaadin.appl.gui.page.BookingFlowPage;
import com.vaadin.appl.gui.session.SessionAttributes;
import com.vaadin.appl.gui.validator.IntegerValidator;
import com.vaadin.appl.gui.view.component.BookingFlowView;
import com.vaadin.appl.model.dto.CityDto;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.appl.service.SearchService;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Kastalski Sergey
 *
 */
@Service
@SpringView(name = SearchHotelsView.NAME, ui = BookingFlowPage.class)
public class SearchHotelsView extends VerticalLayout implements BookingFlowView {
	
	private static final long serialVersionUID = -7236498307580510807L;
	
	public static final String NAME = "";
	
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	private final Binder<HotelsSearchCriteriaDto> binder = new Binder<>();
	
	private IntegerConverter integerConverter;
	
	private SearchService searchService;
	
	public SearchHotelsView(final IntegerConverter integerConverter, final SearchService searchService) {
		this.integerConverter = integerConverter;
		this.searchService = searchService;
		
		Page.getCurrent().setTitle("Hotel Search Page");
	}
	
	@Override
	public void enter(final ViewChangeEvent event) {
		final VaadinSession session = getUI().getSession();
		
		final Label errorLabel = new Label();
		errorLabel.setContentMode(ContentMode.HTML);
		errorLabel.setStyleName("red");
		errorLabel.setVisible(false);
		
		final ComboBox<CityDto> cityField = new ComboBox<>("City");
		cityField.setWidth(200, Unit.PIXELS);
		cityField.setItems(searchService.getCities());
		cityField.setItemCaptionGenerator(CityDto::getName);
		final DateField startDateField = createDateField("Check-in");
		startDateField.setRangeStart(LocalDate.now());
		startDateField.setDateOutOfRangeMessage("Start date is out of allowed range");
		
		final DateField endDateField = createDateField("Check-out");
		endDateField.setRangeStart(LocalDate.now().plusDays(1));
		endDateField.setDateOutOfRangeMessage("End date is out of allowed range");
		
		final TextField adultsCountField = new TextField("Adults count");
		final TextField childrenCountField = new TextField("Children count");
		
		final Button searchButton = new Button("Search");
		searchButton.addClickListener(listener -> {
			errorLabel.setVisible(false);
			if (binder.isValid()) {
				session.setAttribute(SessionAttributes.HOTELS_SEARCH_CRITERIA.toString(), binder.getBean());
				session.setAttribute(SessionAttributes.NAVIGATE_TO.toString(), OffersListView.NAME);
				getUI().getNavigator().navigateTo(SpinnerView.NAME);
			} else {
				final StringBuilder errorsBuilder = new StringBuilder();
				errorsBuilder.append("<ul>");
				
				final BinderValidationStatus<HotelsSearchCriteriaDto> binderValidation = binder.validate();
				binderValidation.getValidationErrors().forEach(validationResult -> {
					errorsBuilder.append("<li>");
					errorsBuilder.append(validationResult.getErrorMessage());
					errorsBuilder.append("</li>");
				});
				
				errorsBuilder.append("</ul>");
				errorLabel.setValue(errorsBuilder.toString());
				errorLabel.setVisible(true);
			}
		});
		
		startDateField.addValueChangeListener(listener -> {
			final LocalDate chosenValue = listener.getValue();
			if (listener.getOldValue() != null && chosenValue.isEqual(listener.getOldValue())) {
				return;
			}
			final LocalDate chosenPlusOneDay = chosenValue.plusDays(1);
			if (endDateField.getValue() != null && endDateField.getValue().isBefore(chosenPlusOneDay)) {
				return;
			}
			
			endDateField.setRangeStart(chosenPlusOneDay);
		});
		endDateField.addValueChangeListener(listener -> {
			final LocalDate chosenValue = listener.getValue();
			if (listener.getOldValue() != null && chosenValue.isEqual(listener.getOldValue())) {
				return;
			}
			final LocalDate chosenMinusOneDay = chosenValue.minusDays(1);
			if (startDateField.getValue() != null && startDateField.getValue().isAfter(chosenMinusOneDay)) {
				return;
			}
			
			startDateField.setRangeEnd(chosenMinusOneDay);
		});
		
		final FormLayout searchForm = new FormLayout();
		searchForm.addComponents(errorLabel, cityField, startDateField, endDateField, adultsCountField, childrenCountField, searchButton);
		
		binder.forField(cityField)
				.withValidator((value) -> value != null, "City is not set")
				.bind(HotelsSearchCriteriaDto::getCity, HotelsSearchCriteriaDto::setCity);
		binder.forField(startDateField)
				.withValidator((value) -> value != null, "Start Date is not set")
				.bind(HotelsSearchCriteriaDto::getStart, HotelsSearchCriteriaDto::setStart);
		binder.forField(endDateField)
				.withValidator((value) -> value != null, "End Date is not set")
				.bind(HotelsSearchCriteriaDto::getEnd, HotelsSearchCriteriaDto::setEnd);
		binder.forField(adultsCountField)
				.withValidator(new IntegerValidator("Adults count should be an integer value in the range of 1 - 9", 1))
				.withConverter(integerConverter)
				.bind(HotelsSearchCriteriaDto::getAdultsCount, HotelsSearchCriteriaDto::setAdultsCount);
		binder.forField(childrenCountField)
				.withValidator(new IntegerValidator("Children count should be an integer value in the range of 0 - 9", 0))
				.withConverter(integerConverter)
				.bind(HotelsSearchCriteriaDto::getChildrenCount, HotelsSearchCriteriaDto::setChildrenCount);
		
		processSession(session);
		
		addComponent(searchForm);
	}
	
	private void processSession(final VaadinSession session) {
		HotelsSearchCriteriaDto criteria = (HotelsSearchCriteriaDto) session.getAttribute(SessionAttributes.HOTELS_SEARCH_CRITERIA.toString());
		if (criteria == null) {
			criteria = new HotelsSearchCriteriaDto(null, null, null, 1, 0);
		}
		binder.setBean(criteria);
		
		for (final SessionAttributes attrName : SessionAttributes.values()) {
			session.setAttribute(attrName.toString(), null);
		}
	}
	
	private DateField createDateField(final String caption) {
		final DateField dateField = new DateField(caption);
		dateField.setDateFormat(DATE_FORMAT);
		dateField.setPlaceholder(DATE_FORMAT);
		dateField.setLenient(true);
		
		return dateField;
	}

	@Override
	public void setTopButtons(final Map<String, Button> topButtons) {
		topButtons.entrySet().forEach(entry -> {
			if (!NAME.equals(entry.getKey())) {
				entry.getValue().setEnabled(false);
			}
		});
	}
}
