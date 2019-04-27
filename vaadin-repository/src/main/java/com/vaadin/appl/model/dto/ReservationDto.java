package com.vaadin.appl.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.vaadin.appl.model.enums.ReservationStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
public class ReservationDto {
	
	private String id;
	
	private Long offerID;
	
	private String offerImage;
	
	private String hotelName;
	
	private String cityName;
	
	private String stars;
	
	private String offerName;
	
	private BigDecimal price;
	
	private String currency;
	
	private ReservationStatus status;
	
	private List<GuestDto> guests;
	
}
