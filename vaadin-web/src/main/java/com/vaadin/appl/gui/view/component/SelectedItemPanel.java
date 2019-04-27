package com.vaadin.appl.gui.view.component;

import static java.time.temporal.ChronoUnit.DAYS;

import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.ui.Panel;

/**
 * @author Kastalski Sergey
 */
public class SelectedItemPanel extends Panel {
	
	private static final long serialVersionUID = 9023423934866659796L;
	
	public SelectedItemPanel(final HotelsSearchCriteriaDto criteria) {
		setCaption(getText(criteria, null));
		setCaptionAsHtml(true);
	}
	
	public SelectedItemPanel(final HotelsSearchCriteriaDto criteria, final HotelOfferDto hotelOffer) {
		setCaption(getText(criteria, hotelOffer));
		setCaptionAsHtml(true);
	}
	
	private String getText(final HotelsSearchCriteriaDto criteria, final HotelOfferDto hotelOffer) {
		final StringBuilder content = new StringBuilder();
		if (hotelOffer != null) {
			content.append("<div style='float:left;'>");
			content.append("<img src='");
			content.append(hotelOffer.getOfferImage());
			content.append("' style='width:100px; height:90px; margin: 10px;' />");
			content.append("</div>");
		}
		
		content.append("<div style='float:left;'>");
		content.append(criteria.getCity().getName());
		content.append(", ");
		content.append(criteria.getStart());
		content.append(" - ");
		content.append(criteria.getEnd());
		content.append(" (");
		content.append(DAYS.between(criteria.getStart(), criteria.getEnd()));
		content.append(" nights), Adults: ");
		content.append(criteria.getAdultsCount());
		content.append(", Children: ");
		content.append(criteria.getChildrenCount());
		
		if (hotelOffer != null) {
			content.append("<br/>\"");
			content.append(hotelOffer.getHotelName());
			content.append("\" ");
			content.append(hotelOffer.getStars());
			content.append("<br/>");
			content.append(hotelOffer.getOfferName());
		}
		content.append("</div>");
		
		return content.toString();
	}
	
}
