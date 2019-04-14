package com.easyhelp.application.controller;

import com.easyhelp.application.model.dto.misc.IdentifierDTO;
import com.easyhelp.application.model.dto.requests.DonationRequestDTO;
import com.easyhelp.application.model.dto.requests.DonationRequestDetailsDTO;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.response.Response;
import com.easyhelp.application.utils.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping("/requestBlood")
    private ResponseEntity<Response> requestBlood(@RequestBody DonationRequestDTO donationRequestDTO) {
        try {
            donationRequestService.requestDonation(donationRequestDTO);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @RequestMapping("/getMyRequests")
    private ResponseEntity<Response> getMyRequests(@RequestBody IdentifierDTO identifierDTO) {
        List<DonationRequest> donationRequests = donationRequestService.getAllRequestsForDoctor(identifierDTO.getId());
        List<DonationRequestDetailsDTO> dtoList = donationRequests.stream().map(DonationRequestDetailsDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }


}


