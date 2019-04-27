package com.vaadin.appl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vaadin.appl.model.dto.CityDto;
import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.appl.repository.CitiesDAO;
import com.vaadin.appl.service.converter.impl.CitiesConverter;
import com.vaadin.appl.service.simulator.WebServiceSimulator;

/**
 * @author Kastalski Sergey
 */
@Service
public class SearchService {
	
	private CitiesDAO citiesDAO;
	
	private CitiesConverter citiesConverter;
	
	public SearchService(final CitiesDAO citiesDAO, final CitiesConverter citiesConverter) {
		this.citiesDAO = citiesDAO;
		this.citiesConverter = citiesConverter;
	}
	
	public List<CityDto> getCities() {
		return citiesConverter.convertToDto(citiesDAO.findAll());
	}
	
	public List<HotelOfferDto> getHotelOffers(final HotelsSearchCriteriaDto criteria) {
		return WebServiceSimulator.getInstance().sendSearchRequest(criteria);
	}
	
}
