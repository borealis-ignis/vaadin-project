package com.vaadin.appl.gui.view.bookingflow;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.appl.gui.page.BookingFlowPage;
import com.vaadin.appl.gui.session.SessionAttributes;
import com.vaadin.appl.gui.view.component.BookingFlowView;
import com.vaadin.appl.gui.view.component.Pagination;
import com.vaadin.appl.gui.view.component.SelectedItemPanel;
import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Kastalski Sergey
 */
@Service
@SpringView(name = OffersListView.NAME, ui = BookingFlowPage.class)
public class OffersListView extends VerticalLayout implements BookingFlowView {
	
	private static final long serialVersionUID = 8401022066825622265L;
	
	public final static String NAME = "offers";
	
	public OffersListView() {
		Page.getCurrent().setTitle("Offers Page");
	}
	
	@Override
	public void enter(final ViewChangeEvent event) {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		
		addComponent(mainLayout);
		
		final UI ui = getUI();
		
		@SuppressWarnings("unchecked")
		final List<HotelOfferDto> offers = (List<HotelOfferDto>) ui.getSession().getAttribute(SessionAttributes.OFFERS.toString());
		final HotelsSearchCriteriaDto criteria = (HotelsSearchCriteriaDto) ui.getSession().getAttribute(SessionAttributes.HOTELS_SEARCH_CRITERIA.toString());
		if (offers == null || criteria == null) {
			ui.access(() -> {
				ui.getNavigator().navigateTo(SearchHotelsView.NAME);
			});
			return;
		}
		
		final VerticalLayout offersLayout = new VerticalLayout();
		offersLayout.setSizeFull();
		
		final int[] itemsCountPerPage = new int[] {3, 5, 10};
		final PaginationResource paginationResource = PaginationResource.newBuilder().setPage(1).setTotal(offers.size()).setLimit(itemsCountPerPage[0]).build();
		final Pagination pagination = new Pagination(paginationResource);
		pagination.setItemsPerPage(itemsCountPerPage);
		pagination.addPageChangeListener(listener -> {
			offersLayout.removeAllComponents();
			for (final HotelOfferDto offer : offers.subList(listener.fromIndex(), listener.toIndex())) {
				offersLayout.addComponent(createOfferContainer(offer, ui));
			}
		});
		pagination.loadFirstPage();
		
		final VerticalLayout paginationLayout = new VerticalLayout(pagination);
		paginationLayout.setWidth(400, Unit.PIXELS);
		
		mainLayout.addComponent(new SelectedItemPanel(criteria));
		mainLayout.addComponents(offersLayout, paginationLayout);
		mainLayout.setComponentAlignment(paginationLayout, Alignment.BOTTOM_LEFT);
	}
	
	private Layout createOfferContainer(final HotelOfferDto offer, final UI ui) {
		final HorizontalLayout offerLayout = new HorizontalLayout();
		offerLayout.setWidth(800, Unit.PIXELS);
		offerLayout.setHeight(210, Unit.PIXELS);
		offerLayout.setMargin(false);
		offerLayout.addStyleName("border");
		
		final Image offerImage = new Image();
		offerImage.setSource(new ExternalResource(offer.getOfferImage()));
		offerImage.setWidth(230, Unit.PIXELS);
		offerImage.setHeight(200, Unit.PIXELS);
		
		final VerticalLayout offerDataLayout = new VerticalLayout();
		offerDataLayout.setWidth(300, Unit.PIXELS);
		final Label hotelInfo = new Label("\"" + offer.getHotelName() + "\", " + offer.getCityName());
		hotelInfo.addStyleName("bold");
		final Label starsInfo = new Label(offer.getStars());
		final Label offerInfo = new Label(offer.getOfferName());
		offerInfo.setWidth(300, Unit.PIXELS);
		offerDataLayout.addComponents(hotelInfo, starsInfo, offerInfo);
		
		final VerticalLayout offerChooseLayout = new VerticalLayout();
		offerChooseLayout.setSizeFull();
		final Label priceInfo = new Label(offer.getPrice() + " " + StringUtils.upperCase(offer.getCurrency()));
		priceInfo.addStyleNames(ValoTheme.LABEL_COLORED, ValoTheme.LABEL_BOLD, ValoTheme.LABEL_H3);
		final Button buttonBook = new Button("Book", new BookClickListener(offer, ui));
		buttonBook.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		buttonBook.setIcon(VaadinIcons.BED);
		buttonBook.setWidth(100, Unit.PIXELS);
		offerChooseLayout.addComponents(priceInfo, buttonBook);
		offerChooseLayout.setComponentAlignment(priceInfo, Alignment.TOP_RIGHT);
		offerChooseLayout.setComponentAlignment(buttonBook, Alignment.BOTTOM_RIGHT);
		
		offerLayout.addComponents(offerImage, offerDataLayout, offerChooseLayout);
		
		return offerLayout;
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
	
	private class BookClickListener implements ClickListener {
		
		private static final long serialVersionUID = -6722320915199887201L;
		
		private HotelOfferDto offer;
		
		private UI ui;
		
		public BookClickListener(final HotelOfferDto offer, final UI ui) {
			this.offer = offer;
			this.ui = ui;
		}
		
		@Override
		public void buttonClick(final ClickEvent event) {
			ui.getSession().setAttribute(SessionAttributes.OFFER_ID.toString(), offer.getOfferID());
			
			ui.getNavigator().navigateTo(GuestsView.NAME);
		}
		
	}

}
