package com.vaadin.appl.model.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
public class PaymentCardDto {
	
	private String cardNumber;
	
	private LocalDate expireDate;
	
	private String secureCode;
	
	private String cardHolder;
	
}
