package com.easyhelp.application.controller;

import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.dto.admin.incoming.AdminCreateDonationCenterDTO;
import com.easyhelp.application.model.dto.donor.incoming.LocalizationDTO;
import com.easyhelp.application.model.dto.misc.outgoing.BaseOutgoingLocationDTO;
import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.easyhelp.application.model.dto.misc.outgoing.StoredBloodLevel2DTO;
import com.easyhelp.application.model.dto.donor.outgoing.AvailableDateDTO;
import com.easyhelp.application.model.dto.dcp.outgoing.DCPDonationBookingDTO;
import com.easyhelp.application.model.dto.dcp.incoming.DonationCommitmentCreateDTO;
import com.easyhelp.application.model.dto.dcp.outgoing.DCPDonationCommitmentDTO;
import com.easyhelp.application.model.dto.dcp.incoming.DonationCreationDTO;
import com.easyhelp.application.model.dto.misc.incoming.CountyDTO;
import com.easyhelp.application.model.dto.misc.incoming.IdentifierDTO;
import com.easyhelp.application.model.dto.dcp.outgoing.DCPDonationRequestDetailsDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationCommitment;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.service.donation_booking.DonationBookingServiceInterface;
import com.easyhelp.application.service.donation_commitment.DonationCommitmentServiceInterface;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
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
@RequestMapping("/donationCenter")
public class DonationCenterController {

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

    @PostMapping("/add")
    private ResponseEntity<Response> addDonationCenter(@RequestBody AdminCreateDonationCenterDTO location) {
        DonationCenter donationCenter = new DonationCenter(location);
        donationCenterService.save(donationCenter);
        return ResponseBuilder.encode(HttpStatus.OK, donationCenter);
    }

    @RequestMapping("/getAll")
    private ResponseEntity<Response> getAllDonationCenters() {
        List<DonationCenter> donationCenters = donationCenterService.getAll();
        List<ExtendedOutgoingLocationDTO> dtoList = donationCenters.stream().map(ExtendedOutgoingLocationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/getAll")
    private ResponseEntity<Response> getAllDonationCenters(@RequestBody LocalizationDTO localizationDTO) {
        List<DonationCenter> donationCenters = donationCenterService.getAll();
        List<ExtendedOutgoingLocationDTO> dtoList = donationCenters.stream().map(ExtendedOutgoingLocationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/remove")
    private ResponseEntity<Response> removeDonationCenter(@RequestBody IdentifierDTO identifierDTO) {
        try {
            donationCenterService.removeDonationCenter(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    @PostMapping("/getInCounty")
    private ResponseEntity<Response> getDonationCentersInCounty(@RequestBody CountyDTO countyDTO) {
        List<DonationCenter> donationCenters = donationCenterService.getDonationCentersInCounty(countyDTO.getCounty());
        List<BaseOutgoingLocationDTO> dtoList = donationCenters.stream().map(BaseOutgoingLocationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/getDCBookings")
    private ResponseEntity<Response> getDCBookings(@RequestBody IdentifierDTO identifierDTO) {
        List<DonationBooking> bookings = donationBookingService.getDCBookings(identifierDTO.getId());
        List<DCPDonationBookingDTO> dtoList = bookings.stream().map(DCPDonationBookingDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/cancelBooking")
    private ResponseEntity<Response> cancelBooking(@RequestBody IdentifierDTO identifierDTO) {
        try {
            donationBookingService.cancelBooking(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/getAvailableHours")
    public ResponseEntity<Response> getAvailableHoursForDCNext7Days(@RequestBody IdentifierDTO identifierDTO) {
        try {
            List<AvailableDate> hours = donationBookingService.getAvailableBookingSlots(identifierDTO.getId());
            List<AvailableDateDTO> hoursDTO = hours.stream().map(AvailableDateDTO::new).collect(Collectors.toList());
            return ResponseBuilder.encode(HttpStatus.OK, hoursDTO, 1, 1, 1);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

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

    @RequestMapping("/createDonation")
    private ResponseEntity<Response> createDonation(@RequestBody DonationCreationDTO donationCreationDTO) {
        try {
            donationBookingService.createDonationFromBooking(donationCreationDTO.getBookingId(), donationCreationDTO.getGroupLetter(), donationCreationDTO.getRh());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/commitToBloodRequest")
    private ResponseEntity<Response> commitToBloodRequest(@RequestBody DonationCommitmentCreateDTO donationCommitmentCreateDTO) {
        try {
            donationRequestService.commitToDonation(donationCommitmentCreateDTO);
            return ResponseBuilder.encode(HttpStatus.OK);
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

    @PostMapping("/getAvailableBloodInDC")
    private ResponseEntity<Response> getAvailableBloodInDC(@RequestBody IdentifierDTO identifierDTO) {
        try {
            List<StoredBlood> storedBloods = storedBloodService.getAvailableBloodInDC(identifierDTO.getId());
            List<StoredBloodLevel2DTO> storedBloodDTOS = storedBloods.stream().map(StoredBloodLevel2DTO::new).collect(Collectors.toList());;
            return ResponseBuilder.encode(HttpStatus.OK, storedBloodDTOS, 1, 1, 1);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/shipCommitment")
    private ResponseEntity<Response> shipCommitment(@RequestBody IdentifierDTO identifierDTO) {
        try {
            donationCommitmentService.shipCommitment(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }
}
