package com.easyhelp.application.controller;


import com.easyhelp.application.model.donations.DonorSummary;
import com.easyhelp.application.model.dto.account.BloodGroupRhDTO;
import com.easyhelp.application.model.dto.account.CountySsnDTO;
import com.easyhelp.application.model.dto.account.DonorAccountDTO;
import com.easyhelp.application.model.dto.booking.BookingRequestDTO;
import com.easyhelp.application.model.dto.booking.DonationBookingDTO;
import com.easyhelp.application.model.dto.donation.DonorSummaryDTO;
import com.easyhelp.application.model.dto.location.CountyDTO;
import com.easyhelp.application.model.dto.misc.IdentifierDTO;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.service.donation_booking.DonationBookingServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/donor")
public class DonorController {

    @Autowired
    DonorServiceInterface donorService;

    @Autowired
    DonationBookingServiceInterface donationBookingService;

    @PostMapping("/updateSsnCounty")
    public ResponseEntity<Response> setCountyAndSSN(@RequestBody CountySsnDTO countySsnDTO) {
        try {
            donorService.updateCountyOnDonor(countySsnDTO.getDonorId(), countySsnDTO.getCounty());
            donorService.updateSsnOnDonor(countySsnDTO.getDonorId(), countySsnDTO.getSsn());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/updateBloodGroup")
    public ResponseEntity<Response> setBloodGroup(@RequestBody BloodGroupRhDTO bloodGroupRhDTO) {
        try {
            donorService.updateBloodGroupOnDonor(bloodGroupRhDTO.getDonorId(),
                    bloodGroupRhDTO.getGroupLetter(),
                    bloodGroupRhDTO.getRh());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/bookDonation")
    public ResponseEntity<Response> bookDonation(@RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            Calendar calendar = bookingRequestDTO.getSelectedDate();
            donorService.bookDonationHour(bookingRequestDTO.getId(), calendar, bookingRequestDTO.getDonationCenterId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/getCurrentBooking")
    public ResponseEntity<Response> getCurrentBooking(@RequestBody IdentifierDTO identifierDTO) {
        try {
            DonationBookingDTO booking = new DonationBookingDTO(donationBookingService.getDonorBooking(identifierDTO.getId()));
            return ResponseBuilder.encode(HttpStatus.OK, booking);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/getInCounty")
    public ResponseEntity<Response> getDonorsInCounty(@RequestBody CountyDTO countyDTO) {
        List<Donor> donors = donorService.getDonorsInCounty(countyDTO.getCounty());
        List<DonorAccountDTO> donorAccountDTOS = donors.stream().map(DonorAccountDTO::new).collect(Collectors.toList());

        return ResponseBuilder.encode(HttpStatus.OK, donorAccountDTOS, 1, 1, 1);
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
}
