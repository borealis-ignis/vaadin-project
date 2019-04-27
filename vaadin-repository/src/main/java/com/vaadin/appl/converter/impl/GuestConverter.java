package com.vaadin.appl.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vaadin.appl.converter.DtoDboConverter;
import com.vaadin.appl.model.dbo.GuestDbo;
import com.vaadin.appl.model.dto.GuestDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class GuestConverter implements DtoDboConverter<GuestDto, GuestDbo> {

	@Override
	public GuestDto convertDboToDto(final GuestDbo dbo) {
		final GuestDto dto = new GuestDto();
		dto.setId(dbo.getId());
		dto.setTitle(dbo.getTitle());
		dto.setGivenName(dbo.getGivenName());
		dto.setSurname(dbo.getSurname());
		dto.setBirthDate(dbo.getBirthDate());
		dto.setGuestType(dbo.getGuestType());
		return dto;
	}

	@Override
	public GuestDbo convertDtoToDbo(final GuestDto dto) {
		final GuestDbo dbo = new GuestDbo();
		dbo.setId(dto.getId());
		dbo.setTitle(dto.getTitle());
		dbo.setGivenName(dto.getGivenName());
		dbo.setSurname(dto.getSurname());
		dbo.setBirthDate(dto.getBirthDate());
		dbo.setGuestType(dto.getGuestType());
		return dbo;
	}

	@Override
	public List<GuestDto> convertDboToDto(final List<GuestDbo> dboList) {
		final List<GuestDto> dtos = new ArrayList<>();
		dboList.forEach(dbo -> dtos.add(convertDboToDto(dbo)));
		return dtos;
	}

	@Override
	public List<GuestDbo> convertDtoToDbo(final List<GuestDto> dtoList) {
		final List<GuestDbo> dbos = new ArrayList<>();
		dtoList.forEach(dto -> dbos.add(convertDtoToDbo(dto)));
		return dbos;
	}
	
}
