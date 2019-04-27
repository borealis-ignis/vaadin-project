package com.vaadin.appl.model.dbo;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.vaadin.appl.model.enums.ReservationStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
@Entity
@Table(name = "Reservation", indexes = {@Index(columnList = "ID", unique = true)})
public class ReservationDbo {
	
	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "offerID")
	private Long offerID;
	
	@Column(name = "offerImage")
	private String offerImage;
	
	@Column(name = "hotelName")
	private String hotelName;
	
	@Column(name = "cityName")
	private String cityName;
	
	@Column(name = "stars")
	private String stars;
	
	@Column(name = "offerName")
	private String offerName;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "status")
	private ReservationStatus status;
	
	@OneToMany(mappedBy = "reservation", orphanRemoval = true, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
	private List<GuestDbo> guests;
	
}
