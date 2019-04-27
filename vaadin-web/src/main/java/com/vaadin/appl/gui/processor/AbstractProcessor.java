package com.vaadin.appl.gui.processor;

import com.vaadin.ui.UI;

/**
 * @author Kastalski Sergey
 */
public abstract class AbstractProcessor extends Thread {
	
	protected UI ui;
	
	private String navigateTo;
	
	public AbstractProcessor(
			final UI ui,
			final String navigateTo) {
		this.ui = ui;
		this.navigateTo = navigateTo;
	}
	
	protected abstract void process();
	
	@Override
	public void run() {
		process();
		
		ui.access(() -> {
			ui.getNavigator().navigateTo(navigateTo);
		});
	}
	
}
