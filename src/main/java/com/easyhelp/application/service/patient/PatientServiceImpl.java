package com.easyhelp.application.service.patient;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.repository.DonorRepository;
import com.easyhelp.application.repository.PatientRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
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

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonationRequestServiceInterface donationRequestService;

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
    public void addPatient(Long doctorId, String ssn, String groupLetter, Boolean rh) throws EntityNotFoundException, EntityAlreadyExistsException {

        if (patientRepository.findBySsn(ssn) != null)
            throw new EntityAlreadyExistsException("Patient with this ssn was already added.");

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
        } else {
            if (bloodTypeInDB.getPatients() == null)
                bloodTypeInDB.setPatients(new HashSet<>());
            patient.setBloodType(bloodTypeInDB);
            bloodTypeInDB.getPatients().add(patient);
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

    @Override
    public void deletePatient(Long patientId) throws EasyHelpException {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);

        if (!patientOptional.isPresent())
            throw new EntityNotFoundException("Patient with this id does not exist.");

        Patient patient = patientOptional.get();
        if (patient.getDonationRequests() != null && !patient.getDonationRequests().isEmpty())
            throw new EasyHelpException("This patient still has donation requests ongoing");

        patient.getDoctor().getPatients().remove(patient);
        patient.getDonations().forEach(donation -> donation.setPatient(null));
        patientRepository.delete(patient);
    }

    @Override
    public Patient findBySSN(String ssn) throws EntityNotFoundException {
        Patient patient = patientRepository.findBySsn(ssn);

        if (patient == null)
            throw new EntityNotFoundException("Patient with this ssn does not exist.");

        return patient;
    }

    @Override
    public void save(Patient patient) {
        patientRepository.save(patient);
    }
}
