package com.easyhelp.application.service.patient;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.PatientRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientServiceInterface {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BloodTypeServiceInterface bloodTypeService;

    @Autowired
    private DoctorServiceInterface doctorService;

    @Override
    public List<Patient> getAll() {
        return new ArrayList<>(patientRepository.findAll());
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
    public void addPatient(Long doctorId, String ssn, String groupLetter, Boolean rh) throws EntityNotFoundException {

        Doctor doctor = doctorService.findById(doctorId);
        Patient patient = new Patient();
        patient.setSsn(ssn);
        patient.setDoctor(doctor);

        BloodType bloodTypeInDB = bloodTypeService.findBloodTypeInDB(groupLetter, rh);
        if (bloodTypeInDB == null) {
            BloodType newBloodType = new BloodType(groupLetter, rh);
            Set<Patient> patients = new HashSet<>();
            patients.add(patient);
            newBloodType.setPatients(patients);
            patient.setBloodType(newBloodType);
            bloodTypeService.saveBloodType(newBloodType);
        }
        else {
            if (bloodTypeInDB.getPatients() == null)
                bloodTypeInDB.setPatients(new HashSet<>());
            bloodTypeInDB.getPatients().add(patient);
            patient.setBloodType(bloodTypeInDB);
            bloodTypeService.saveBloodType(bloodTypeInDB);
        }
        patientRepository.save(patient);
    }

    @Override
    public List<Patient> getPatientsForDoctor(Long doctorId) {
        return patientRepository.findAll()
                .stream()
                .filter(p -> p.getDoctor().getId().equals(doctorId))
                .collect(Collectors.toList());
    }
}
