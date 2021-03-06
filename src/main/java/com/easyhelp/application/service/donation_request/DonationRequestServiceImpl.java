package com.easyhelp.application.service.donation_request;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.blood.SeparatedBloodType;
import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.dto.dcp.incoming.DonationCommitmentCreateDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.*;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.DonationRequestRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donation_commitment.DonationCommitmentServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.service.separated_bloodtype.SeparatedBloodTypeServiceInterface;
import com.easyhelp.application.service.stored_blood.StoredBloodServiceInterface;
import com.easyhelp.application.utils.BloodUtils;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
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

    @Autowired
    private StoredBloodServiceInterface storedBloodService;

    @Autowired
    private DonationCommitmentServiceInterface donationCommitmentService;

    @Override
    public List<DonationRequest> getAll() {
        return new ArrayList<>(donationRequestRepository.findAll());
    }

    @Override
    public DonationRequest requestDonation(Long doctorId, Long patientId, Double quantity, RequestUrgency urgency, BloodComponent bloodComponent) throws EntityNotFoundException, EntityAlreadyExistsException {
        DonationRequest request = new DonationRequest();
        Doctor doctor = doctorService.findById(doctorId);
        Patient patient = patientService.findById(patientId);

        if (getAllRequestsForDoctor(doctor.getId()).stream().anyMatch(r -> r.getPatient() == patient))
            throw new EntityAlreadyExistsException("You have already made a request for this patient");

        if (getAll().stream().anyMatch(r -> r.getPatient() == patient))
            throw new EntityAlreadyExistsException("There is already a request made for this patient");


        SeparatedBloodType separatedBloodType = separatedBloodTypeService.findSeparatedBloodTypeInDB(patient.getBloodType().getGroupLetter(), patient.getBloodType().getRh(), bloodComponent);
        if (separatedBloodType == null) {
            separatedBloodType = new SeparatedBloodType();
            separatedBloodType.setComponent(bloodComponent);
            BloodType bloodTypeInDb = bloodTypeService.findBloodTypeInDB(patient.getBloodType().getGroupLetter(), patient.getBloodType().getRh());
            separatedBloodType.setBloodType(bloodTypeInDb);
            Set<DonationRequest> donationRequests = new HashSet<>();
            donationRequests.add(request);
            separatedBloodType.setDonationRequests(donationRequests);

            bloodTypeInDb.getSeparatedBloodTypes().add(separatedBloodType);
            bloodTypeService.saveBloodType(bloodTypeInDb);
        } else {
            separatedBloodType.getDonationRequests().add(request);
        }

        request.setDoctor(doctor);
        request.setPatient(patient);
        request.setUrgency(urgency);
        request.setStatus(RequestStatus.PROCESSING);
        request.setQuantity(quantity);
        request.setSeparatedBloodType(separatedBloodType);

        separatedBloodTypeService.save(separatedBloodType);
        return donationRequestRepository.save(request);
    }

    @Override
    public void markRequestAsFinished(DonationRequest donationRequest, DonationCommitment finalCommitment) {
        donationRequest.getDonationCommitments().forEach(donationCommitment -> {
            if (!donationCommitment.equals(finalCommitment)) {
                // TODO - notify dc that the request was solved and that their blood is not needed
                donationCommitment.setStatus(DonationCommitmentStatus.UNFUFILLED);
                donationCommitmentService.save(donationCommitment);
            }
        });

        donationRequest.setStatus(RequestStatus.COMPLETED);
        donationRequestRepository.save(donationRequest);
    }

    @Override
    public DonationRequest saveRequest(DonationRequest donationRequest) {
        return donationRequestRepository.save(donationRequest);
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
    public DonationCommitment commitToDonation(DonationCommitmentCreateDTO donationCommitmentCreateDTO) throws EasyHelpException {
        DonationCenter donationCenter = donationCenterService.findById(donationCommitmentCreateDTO.getDonationCenterId());
        Optional<DonationRequest> donationRequestOpt = donationRequestRepository.findById(donationCommitmentCreateDTO.getDonationRequestId());
        StoredBlood storedBlood = storedBloodService.findById(donationCommitmentCreateDTO.getStoredBloodId());

        if (!donationRequestOpt.isPresent()) {
            throw new EntityNotFoundException("Donation request with that id not there");
        }

        DonationRequest donationRequest = donationRequestOpt.get();

        if(!donationCenter.getStoredBloodSet().contains(storedBlood)) {
            throw new EasyHelpException("Stored blood with that id is not in that donation center.");
        }

        DonationCommitment donationCommitment = new DonationCommitment();
        donationCommitment.setDonationCenter(donationCenter);
        donationCommitment.setDonationRequest(donationRequest);
        donationCommitment.setStoredBlood(storedBlood);
        donationCommitment.setStatus(DonationCommitmentStatus.COMMITTED_BY_DONATION_CENTER);

        donationCenter.getDonationCommitments().add(donationCommitment);
        donationRequest.getDonationCommitments().add(donationCommitment);
        storedBlood.setDonationCommitment(donationCommitment);
        storedBlood.setIsUsable(false);

        DonationCommitment donationCommitment1 = donationCommitmentService.save(donationCommitment);
        donationCenterService.save(donationCenter);
        donationRequestRepository.save(donationRequest);
        storedBloodService.storeBlood(storedBlood);
        return donationCommitment1;
    }

    @Override
    public void cancelRequest(Long requestId) throws EasyHelpException {
        Optional<DonationRequest> donationRequestOpt = donationRequestRepository.findById(requestId);

        if (!donationRequestOpt.isPresent()) {
            throw new EntityNotFoundException("Donation request with that id not there");
        }

        DonationRequest donationRequest = donationRequestOpt.get();

        if (!donationRequest.getDonationCommitments().isEmpty()) {
            throw new EasyHelpException("This donation has pending commitments. Please handle these first");
        }

        donationRequest.getDoctor().getDonationRequests().remove(donationRequest);
        donationRequest.getSeparatedBloodType().getDonationRequests().remove(donationRequest);
        donationRequest.getPatient().getDonationRequests().remove(donationRequest);

        donationRequestRepository.delete(donationRequest);
    }

    @Override
    public List<DonationRequest> getDonationRequestsDonorCouldDonateFor(Donor donor) {
        return donationRequestRepository
                .findAll()
                .stream()
                .filter(donationRequest -> BloodUtils.checkBloodCompatibility(donor, donationRequest.getPatient(), donationRequest.getSeparatedBloodType().getComponent()))
                .collect(Collectors.toList());
    }

    @Override
    public DonationRequest findById(Long donationRequestId) throws EntityNotFoundException {
        Optional<DonationRequest> donationRequest = donationRequestRepository.findById(donationRequestId);

        if (!donationRequest.isPresent()) {
            throw new EntityNotFoundException("Donation request with that id does not exist");
        }

        return donationRequest.get();
    }

    @Override
    public List<DonationRequest> getAllRequestsForDC(Long donationCenterId) throws EntityNotFoundException {

        DonationCenter donationCenter = donationCenterService.findById(donationCenterId);

        return donationRequestRepository.findAll()
                .stream()
                .filter(donationRequest -> !(donationRequest.getStatus().equals(RequestStatus.FULLY_COMMITTED_TO) || donationRequest.getStatus().equals(RequestStatus.COMPLETED) || donationRequest.getStatus().equals(RequestStatus.FAILED)))
                .sorted(Comparator.comparingDouble(e -> MiscUtils.computeDistance(donationCenter.getLatitude(), donationCenter.getLongitude(),
                        e.getDoctor().getHospital().getLatitude(), e.getDoctor().getHospital().getLongitude())))
                .collect(Collectors.toList());

    }
}
