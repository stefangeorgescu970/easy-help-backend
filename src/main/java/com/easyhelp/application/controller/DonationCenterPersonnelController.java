package com.easyhelp.application.controller;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.dto.dcp.incoming.*;
import com.easyhelp.application.model.dto.dcp.outgoing.*;
import com.easyhelp.application.model.dto.misc.incoming.CountyDTO;
import com.easyhelp.application.model.dto.misc.incoming.IdentifierDTO;
import com.easyhelp.application.model.dto.misc.outgoing.OutgoingIdentifierDTO;
import com.easyhelp.application.model.dto.misc.outgoing.StoredBloodLevel2DTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.service.donation.DonationServiceInterface;
import com.easyhelp.application.service.donation_booking.DonationBookingServiceInterface;
import com.easyhelp.application.service.donation_commitment.DonationCommitmentServiceInterface;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
import com.easyhelp.application.service.stored_blood.StoredBloodServiceInterface;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
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
@RequestMapping("/dcp")
public class DonationCenterPersonnelController {

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @Autowired
    private DonationBookingServiceInterface donationBookingService;

    @Autowired
    private DonationRequestServiceInterface donationRequestService;

    @Autowired
    private StoredBloodServiceInterface storedBloodService;

    @Autowired
    private DonationCommitmentServiceInterface donationCommitmentService;

    @Autowired
    private DonationServiceInterface donationService;

    @Autowired
    private DonorServiceInterface donorService;

    //================================================================================
    // Managing Donations
    //================================================================================


    @PostMapping("/getDCBookings")
    private ResponseEntity<Response> getDCBookings(@RequestBody IdentifierDTO identifierDTO) {
        List<DonationBooking> bookings = donationBookingService.getDCBookings(identifierDTO.getId());
        List<DCPDonationBookingDTO> dtoList = bookings.stream().map(DCPDonationBookingDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/cancelBooking")
    private ResponseEntity<Response> cancelBooking(@RequestBody IdentifierDTO identifierDTO) {
        try {
            // TODO - here add checks that the booking is indeed made for the donation center sending the request
            donationBookingService.cancelBooking(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @RequestMapping("/createDonation")
    private ResponseEntity<Response> createDonation(@RequestBody DonationCreationDTO donationCreationDTO) {
        try {
            Donation donation = donationBookingService.createDonationFromBooking(donationCreationDTO.getBookingId(), donationCreationDTO.getGroupLetter(), donationCreationDTO.getRh());
            return ResponseBuilder.encode(HttpStatus.OK, new OutgoingIdentifierDTO(donation));
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/getWaitingForTestResults")
    private ResponseEntity<Response> getWaitingForTestResults(@RequestBody IdentifierDTO identifierDTO) {
        List<Donation> donations = donationService.getDonationsAwaitingTestResultForDonationCenter(identifierDTO.getId());
        List<DCPDonationDTO> donationDTOS = donations.stream().map(DCPDonationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, donationDTOS, 1, 1, 1);
    }

    @PostMapping("/addTestResult")
    private ResponseEntity<Response> addTestResults(@RequestBody DonationTestResultCreateDTO donationTestResultDTO) {
        try {
            // TODO - here add checks that the donation is indeed made for the donation center sending the request
            donationService.addTestResults(donationTestResultDTO);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/getWaitingForSplitResults")
    private ResponseEntity<Response> getWaitingForSplitResults(@RequestBody IdentifierDTO identifierDTO) {
        List<Donation> donations = donationService.getDonationsAwaitingSplitResultForDonationCenter(identifierDTO.getId());
        List<DCPDonationDTO> donationDTOS = donations.stream().map(DCPDonationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, donationDTOS, 1, 1, 1);
    }

    @PostMapping("/addSplitResults")
    private ResponseEntity<Response> addSplitResults(@RequestBody DonationSplitResultCreateDTO donationSplitResultCreateDTO) {
        try {
            // TODO - here add checks that the donation is indeed made for the donation center sending the request
            donationService.separateBlood(donationSplitResultCreateDTO);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    //================================================================================
    // Managing Blood Requests
    //================================================================================

    @RequestMapping("/seeAllBloodRequests")
    private ResponseEntity<Response> getRequestsForDC(@RequestBody IdentifierDTO identifierDTO) {
        try {
            DonationCenter donationCenter = donationCenterService.findById(identifierDTO.getId());
            List<DonationRequest> donationRequests = donationRequestService.getAllRequestsForDC(identifierDTO.getId());
            List<DCPDonationRequestDetailsDTO> dtoList = donationRequests.stream().map(dc -> new DCPDonationRequestDetailsDTO(dc, donationCenter)).collect(Collectors.toList());
            return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/commitToBloodRequest")
    private ResponseEntity<Response> commitToBloodRequest(@RequestBody DonationCommitmentCreateDTO donationCommitmentCreateDTO) {
        try {
            // TODO - here add checks that the stored blood is indeed owned by the donation center sending the request
            DonationCommitment donationCommitment = donationRequestService.commitToDonation(donationCommitmentCreateDTO);
            return ResponseBuilder.encode(HttpStatus.OK, new OutgoingIdentifierDTO(donationCommitment));
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/getCommitments")
    private ResponseEntity<Response> getCommitmentsForSending(@RequestBody IdentifierDTO identifierDTO) {
        try {
            DonationCenter donationCenter = donationCenterService.findById(identifierDTO.getId());
            List<DonationCommitment> donationCommitments = donationCommitmentService.getCommitmentsForDonationCenter(donationCenter);
            List<DCPDonationCommitmentDTO> dtoList = donationCommitments.stream().map(DCPDonationCommitmentDTO::new).collect(Collectors.toList());
            return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/shipCommitment")
    private ResponseEntity<Response> shipCommitment(@RequestBody IdentifierDTO identifierDTO) {
        try {
            // TODO - here add checks that the commitment is indeed made for the donation center sending the request
            donationCommitmentService.shipCommitment(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    //================================================================================
    // Managing Donors
    //================================================================================

    @PostMapping("/getInCounty")
    public ResponseEntity<Response> getDonorsInCounty(@RequestBody CountyDTO countyDTO) {
        List<Donor> donors = donorService.getDonorsInCounty(countyDTO.getCounty());
        List<DCPDonorAccountDTO> donorAccountDTOS = donors.stream().map(DCPDonorAccountDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, donorAccountDTOS, 1, 1, 1);
    }

    @PostMapping("/filterDonors")
    public ResponseEntity<Response> getDonorsInCounty(@RequestBody FilterDonorDTO filterDonorDTO) {
        List<Donor> donors = donorService.filterDonors(filterDonorDTO.getCounty(), filterDonorDTO.getGroupLetter(), filterDonorDTO.getCanDonate());
        List<DCPDonorAccountDTO> donorAccountDTOS = donors.stream().map(DCPDonorAccountDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, donorAccountDTOS, 1, 1, 1);
    }

    //================================================================================
    // Others
    //================================================================================

    @PostMapping("/getAvailableBloodInDC")
    private ResponseEntity<Response> getAvailableBloodInDC(@RequestBody IdentifierDTO identifierDTO) {
        try {
            List<StoredBlood> storedBloods = storedBloodService.getAvailableBloodInDC(identifierDTO.getId());
            List<StoredBloodLevel2DTO> storedBloodDTOS = storedBloods.stream().map(StoredBloodLevel2DTO::new).collect(Collectors.toList());
            return ResponseBuilder.encode(HttpStatus.OK, storedBloodDTOS, 1, 1, 1);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }
}