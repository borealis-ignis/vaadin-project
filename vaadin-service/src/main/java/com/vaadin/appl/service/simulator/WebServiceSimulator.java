package com.vaadin.appl.service.simulator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.appl.model.dto.GuestDto;
import com.vaadin.appl.model.dto.HotelOfferDto;
import com.vaadin.appl.model.dto.HotelsSearchCriteriaDto;
import com.vaadin.appl.model.dto.PaymentCardDto;
import com.vaadin.appl.model.dto.ReservationDto;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author Kastalski Sergey
 */
public class WebServiceSimulator {
	
	private static volatile WebServiceSimulator instance;
	
	private WebServiceSimulator() {
		
	}
	
	public static WebServiceSimulator getInstance() {
		if (instance == null) {
			synchronized (WebServiceSimulator.class) {
				if (instance == null) {
					instance = new WebServiceSimulator();
				}
			}
		}
		return instance;
	}
	
	public List<HotelOfferDto> sendSearchRequest(final HotelsSearchCriteriaDto criteria) {
		final List<HotelOfferDto> result = new ArrayList<>();
		
		sleep(1400); // just to simulate long processing
		
		final Iterable<CSVRecord> records;
		try {
			final File csvFile = new File(IOUtils.resourceToURL("com/vaadin/appl/service/simulator/HotelOffers.csv", this.getClass().getClassLoader()).toURI());
			final Reader reader = new FileReader(csvFile);
			records = CSVFormat.RFC4180.withDelimiter('\t').withFirstRecordAsHeader().parse(reader);
		} catch (final IOException | URISyntaxException e) {
			e.printStackTrace();
			return result;
		}
		
		final BigDecimal nights = BigDecimal.valueOf(DAYS.between(criteria.getStart(), criteria.getEnd()));
		for (final CSVRecord record : records) {
			final BigDecimal pricePerNight = BigDecimal.valueOf(Double.parseDouble(record.get("price"))).setScale(2);
			
			final HotelOfferDto hotelOffer = new HotelOfferDto();
			hotelOffer.setOfferID(Long.parseLong(record.get("offerID")));
			hotelOffer.setCityName(criteria.getCity().getName());
			hotelOffer.setHotelName(record.get("hotelName"));
			hotelOffer.setStars(record.get("stars"));
			hotelOffer.setOfferImage(StringUtils.substringBefore(getImageUrl(record.get("offerImage")), ".jpg"));
			hotelOffer.setOfferName(record.get("offerName"));
			hotelOffer.setPrice(pricePerNight.multiply(nights));
			hotelOffer.setCurrency(record.get("currency"));
			
			result.add(hotelOffer);
		}
		
		return result;
	}
	
	public String sendBookRequest(final HotelOfferDto offer, final List<GuestDto> guests) {
		sleep(800); // just to simulate long processing
		
		return UUID.randomUUID().toString();
	}
	
	public boolean sendPayRequest(final PaymentCardDto paymentData) {
		sleep(500); // just to simulate long processing
		
		if (LocalDate.now().plus(30, ChronoUnit.DAYS).isAfter(paymentData.getExpireDate())) {
			return false;
		}
		
		return true;
	}
	
	public void sendCancelRequest(final ReservationDto reservation) {
		sleep(1500); // just to simulate long processing
	}
	
	private void sleep(final long sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private String getImageUrl(final String imageName) {
		final URI location = UI.getCurrent().getPage().getLocation();
		final String scheme = location.getScheme();
		final String authority = location.getAuthority();
		final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		
		return scheme + "://" + authority + "/" + contextPath + "image/" + imageName;
	}
	
}
