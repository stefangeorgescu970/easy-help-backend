package com.easyhelp.application.service.patient;

import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface PatientServiceInterface {
    List<Patient> getAll();

    void save(Patient patient);

    Patient findById(Long patientId) throws EntityNotFoundException;

    void updateBloodGroupOnPatient(Long patient, String groupLetter, Boolean rh) throws EntityNotFoundException;

}
