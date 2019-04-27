package com.vaadin.appl.gui.processor;

import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.appl.service.BookingService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;

/**
 * @author Kastalski Sergey
 */
public class CancellationProcessor extends Thread {
	
	private UI ui;
	
	private BookingService bookingService;
	
	private ReservationDto reservation;
	
	private Grid<ReservationDto> grid;
	
	private ProgressBar spinner;
	
	private Button cancelButton;
	
	
	public CancellationProcessor(
			final UI ui,
			final BookingService bookingService,
			final ReservationDto reservation,
			final Grid<ReservationDto> grid,
			final ProgressBar spinner,
			final Button cancelButton) {
		this.ui = ui;
		this.bookingService = bookingService;
		this.reservation = reservation;
		this.grid = grid;
		this.spinner = spinner;
		this.cancelButton = cancelButton;
	}
	
	@Override
	public void run() {
		ui.access(() -> {
			if (bookingService.cancelReservation(reservation)) {
				grid.getDataProvider().refreshAll();
			} else {
				cancelButton.setVisible(true);
			}
			
			ui.setPollInterval(-1);
			spinner.setVisible(false);
		});
	}
	
}
