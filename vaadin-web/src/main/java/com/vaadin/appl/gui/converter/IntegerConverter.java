package com.vaadin.appl.gui.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

/**
 * @author Kastalski Sergey
 */
@Service
public class IntegerConverter implements Converter<String, Integer> {
	
	private static final long serialVersionUID = 5703752806461246506L;

	@Override
	public Result<Integer> convertToModel(final String value, final ValueContext context) {
		if (StringUtils.isBlank(value)) {
			return Result.ok(null);
		}
		return Result.ok(Integer.valueOf(value));
	}

	@Override
	public String convertToPresentation(final Integer value, final ValueContext context) {
		if (value == null) {
			return "";
		}
		return value.toString();
	}
	
}
