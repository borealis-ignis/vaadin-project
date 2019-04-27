package com.vaadin.appl.gui.processor;

import java.util.List;

import com.vaadin.appl.gui.session.SessionAttributes;
import com.vaadin.appl.model.dto.GuestDto;
import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.appl.service.BookingService;
import com.vaadin.ui.UI;

/**
 * @author Kastalski Sergey
 */
public class HotelBookingProcessor extends AbstractProcessor {
	
	private BookingService bookingService;
	
	private HotelOfferDto offer;
	
	private List<GuestDto> guests;
	
	
	public HotelBookingProcessor(
			final BookingService bookingService,
			final UI ui,
			final String navigateTo,
			final HotelOfferDto offer,
			final List<GuestDto> guests) {
		super(ui, navigateTo);
		
		this.bookingService = bookingService;
		this.offer = offer;
		this.guests = guests;
	}
	
	@Override
	protected void process() {
		final ReservationDto reservation = bookingService.bookHotelOffer(offer, guests);
		
		ui.getSession().setAttribute(SessionAttributes.RESERVATION.toString(), reservation);
	}
	
}
