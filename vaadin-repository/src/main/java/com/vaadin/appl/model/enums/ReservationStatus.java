package com.vaadin.appl.model.enums;

/**
 * @author Kastalski Sergey
 */
public enum ReservationStatus {
	CONFIRMED("confirmed"),
	BOOKED("booked"),
	REJECTED("rejected"),
	CANCELLED("cancelled");
	
	private String value;
	
	private ReservationStatus(final String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
