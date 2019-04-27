package com.vaadin.appl.model.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
public class HotelsSearchCriteriaDto {
	
	private CityDto city;
	
	private LocalDate start;
	
	private LocalDate end;
	
	private Integer adultsCount;
	
	private Integer childrenCount;
	
	public HotelsSearchCriteriaDto(final CityDto city, final LocalDate start, final LocalDate end, final Integer adultsCount, final Integer childrenCount) {
		this.city = city;
		this.start = start;
		this.end = end;
		this.adultsCount = adultsCount;
		this.childrenCount =  childrenCount;
	}
	
	@Override
	public String toString() {
		return city.getName() + ", " + start + ", " + end + ", " + adultsCount + ", " + childrenCount;
	}
}
