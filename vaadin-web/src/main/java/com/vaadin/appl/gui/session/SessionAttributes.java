package com.vaadin.appl.gui.session;

/**
 * @author Kastalski Sergey
 */
public enum SessionAttributes {
	
	HOTELS_SEARCH_CRITERIA("hotelsSearchCriteria"),
	NAVIGATE_TO("navigateTo"),
	OFFERS("offers"),
	OFFER_ID("offer_id"),
	GUESTS("guests"),
	RESERVATION("reservation"),
	PAYMENT_DATA("payment_data");
	
	private String value;
	
	private SessionAttributes(final String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
