package com.easyhelp.application.repository;

import com.easyhelp.application.model.locations.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
