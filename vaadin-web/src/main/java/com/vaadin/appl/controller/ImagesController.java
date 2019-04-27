package com.vaadin.appl.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kastalski Sergey
 */
@Controller
public class ImagesController {
	
	@GetMapping(value = "/image/{image}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getSimulatorImage(@PathVariable(value = "image") final String image) throws IOException {
	    final InputStream in = getClass().getResourceAsStream("/com/vaadin/appl/service/simulator/images/" + image + ".jpg");
	    if (in == null) {
	    	return null;
	    }
	    return IOUtils.toByteArray(in);
	}
	
}
