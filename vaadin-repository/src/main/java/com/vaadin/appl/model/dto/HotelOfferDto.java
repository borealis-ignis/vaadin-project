package com.vaadin.appl.model.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
public class HotelOfferDto {
	
	private Long offerID;
	
	private String offerImage;
	
	private String hotelName;
	
	private String cityName;
	
	private String stars;
	
	private String offerName;
	
	private BigDecimal price;
	
	private String currency;
	
	@Override
	public String toString() {
		return offerID + " - " + offerImage + ", " + hotelName + ", " + cityName + ", " + stars + ", " + offerName + ", " + price + " " + currency;
	}
	
}
