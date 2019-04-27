package com.vaadin.appl.model.dbo;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vaadin.appl.model.enums.GuestType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
@Entity
@Table(name = "Guest")
public class GuestDbo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "givenName")
	private String givenName;
	
	@Column(name = "surname")
	private String surname;
	
	@Column(name = "birthDate")
	private LocalDate birthDate;
	
	@Column(name = "guestType")
	private GuestType guestType;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "reservationID", nullable = false)
	private ReservationDbo reservation;
	
}
