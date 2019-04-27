package com.vaadin.appl.gui.view.component;

import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.ui.Button;

/**
 * @author Kastalski Sergey
 */
public class Pagination extends com.vaadin.addon.pagination.Pagination {

	private static final long serialVersionUID = 5406751809257858570L;
	
	public Pagination() {
		super();
	}
	
	public Pagination(final PaginationResource paginationResource) {
		super(paginationResource);
	}
	
	public void loadFirstPage() {
		final Button first = getFirstButton();
		if (first.isEnabled()) {
			first.click();
		} else {
			first.setEnabled(true);
			first.click();
			first.setEnabled(false);
		}
	}
	
}
