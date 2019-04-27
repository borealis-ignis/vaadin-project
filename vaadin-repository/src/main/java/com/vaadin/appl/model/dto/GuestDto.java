package com.vaadin.appl.model.dto;

import java.time.LocalDate;

import com.vaadin.appl.model.enums.GuestType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
public class GuestDto {
	
	private Long id;
	
	private String title;
	
	private String givenName;
	
	private String surname;
	
	private LocalDate birthDate;
	
	private GuestType guestType;
	
}
