package com.vaadin.appl.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vaadin.appl.converter.DtoDboConverter;
import com.vaadin.appl.model.dbo.GuestDbo;
import com.vaadin.appl.model.dbo.ReservationDbo;
import com.vaadin.appl.model.dto.ReservationDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class ReservationConverter implements DtoDboConverter<ReservationDto, ReservationDbo>  {
	
	private GuestConverter guestConverter;
	
	public ReservationConverter(final GuestConverter guestConverter) {
		this.guestConverter = guestConverter;
	}
	
	@Override
	public ReservationDto convertDboToDto(final ReservationDbo dbo) {
		final ReservationDto dto = new ReservationDto();
		dto.setId(dbo.getId());
		dto.setOfferID(dbo.getOfferID());
		dto.setCityName(dbo.getCityName());
		dto.setHotelName(dbo.getHotelName());
		dto.setOfferName(dbo.getOfferName());
		dto.setOfferImage(dbo.getOfferImage());
		dto.setStars(dbo.getStars());
		dto.setPrice(dbo.getPrice());
		dto.setCurrency(dbo.getCurrency());
		dto.setStatus(dbo.getStatus());
		dto.setGuests(guestConverter.convertDboToDto(dbo.getGuests()));
		return dto;
	}

	@Override
	public ReservationDbo convertDtoToDbo(final ReservationDto dto) {
		final ReservationDbo dbo = new ReservationDbo();
		dbo.setId(dto.getId());
		dbo.setOfferID(dto.getOfferID());
		dbo.setCityName(dto.getCityName());
		dbo.setHotelName(dto.getHotelName());
		dbo.setOfferName(dto.getOfferName());
		dbo.setOfferImage(dto.getOfferImage());
		dbo.setStars(dto.getStars());
		dbo.setPrice(dto.getPrice());
		dbo.setCurrency(dto.getCurrency());
		dbo.setStatus(dto.getStatus());
		final List<GuestDbo> guests = guestConverter.convertDtoToDbo(dto.getGuests());
		guests.forEach(guest -> guest.setReservation(dbo));
		dbo.setGuests(guests);
		return dbo;
	}

	@Override
	public List<ReservationDto> convertDboToDto(final List<ReservationDbo> dboList) {
		final List<ReservationDto> dtos = new ArrayList<>();
		dboList.forEach(dbo -> dtos.add(convertDboToDto(dbo)));
		return dtos;
	}

	@Override
	public List<ReservationDbo> convertDtoToDbo(final List<ReservationDto> dtoList) {
		final List<ReservationDbo> dbos = new ArrayList<>();
		dtoList.forEach(dto -> dbos.add(convertDtoToDbo(dto)));
		return dbos;
	}

}
