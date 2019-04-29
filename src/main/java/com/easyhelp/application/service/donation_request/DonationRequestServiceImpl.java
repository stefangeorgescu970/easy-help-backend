package com.easyhelp.application.service.donation_request;

import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.blood.SeparatedBloodType;
import com.easyhelp.application.model.dto.requests.DonationRequestDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.requests.RequestStatus;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.repository.DonationRequestRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.service.separated_bloodtype.SeparatedBloodTypeServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DonationRequestServiceImpl implements DonationRequestServiceInterface {

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private DoctorServiceInterface doctorService;

    @Autowired
    private PatientServiceInterface patientService;

    @Autowired
    private SeparatedBloodTypeServiceInterface separatedBloodTypeService;

    @Autowired
    private BloodTypeServiceInterface bloodTypeService;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @Override
    public List<DonationRequest> getAll() {
        return new ArrayList<>(donationRequestRepository.findAll());
    }

    @Override
    public void requestDonation(DonationRequestDTO donationRequest) throws EntityNotFoundException, EntityAlreadyExistsException {
        DonationRequest request = new DonationRequest();
        Doctor doctor = doctorService.findById(donationRequest.getDoctorId());
        Patient patient = patientService.findById(donationRequest.getPatientId());

        if (getAllRequestsForDoctor(doctor.getId()).stream().anyMatch(r -> r.getPatient() == patient))
            throw new EntityAlreadyExistsException("You have already made a request for this patient");

        if (getAll().stream().anyMatch(r -> r.getPatient() == patient))
            throw new EntityAlreadyExistsException("There is already a request made for this patient");


        SeparatedBloodType separatedBloodType = separatedBloodTypeService.findSeparatedBloodTypeInDB(patient.getBloodType().getGroupLetter(), patient.getBloodType().getRh(), donationRequest.getBloodComponent());
        if (separatedBloodType == null) {
            separatedBloodType = new SeparatedBloodType();
            separatedBloodType.setComponent(donationRequest.getBloodComponent());
            BloodType bloodTypeInDb = bloodTypeService.findBloodTypeInDB(patient.getBloodType().getGroupLetter(), patient.getBloodType().getRh());
            separatedBloodType.setBloodType(bloodTypeInDb);
            Set<DonationRequest> donationRequests = new HashSet<>();
            donationRequests.add(request);
            separatedBloodType.setDonationRequests(donationRequests);
        } else {
            separatedBloodType.getDonationRequests().add(request);
        }

        separatedBloodTypeService.save(separatedBloodType);

        request.setDoctor(doctor);
        request.setPatient(patient);
        request.setUrgency(donationRequest.getUrgency());
        request.setStatus(RequestStatus.PROCESSING);
        request.setQuantity(donationRequest.getQuantity());
        request.setSeparatedBloodType(separatedBloodType);
        donationRequestRepository.save(request);
    }

    @Override
    public List<DonationRequest> getAllRequestsForDoctor(Long doctorId) {
        return donationRequestRepository.findAll()
                .stream()
                .filter(r -> r.getDoctor().getId().equals(doctorId))
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationRequest> getAllRequestsForPatient(Long patientId) {
        return donationRequestRepository.findAll()
                .stream()
                .filter(r -> r.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationRequest> getAllRequestsForDC(Long donationCenterId) throws EntityNotFoundException {

        DonationCenter donationCenter = donationCenterService.findById(donationCenterId);

        return donationRequestRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(e -> MiscUtils.computeDistance(donationCenter.getLatitude(), donationCenter.getLongitude(),
                        e.getDoctor().getHospital().getLatitude(), e.getDoctor().getHospital().getLongitude())))
                .collect(Collectors.toList());

    }
}
