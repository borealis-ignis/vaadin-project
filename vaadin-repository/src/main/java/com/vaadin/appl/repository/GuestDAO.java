package com.vaadin.appl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.appl.model.dbo.GuestDbo;

/**
 * @author Kastalski Sergey
 */
@Service
public interface GuestDAO extends JpaRepository<GuestDbo, Long> {
	
}
