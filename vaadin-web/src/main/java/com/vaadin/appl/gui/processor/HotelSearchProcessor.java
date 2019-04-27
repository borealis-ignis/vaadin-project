package com.vaadin.appl.gui.processor;

import java.util.List;

import com.vaadin.appl.gui.session.SessionAttributes;
import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.appl.service.SearchService;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

/**
 * @author Kastalski Sergey
 */
public class HotelSearchProcessor extends AbstractProcessor {
	
	private SearchService searchService;
	
	private VaadinService vaadinService;
	
	private HotelsSearchCriteriaDto criteria;
	
	
	public HotelSearchProcessor(
			final SearchService searchService,
			final UI ui,
			final String navigateTo,
			final VaadinService vaadinService,
			final HotelsSearchCriteriaDto criteria) {
		super(ui, navigateTo);
		
		this.searchService = searchService;
		this.vaadinService = vaadinService;
		this.criteria = criteria;
	}
	
	@Override
	protected void process() {
		UI.setCurrent(ui);
		CurrentInstance.set(VaadinService.class, vaadinService);
		
		final List<HotelOfferDto> offers = searchService.getHotelOffers(criteria);
		
		ui.getSession().setAttribute(SessionAttributes.OFFERS.toString(), offers);
	}
}
