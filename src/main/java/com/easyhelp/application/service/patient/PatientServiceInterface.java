package com.easyhelp.application.service.patient;

import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface PatientServiceInterface {
    List<Patient> getAll();

    Patient findById(Long patientId) throws EntityNotFoundException;

    void addPatient(Long doctorId, String ssn, String groupLetter, Boolean rh) throws EntityNotFoundException, EntityAlreadyExistsException;

    List<Patient> getPatientsForDoctor(Long doctorId);

    void deletePatient(Long patientId) throws EntityNotFoundException, EasyHelpException;
}
