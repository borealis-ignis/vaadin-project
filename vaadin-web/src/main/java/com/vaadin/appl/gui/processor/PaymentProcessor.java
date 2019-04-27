package com.vaadin.appl.gui.processor;

import com.vaadin.appl.gui.session.SessionAttributes;
import com.vaadin.appl.model.dto.PaymentCardDto;
import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.appl.service.PaymentService;
import com.vaadin.ui.UI;

/**
 * @author Kastalski Sergey
 */
public class PaymentProcessor extends AbstractProcessor {
	
	private PaymentService paymentService;
	
	private PaymentCardDto paymentData;
	
	
	public PaymentProcessor(
			final PaymentService paymentService,
			final UI ui,
			final String navigateTo,
			final PaymentCardDto paymentData) {
		super(ui, navigateTo);
		
		this.paymentService = paymentService;
		this.paymentData = paymentData;
	}


	@Override
	protected void process() {
		final ReservationDto reservation = (ReservationDto) ui.getSession().getAttribute(SessionAttributes.RESERVATION.toString());
		final ReservationDto updatedReservation = paymentService.pay(paymentData, reservation);
		
		ui.getSession().setAttribute(SessionAttributes.RESERVATION.toString(), updatedReservation);
	}
	
	
}
