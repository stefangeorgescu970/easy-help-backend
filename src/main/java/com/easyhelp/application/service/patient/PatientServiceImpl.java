package com.easyhelp.application.service.patient;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.PatientRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PatientServiceImpl implements PatientServiceInterface {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BloodTypeServiceInterface bloodTypeService;

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
            throw new EntityNotFoundException("Patient with that id does not exist");
        }
    }

    @Override
    public void updateBloodGroupOnPatient(Long patientId, String groupLetter, Boolean rh) throws EntityNotFoundException {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);

        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            BloodType bloodTypeInDB = bloodTypeService.findBloodTypeInDB(groupLetter, rh);
            if (bloodTypeInDB == null) {
                BloodType newBloodType = new BloodType(groupLetter, rh);
                Set<Patient> patients = new HashSet<>();
                patients.add(patient);
                newBloodType.setPatients(patients);
                patient.setBloodType(newBloodType);
                bloodTypeService.saveBloodType(newBloodType);
            } else {
                patient.setBloodType(bloodTypeInDB);
                if (bloodTypeInDB.getPatients() == null)
                    bloodTypeInDB.setPatients(new HashSet<>());
                bloodTypeInDB.getPatients().add(patient);
                bloodTypeService.saveBloodType(bloodTypeInDB);
            }
            patientRepository.save(patient);
        } else {
            throw new EntityNotFoundException("No patient was found with provided id.");
        }
    }
}
