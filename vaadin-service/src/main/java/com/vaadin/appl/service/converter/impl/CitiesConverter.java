package com.vaadin.appl.service.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vaadin.appl.model.dbo.CityDbo;
import com.vaadin.appl.model.dto.CityDto;
import com.vaadin.appl.service.converter.DboDtoConverter;

/**
 * @author Kastalski Sergey
 */
@Service
public class CitiesConverter implements DboDtoConverter<CityDbo, CityDto> {
	
	@Override
	public CityDbo convertToDbo(final CityDto dto) {
		final CityDbo dbo = new CityDbo();
		dbo.setId(dto.getId());
		dbo.setName(dto.getName());
		return dbo;
	}
	
	@Override
	public CityDto convertToDto(final CityDbo dbo) {
		final CityDto dto = new CityDto();
		dto.setId(dbo.getId());
		dto.setName(dbo.getName());
		return dto;
	}

	@Override
	public List<CityDbo> convertToDbo(final List<CityDto> dtoList) {
		final List<CityDbo> listDbo = new ArrayList<>();
		dtoList.forEach(dto -> {
			listDbo.add(convertToDbo(dto));
		});
		return listDbo;
	}

	@Override
	public List<CityDto> convertToDto(final List<CityDbo> dboList) {
		final List<CityDto> listDto = new ArrayList<>();
		dboList.forEach(dbo -> {
			listDto.add(convertToDto(dbo));
		});
		return listDto;
	}
	
}
