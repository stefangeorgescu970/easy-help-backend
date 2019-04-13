package com.easyhelp.application.service.patient;

import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.repository.PatientRepository;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientServiceInterface {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<Patient> getAll() {
        return new ArrayList<>(patientRepository.findAll());
    }

    @Override
    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public Patient findById(Long patientId) throws EntityNotFoundException {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            return patient.get();
        } else {
            throw new EntityNotFoundException("Hospital with that id does not exist");
        }
    }
}
