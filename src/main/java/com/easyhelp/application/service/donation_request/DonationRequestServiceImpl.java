package com.easyhelp.application.service.donation_request;

import com.easyhelp.application.model.dto.requests.DonationRequestDTO;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.repository.DonationRequestRepository;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationRequestServiceImpl implements DonationRequestServiceInterface {

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private DoctorServiceInterface doctorService;

    @Autowired
    private PatientServiceInterface patientService;

    @Override
    public List<DonationRequest> getAll() {
        return new ArrayList<>(donationRequestRepository.findAll());
    }

    @Override
    public void requestDonation(DonationRequestDTO donationRequest) throws EntityNotFoundException {
        DonationRequest request = new DonationRequest();
        Doctor doctor = doctorService.findById(donationRequest.getDoctorId());
        Patient patient = patientService.findById(donationRequest.getPatientId());
        request.setDoctor(doctor);
        request.setPatient(patient);
        request.setUrgency(donationRequest.getUrgency());
        request.setQuantity(donationRequest.getQuantity());
        donationRequestRepository.save(request);
    }

    @Override
    public List<DonationRequest> getAllRequestsForDoctor(Long doctorId) {
        return donationRequestRepository.findAll()
                .stream()
                .filter(r -> r.getDoctor().getId() == doctorId)
                .collect(Collectors.toList());
    }
}
