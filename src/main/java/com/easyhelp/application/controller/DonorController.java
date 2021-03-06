package com.easyhelp.application.controller;


import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.dto.donor.incoming.*;
import com.easyhelp.application.model.dto.donor.outgoing.*;
import com.easyhelp.application.model.dto.misc.incoming.IdentifierDTO;
import com.easyhelp.application.model.dto.misc.incoming.StringDTO;
import com.easyhelp.application.model.dto.misc.outgoing.OutgoingIdentifierDTO;
import com.easyhelp.application.model.requests.Patient;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.service.donation.DonationServiceInterface;
import com.easyhelp.application.service.donation_booking.DonationBookingServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
import com.easyhelp.application.service.patient.PatientServiceInterface;
import com.easyhelp.application.utils.MiscUtils;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityAlreadyExistsException;
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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/donor")
public class DonorController {

    @Autowired
    private DonorServiceInterface donorService;

    @Autowired
    private DonationBookingServiceInterface donationBookingService;

    @Autowired
    private PatientServiceInterface patientService;

    @Autowired
    private DonationServiceInterface donationService;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    //================================================================================
    // Managing Donation Booking
    //================================================================================

    @PostMapping("/getDonationCenters")
    private ResponseEntity<Response> getAllDonationCenters(@RequestBody LocalizationDTO localizationDTO) {
        List<DonorDonationCenterDTO> donationCenters = donationCenterService.getOrderedDonationCenters(localizationDTO.getLongitude(), localizationDTO.getLatitude());
        return ResponseBuilder.encode(HttpStatus.OK, donationCenters, 1, 1, 1);
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

    @PostMapping("/checkPatientSSN")
    public ResponseEntity<Response> checkPatientSSN(@RequestBody StringDTO stringDTO) {
        try {
            MiscUtils.validateSsn(stringDTO.getParam());
            Patient patient = patientService.findBySSN(stringDTO.getParam());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/bookDonation")
    public ResponseEntity<Response> bookDonation(@RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            DonationBooking donationBooking = donorService.bookDonationHour(bookingRequestDTO.getUserId(), bookingRequestDTO.getSelectedDate(), bookingRequestDTO.getDonationCenterId(), bookingRequestDTO.getPatientSSN());
            return ResponseBuilder.encode(HttpStatus.OK, new OutgoingIdentifierDTO(donationBooking));
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/getCurrentBooking")
    public ResponseEntity<Response> getCurrentBooking(@RequestBody IdentifierDTO identifierDTO) {
        try {
            DonorDonationBookingDTO booking = new DonorDonationBookingDTO(donationBookingService.getDonorBooking(identifierDTO.getId()));
            return ResponseBuilder.encode(HttpStatus.OK, booking);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/cancelBooking")
    private ResponseEntity<Response> cancelBooking(@RequestBody IdentifierDTO identifierDTO) {
        try {
            if (!bookingOwnedByDonor(identifierDTO.getUserId(), identifierDTO.getId())) {
                return ResponseBuilder.encode(HttpStatus.OK, "You do not have ownership of this booking.");
            }
            donationBookingService.cancelBooking(identifierDTO.getId(), false);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    //================================================================================
    // Managing Account Data
    //================================================================================

    @PostMapping("/updateSsnCounty")
    public ResponseEntity<Response> setCountyAndSSN(@RequestBody CountySsnDTO countySsnDTO) {
        try {
            donorService.updateCountyOnDonor(countySsnDTO.getUserId(), countySsnDTO.getCounty());
            donorService.updateSsnOnDonor(countySsnDTO.getUserId(), countySsnDTO.getSsn());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/updateBloodGroup")
    public ResponseEntity<Response> setBloodGroup(@RequestBody BloodGroupRhDTO bloodGroupRhDTO) {
        try {
            donorService.updateBloodGroupOnDonor(bloodGroupRhDTO.getUserId(),
                    bloodGroupRhDTO.getGroupLetter(),
                    bloodGroupRhDTO.getRh());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/addDonationForm")
    public ResponseEntity<Response> addDonationForm(@RequestBody DonationFormCreateDTO donationFormCreateDTO) {
        try {
            donorService.addDonationForm(donationFormCreateDTO);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/getDonorSummary")
    public ResponseEntity<Response> getDonorSummary(@RequestBody IdentifierDTO identifierDTO) {
        try {
            DonorSummary donorSummary = donorService.getDonorSummary(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK, new DonorSummaryDTO(donorSummary));
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/registerPushToken")
    public ResponseEntity<Response> registerPushToken(@RequestBody PushNotificationDTO pushNotificationDTO) {
        try {
            donorService.registerPushToken(pushNotificationDTO.getUserId(), pushNotificationDTO.getToken(), pushNotificationDTO.getAppPlatform());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/history")
    public ResponseEntity<Response> getDonorHistory(@RequestBody IdentifierDTO identifierDTO) {
        List<Donation> donations = donationService.getDonationsForDonor(identifierDTO.getId());
        List<DonorDonationDTO> dtos = donations.stream().map(DonorDonationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtos, 1, 1, 1);
    }

    //================================================================================
    // Private Helpers
    //================================================================================

    private Boolean bookingOwnedByDonor(Long donorId, Long bookingId) throws EntityNotFoundException {
        Donor donor = donorService.findById(donorId);
        DonationBooking donationBooking = donationBookingService.findById(bookingId);

        return donationBooking.getDonor().getId().equals(donor.getId());
    }
}
