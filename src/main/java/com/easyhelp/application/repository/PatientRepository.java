package com.easyhelp.application.repository;

import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.model.requests.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findBySsn(String ssn);
}

