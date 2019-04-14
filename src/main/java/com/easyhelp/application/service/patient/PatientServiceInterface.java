package com.easyhelp.application.service.patient;

import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface PatientServiceInterface {
    List<Patient> getAll();


    Patient findById(Long patientId) throws EntityNotFoundException;


    void addPatient(Long doctorId, String ssn, String groupLetter, Boolean rh) throws EntityNotFoundException;

    List<Patient> getPatientsForDoctor(Long doctorId);
}
