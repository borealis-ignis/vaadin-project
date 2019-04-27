package com.vaadin.appl.gui.view.component;

import java.util.Map;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;

/**
 * @author Kastalski Sergey
 */
public interface BookingFlowView extends View {
	
	void setTopButtons(Map<String, Button> topButtons);
	
}
