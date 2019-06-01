package com.easyhelp.application.controller;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.dto.dcp.incoming.*;
import com.easyhelp.application.model.dto.dcp.outgoing.*;
import com.easyhelp.application.model.dto.misc.incoming.CountyDTO;
import com.easyhelp.application.model.dto.misc.incoming.IdentifierDTO;
import com.easyhelp.application.model.dto.misc.outgoing.BloodStockDTO;
import com.easyhelp.application.model.dto.misc.outgoing.OutgoingIdentifierDTO;
import com.easyhelp.application.model.dto.misc.outgoing.StoredBloodLevel1DTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.service.donation.DonationServiceInterface;
import com.easyhelp.application.service.donation_booking.DonationBookingServiceInterface;
import com.easyhelp.application.service.donation_commitment.DonationCommitmentServiceInterface;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.donationcenterpersonnel.DonationCenterPersonnelServiceInterface;
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

    @Autowired
    private DonationCenterPersonnelServiceInterface donationCenterPersonnelService;

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
            if (!bookingOwnedByDCP(identifierDTO.getUserId(), identifierDTO.getId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this booking.");
            }
            donationBookingService.cancelBooking(identifierDTO.getId(), true);
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
            if (!donationOwnedByDCP(donationTestResultDTO.getUserId(), donationTestResultDTO.getDonationId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this donation.");
            }
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
            if (!donationOwnedByDCP(donationSplitResultCreateDTO.getUserId(), donationSplitResultCreateDTO.getDonationId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this donation.");
            }
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
            if (!donationOwnedByDCP(identifierDTO.getUserId(), identifierDTO.getId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this commitment.");
            }
            donationCommitmentService.shipCommitment(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    //================================================================================
    // Managing Donors
    //================================================================================

    @PostMapping("/getDonorsInCounty")
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
    // Managing Blood
    //================================================================================

    @PostMapping("/getAvailableBloodInDC")
    private ResponseEntity<Response> getAvailableBloodInDC(@RequestBody IdentifierDTO identifierDTO) {
        try {
            List<StoredBlood> storedBloods = storedBloodService.getAvailableBloodInDC(identifierDTO.getId());
            List<StoredBloodLevel1DTO> storedBloodDTOS = storedBloods.stream().map(StoredBloodLevel1DTO::new).collect(Collectors.toList());
            return ResponseBuilder.encode(HttpStatus.OK, storedBloodDTOS, 1, 1, 1);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/expiredBloodInDC")
    private ResponseEntity<Response> getExpiredBloodInDC(@RequestBody IdentifierDTO identifierDTO) {
        List<StoredBlood> storedBloods = storedBloodService.getExpiredBloodInDC(identifierDTO.getId());
        List<StoredBloodLevel1DTO> dtos = storedBloods.stream().map(StoredBloodLevel1DTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtos, 1, 1, 1);
    }

    @RequestMapping("/countryBloodStock")
    private ResponseEntity<Response> getCountryBloodStock() {
        List<BloodStockDTO> bloodStockDTOS = storedBloodService.getBloodStocksInCountry();
        return ResponseBuilder.encode(HttpStatus.OK, bloodStockDTOS, 1, 1, 1);
    }

    @PostMapping("/dcBloodStock")
    private ResponseEntity<Response> getDonatioNCenterBloodStock(@RequestBody IdentifierDTO identifierDTO) {
        List<BloodStockDTO> bloodStockDTOS = storedBloodService.getBloodStocksInDC(identifierDTO.getId());
        return ResponseBuilder.encode(HttpStatus.OK, bloodStockDTOS, 1, 1, 1);
    }

    @PostMapping("/discardBlood")
    private ResponseEntity<Response> discardBlood(@RequestBody IdentifierDTO identifierDTO) {
        try {
            storedBloodService.removeBlood(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    //================================================================================
    // Private Helpers
    //================================================================================

    private Boolean bookingOwnedByDCP(Long dcpId, Long bookingId) throws EntityNotFoundException {
        DonationCenterPersonnel dcp = donationCenterPersonnelService.findById(dcpId);
        DonationBooking donationBooking = donationBookingService.findById(bookingId);

        return donationBooking.getDonationCenter().getId().equals(dcp.getDonationCenter().getId());
    }

    private Boolean donationOwnedByDCP(Long dcpId, Long donationid) throws EntityNotFoundException {
        DonationCenterPersonnel dcp = donationCenterPersonnelService.findById(dcpId);
        Donation donation = donationService.findById(donationid);

        return donation.getDonationCenter().getId().equals(dcp.getDonationCenter().getId());
    }

    private Boolean commitmentOwnedByDCP(Long dcpId, Long commitmentId) throws EntityNotFoundException {
        DonationCenterPersonnel dcp = donationCenterPersonnelService.findById(dcpId);
        DonationCommitment donationCommitment = donationCommitmentService.findById(commitmentId);

        return donationCommitment.getDonationCenter().getId().equals(dcp.getDonationCenter().getId());
    }
}
