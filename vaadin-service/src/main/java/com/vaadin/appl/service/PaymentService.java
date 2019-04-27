package com.vaadin.appl.service;

import org.springframework.stereotype.Service;

import com.vaadin.appl.model.dto.PaymentCardDto;
import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.appl.model.enums.ReservationStatus;
import com.vaadin.appl.repository.ReservationDAO;
import com.vaadin.appl.service.simulator.WebServiceSimulator;

/**
 * @author Kastalski Sergey
 */
@Service
public class PaymentService {
	
	private ReservationDAO reservationDAO;
	
	
	public PaymentService(final ReservationDAO reservationDAO) {
		this.reservationDAO = reservationDAO;
	}
	
	public ReservationDto pay(final PaymentCardDto paymentData, final ReservationDto reservation) {
		final Boolean resultOk = WebServiceSimulator.getInstance().sendPayRequest(paymentData);
		
		ReservationStatus status = ReservationStatus.REJECTED;
		if (resultOk) {
			status = ReservationStatus.CONFIRMED;
		}
		
		reservation.setStatus(status);
		
		reservationDAO.updateStatus(reservation.getStatus(), reservation.getId());
		
		return reservation;
	}
	
}
