package com.vaadin.appl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySources({
	@PropertySource(value="classpath:application-config.properties", ignoreResourceNotFound=true),
	@PropertySource(value="file:${catalina.home}/conf/application-config.properties", ignoreResourceNotFound=true)
})
public class CommonConfiguration {
	
}
