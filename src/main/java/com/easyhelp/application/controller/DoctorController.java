package com.easyhelp.application.controller;

import com.easyhelp.application.model.dto.doctor.incoming.DoctorPatientCreateDTO;
import com.easyhelp.application.model.dto.doctor.incoming.DonationRequestCreateDTO;
import com.easyhelp.application.model.dto.doctor.outgoing.DoctorDonationCommitmentDTO;
import com.easyhelp.application.model.dto.doctor.outgoing.DoctorDonationRequestDetailsDTO;
import com.easyhelp.application.model.dto.doctor.outgoing.DoctorPatientLevel2DTO;
import com.easyhelp.application.model.dto.misc.incoming.IdentifierDTO;
import com.easyhelp.application.model.dto.misc.outgoing.OutgoingIdentifierDTO;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.requests.RequestStatus;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donation_commitment.DonationCommitmentServiceInterface;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.SsnInvalidException;
import com.easyhelp.application.utils.response.Response;
import com.easyhelp.application.utils.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorServiceInterface doctorService;

    @Autowired
    private PatientServiceInterface patientService;

    @Autowired
    private DonationRequestServiceInterface donationRequestService;

    @Autowired
    private DonationCommitmentServiceInterface donationCommitmentService;

    //================================================================================
    // Managing Patients
    //================================================================================

    @PostMapping("/addPatient")
    private ResponseEntity<Response> addPatient(@RequestBody DoctorPatientCreateDTO patientDTO) {
        try {
            Patient patient = patientService.addPatient(
                    patientDTO.getDoctorId(),
                    patientDTO.getSsn(),
                    patientDTO.getBloodType().getGroupLetter(),
                    patientDTO.getBloodType().getRh());
            return ResponseBuilder.encode(HttpStatus.OK, new OutgoingIdentifierDTO(patient));
        } catch (EntityNotFoundException | EntityAlreadyExistsException | SsnInvalidException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/seeMyPatients")
    private ResponseEntity<Response> getMyPatients(@RequestBody IdentifierDTO identifierDTO) {
        List<Patient> patients = patientService.getPatientsForDoctor(identifierDTO.getId());
        List<DoctorPatientLevel2DTO> dtoList = patients.stream().map(DoctorPatientLevel2DTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/deletePatient")
    private ResponseEntity<Response> deletePatient(@RequestBody IdentifierDTO identifierDTO) {
        try {
            if (!patientOwnedByDoctor(identifierDTO.getUserId(), identifierDTO.getId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this patient.");
            }
            patientService.deletePatient(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    //================================================================================
    // Managing Blood Requests
    //================================================================================

    @PostMapping("/requestBlood")
    private ResponseEntity<Response> requestBlood(@RequestBody DonationRequestCreateDTO donationRequestDTO) {
        try {
            DonationRequest donationRequest = donationRequestService.requestDonation(donationRequestDTO.getDoctorId(), donationRequestDTO.getPatientId(), donationRequestDTO.getQuantity(), donationRequestDTO.getUrgency(), donationRequestDTO.getBloodComponent());
            return ResponseBuilder.encode(HttpStatus.OK, new OutgoingIdentifierDTO(donationRequest));
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @RequestMapping("/seeMyBloodRequests")
    private ResponseEntity<Response> getMyRequests(@RequestBody IdentifierDTO identifierDTO) {
        List<DonationRequest> donationRequests = donationRequestService.getAllRequestsForDoctor(identifierDTO.getId());
        List<DoctorDonationRequestDetailsDTO> dtoList = donationRequests.stream().map(DoctorDonationRequestDetailsDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/cancelBloodRequest")
    private ResponseEntity<Response> cancelBloodRequest(@RequestBody IdentifierDTO identifierDTO) {
        try {
            if (!requestOwnedByDoctor(identifierDTO.getUserId(), identifierDTO.getId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this request.");
            }
            donationRequestService.cancelRequest(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    //================================================================================
    // Managing Donation Commitments
    //================================================================================

    @PostMapping("/requestCommitments")
    private ResponseEntity<Response> getRequestCommitments(@RequestBody IdentifierDTO identifierDTO) {
        try {
            List<DonationCommitment> donationCommitments = donationCommitmentService.getDonationCommitments(identifierDTO.getId());
            List<DoctorDonationCommitmentDTO> dtoList = donationCommitments.stream().map(DoctorDonationCommitmentDTO::new).collect(Collectors.toList());
            return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/acceptCommitment")
    private ResponseEntity<Response> acceptCommitment(@RequestBody IdentifierDTO identifierDTO) {
        try {
            if (!commitmentOwnedByDoctor(identifierDTO.getUserId(), identifierDTO.getId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this commitment");
            }
            RequestStatus status = donationCommitmentService.acceptCommitment(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK, status);
        } catch (EasyHelpException e) {
            e.printStackTrace();
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/declineCommitment")
    private ResponseEntity<Response> declineCommitment(@RequestBody IdentifierDTO identifierDTO) {
        try {
            if (!commitmentOwnedByDoctor(identifierDTO.getUserId(), identifierDTO.getId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this commitment");
            }
            donationCommitmentService.declineCommitment(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            e.printStackTrace();
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/commitmentArrived")
    private ResponseEntity<Response> markCommitmentAsArrived(@RequestBody IdentifierDTO identifierDTO) {
        try {
            if (!commitmentOwnedByDoctor(identifierDTO.getUserId(), identifierDTO.getId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this commitment");
            }
            donationCommitmentService.markCommitmentAsArrived(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            e.printStackTrace();
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    //================================================================================
    // Private Helpers
    //================================================================================

    private Boolean patientOwnedByDoctor(Long doctorId, Long patientId) throws EntityNotFoundException {
        Doctor doctor = doctorService.findById(doctorId);
        Patient patient = patientService.findById(patientId);

        return doctor.getPatients().stream().anyMatch(patient1 -> patient1.getId().equals(patient.getId()));
    }


    private Boolean requestOwnedByDoctor(Long doctorId, Long donationRequestId) throws EntityNotFoundException {
        Doctor doctor = doctorService.findById(doctorId);
        DonationRequest donationRequest = donationRequestService.findById(donationRequestId);

        return doctor.getDonationRequests().stream().anyMatch(donationRequest1 -> donationRequest1.getId().equals(donationRequest.getId()));
    }

    private Boolean commitmentOwnedByDoctor(Long doctorId, Long donationCommitmentId) throws EntityNotFoundException {
        Doctor doctor = doctorService.findById(doctorId);
        DonationCommitment donationCommitment = donationCommitmentService.findById(donationCommitmentId);
        boolean returnValue = false;

        for (DonationRequest donationRequest : doctor.getDonationRequests()) {
            for (DonationCommitment donationCommitment1 : donationRequest.getDonationCommitments()) {
                if (donationCommitment1.getId().equals(donationCommitment.getId())) {
                    returnValue = true;
                }
            }
        }

        return returnValue;
    }
}


