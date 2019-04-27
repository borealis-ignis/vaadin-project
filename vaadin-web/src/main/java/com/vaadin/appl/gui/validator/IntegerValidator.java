package com.vaadin.appl.gui.validator;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.AbstractValidator;

/**
 * @author Kastalski Sergey
 */
public class IntegerValidator extends AbstractValidator<String> {
	
	private int from = 0;
	
	public IntegerValidator(final String errorMessage, final int from) {
		super(errorMessage);
	}

	private static final long serialVersionUID = -741518787595145939L;
	
	@Override
	public ValidationResult apply(final String value, final ValueContext context) {
		return toResult(value, isValid(value));
	}

	private boolean isValid(final String value) {
		if (StringUtils.isBlank(value) || value.length() > 1) {
			return false;
		}
		return value.matches("^[" + from + "-9]$");
	}

}
