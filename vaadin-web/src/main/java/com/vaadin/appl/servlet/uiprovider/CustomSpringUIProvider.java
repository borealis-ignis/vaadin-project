package com.vaadin.appl.servlet.uiprovider;

import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;

import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.server.SpringUIProvider;
import com.vaadin.ui.UI;

/**
 * @author Kastalski Sergey
 */
public class CustomSpringUIProvider extends SpringUIProvider {
	
	private static final long serialVersionUID = 5652893180962886442L;
	
	
	public CustomSpringUIProvider(final VaadinSession vaadinSession) {
		super(vaadinSession);
	}
	
	@Override
	protected Object findSpringViewDisplay(final UI ui) {
		try {
			final Map<String, Object> beans = getWebApplicationContext().getBeansWithAnnotation(SpringViewDisplay.class);
			
			final Class<?> clazz = ui.getPage().getUI().getClass();
			for (final Object bean : beans.values()) {
				if (bean.getClass().equals(clazz)) {
					return bean;
				}
			}
			
			throw new NoSuchBeanDefinitionException("Requested Page bean is not found. List of beans: " + beans.keySet());
		} catch (NoUniqueBeanDefinitionException e) {
			throw e;
		}
	}
	
}
