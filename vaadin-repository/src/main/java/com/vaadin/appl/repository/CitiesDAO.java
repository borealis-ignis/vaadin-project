package com.vaadin.appl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vaadin.appl.model.dbo.CityDbo;

/**
 * @author Kastalski Sergey
 */
@Repository
public interface CitiesDAO extends JpaRepository<CityDbo, Long> {

}
