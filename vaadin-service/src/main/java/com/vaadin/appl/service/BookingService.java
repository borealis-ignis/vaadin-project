package com.vaadin.appl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vaadin.appl.converter.impl.GuestConverter;
import com.vaadin.appl.converter.impl.ReservationConverter;
import com.vaadin.appl.model.dbo.GuestDbo;
import com.vaadin.appl.model.dbo.ReservationDbo;
import com.vaadin.appl.model.dto.GuestDto;
import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.appl.model.enums.ReservationStatus;
import com.vaadin.appl.repository.ReservationDAO;
import com.vaadin.appl.service.simulator.WebServiceSimulator;

/**
 * @author Kastalski Sergey
 */
@Service
public class BookingService {
	
	private ReservationDAO reservationDAO;
	
	private GuestConverter guestConverter;
	
	private ReservationConverter reservationConverter;
	
	
	public BookingService(final ReservationDAO reservationDAO, final GuestConverter guestConverter, final ReservationConverter reservationConverter) {
		this.reservationDAO = reservationDAO;
		this.guestConverter = guestConverter;
		this.reservationConverter = reservationConverter;
	}
	
	public ReservationDto bookHotelOffer(final HotelOfferDto offer, final List<GuestDto> guests) {
		final String reservationID = WebServiceSimulator.getInstance().sendBookRequest(offer, guests);
		
		final ReservationDbo reservation = new ReservationDbo();
		
		final List<GuestDbo> guestsList = guestConverter.convertDtoToDbo(guests);
		guestsList.forEach(guest -> guest.setReservation(reservation));
		
		reservation.setId(reservationID);
		reservation.setOfferID(offer.getOfferID());
		reservation.setCityName(offer.getCityName());
		reservation.setHotelName(offer.getHotelName());
		reservation.setOfferName(offer.getOfferName());
		reservation.setOfferImage(offer.getOfferImage());
		reservation.setStars(offer.getStars());
		reservation.setPrice(offer.getPrice());
		reservation.setCurrency(offer.getCurrency());
		reservation.setGuests(guestsList);
		reservation.setStatus(ReservationStatus.BOOKED);
		
		return reservationConverter.convertDboToDto(reservationDAO.saveAndFlush(reservation));
	}
	
	public List<ReservationDto> getReservations() {
		return reservationConverter.convertDboToDto(reservationDAO.findAll());
	}
	
	public boolean cancelReservation(final ReservationDto reservation) {
		WebServiceSimulator.getInstance().sendCancelRequest(reservation);
		
		if (ReservationStatus.CONFIRMED == reservation.getStatus()) {
			reservation.setStatus(ReservationStatus.CANCELLED);
		} else {
			return false;
		}
		
		final ReservationDbo reservationDbo = reservationConverter.convertDtoToDbo(reservation);
		reservationDAO.saveAndFlush(reservationDbo);
		
		return true;
	}
	
}
