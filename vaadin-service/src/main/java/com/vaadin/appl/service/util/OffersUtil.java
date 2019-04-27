package com.vaadin.appl.service.util;

import java.util.List;

import com.vaadin.appl.model.dto.HotelOfferDto;

/**
 * @author Kastalski Sergey
 */
public final class OffersUtil {
	
	public static HotelOfferDto getOffer(final long id, final List<HotelOfferDto> offers) {
		return offers.stream().filter(offer -> offer.getOfferID() == id).findFirst().orElse(null);
	}
	
}
