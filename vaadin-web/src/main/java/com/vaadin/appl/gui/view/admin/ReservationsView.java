package com.vaadin.appl.gui.view.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.annotations.Push;
import com.vaadin.appl.gui.page.ReservationsPage;
import com.vaadin.appl.gui.processor.CancellationProcessor;
import com.vaadin.appl.model.dto.GuestDto;
import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.appl.model.enums.ReservationStatus;
import com.vaadin.appl.service.BookingService;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Kastalski Sergey
 */
@Service
@SpringView(name = ReservationsView.NAME, ui = ReservationsPage.class)
@Push
public class ReservationsView extends HorizontalLayout implements View {
	
	private static final long serialVersionUID = 6444394183304588404L;
	
	public final static String NAME = "";
	
	@Autowired
	private BookingService bookingService;
	
	private Grid<ReservationDto> grid = new Grid<>();
	
	private List<ReservationDto> reservations;
	
	
	public ReservationsView() {
		Page.getCurrent().setTitle("Reservations");
	}
	
	@Override
	public void enter(final ViewChangeEvent event) {
		reservations = bookingService.getReservations();
		
		final VerticalLayout dataPod = new VerticalLayout();
		dataPod.setWidth(400, Unit.PIXELS);
		
		final ListDataProvider<ReservationDto> dataProvider = new ListDataProvider<>(reservations);
		
		grid.setWidth(900, Unit.PIXELS);
		grid.setDataProvider(dataProvider);
		grid.addColumn(ReservationDto::getId).setCaption("Reservation ID");
		grid.addColumn(reservation -> {
			final GuestDto guest = reservation.getGuests().get(0);
			return guest.getTitle() + " " + guest.getGivenName() + " " + guest.getSurname();
		}).setCaption("Tourlead");
		grid.addColumn(ReservationDto::getStatus).setStyleGenerator(item -> {
			String colorStyle = "";
			if (ReservationStatus.CANCELLED == item.getStatus()) {
				colorStyle = " red";
			} else if (ReservationStatus.CONFIRMED == item.getStatus()) {
				colorStyle = " green";
			} else if (ReservationStatus.BOOKED == item.getStatus()) {
				colorStyle = " grey";
			}
			return "bold" + colorStyle;
		}).setCaption("Status");
		grid.addComponentColumn(this::buildCancelButton);
		
		grid.addItemClickListener(clickItemEvent -> {
			final ReservationDto reservation = clickItemEvent.getItem();
			
			dataPod.removeAllComponents();
			
			final StringBuilder content = new StringBuilder();
			content.append("<div style='float:left;'>");
			content.append("<img src='");
			content.append(reservation.getOfferImage());
			content.append("' style='width:100px; height:90px; margin-right: 10px;' />");
			content.append("</div>");
			content.append("<div style='float:left; width: 100px;'>");
			content.append("<span style='font-weight:bold;'>");
			content.append(reservation.getHotelName() + ", " + reservation.getCityName());
			content.append("</span>");
			content.append("<br/>");
			content.append("<span>");
			content.append("Stars: " + reservation.getStars());
			content.append("</span>");
			content.append("<br/>");
			content.append("<span>");
			content.append(reservation.getOfferName());
			content.append("</span>");
			content.append("<br/>");
			content.append("<span>");
			content.append(reservation.getPrice() + " " + reservation.getCurrency());
			content.append("</span>");
			content.append("</div>");
			
			final Label itemContainer = new Label();
			itemContainer.setCaptionAsHtml(true);
			itemContainer.setCaption(content.toString());
			
			dataPod.addComponent(new Label("Current reservation:"));
			dataPod.addComponent(itemContainer);
		});
		
		addComponent(grid);
		addComponent(dataPod);
	}
	
	private VerticalLayout buildCancelButton(final ReservationDto reservation) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		layout.setSizeFull();
		
		final Button cancelButton = new Button("Cancel");
		cancelButton.setHeight(26, Unit.PIXELS);
		if (ReservationStatus.CONFIRMED != reservation.getStatus()) {
			cancelButton.setVisible(false);
		}
		cancelButton.addClickListener(listener -> {
			cancelButton.setVisible(false);
			
			final ProgressBar spinner = new ProgressBar();
			spinner.setIndeterminate(true);
			layout.addComponent(spinner);
			layout.setComponentAlignment(spinner, Alignment.MIDDLE_CENTER);
			
			getUI().setPollInterval(500);
			
			new CancellationProcessor(getUI(), bookingService, reservation, grid, spinner, cancelButton).start();
		});
		
		layout.addComponent(cancelButton);
		layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
		
		return layout;
	}
	
}
