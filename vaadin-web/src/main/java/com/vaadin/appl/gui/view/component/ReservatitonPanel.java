package com.vaadin.appl.gui.view.component;

import static java.time.temporal.ChronoUnit.DAYS;

import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.ui.Panel;

/**
 * @author Kastalski Sergey
 */
public class ReservatitonPanel extends Panel {
	
	private static final long serialVersionUID = -7512737096476398488L;
	
	public ReservatitonPanel(final HotelsSearchCriteriaDto criteria, final ReservationDto reservation) {
		setCaption(getText(criteria, reservation));
		setCaptionAsHtml(true);
	}
	
	private String getText(final HotelsSearchCriteriaDto criteria, final ReservationDto reservation) {
		final StringBuilder content = new StringBuilder();
		content.append("<div style='float:left;'>");
		content.append("<img src='");
		content.append(reservation.getOfferImage());
		content.append("' style='width:100px; height:90px; margin: 10px;' />");
		content.append("</div>");
		
		content.append("<div style='float:left;'>");
		content.append(reservation.getCityName());
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
		
		content.append("<br/>\"");
		content.append(reservation.getHotelName());
		content.append("\" ");
		content.append(reservation.getStars());
		content.append("<br/>");
		content.append(reservation.getOfferName());
		content.append("</div>");
		
		return content.toString();
	}
	
}
