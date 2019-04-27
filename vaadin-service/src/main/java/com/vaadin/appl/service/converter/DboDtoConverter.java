package com.vaadin.appl.service.converter;

import java.util.List;

/**
 * @author Kastalski Sergey
 */
public interface DboDtoConverter<B, T> {
	
	B convertToDbo(T dto);
	
	T convertToDto(B dbo);
	
	List<B> convertToDbo(List<T> dtoList);
	
	List<T> convertToDto(List<B> dboList);
	
}
