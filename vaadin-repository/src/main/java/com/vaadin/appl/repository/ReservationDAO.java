package com.vaadin.appl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.appl.model.dbo.ReservationDbo;
import com.vaadin.appl.model.enums.ReservationStatus;

/**
 * @author Kastalski Sergey
 */
@Service
public interface ReservationDAO extends JpaRepository<ReservationDbo, String> {
	
	@Modifying
	@Transactional
	@Query(value = "update ReservationDbo r set r.status = :status where r.id = :reservationId")
	void updateStatus(@Param("status") final ReservationStatus status, @Param("reservationId") final String reservationId);
	
}
