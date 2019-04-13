package com.easyhelp.application.controller;

import com.easyhelp.application.model.donations.DonationBooking;
import com.easyhelp.application.model.donations.AvailableDate;
import com.easyhelp.application.model.dto.booking.AvailableDateDTO;
import com.easyhelp.application.model.dto.booking.DonationBookingDTO;
import com.easyhelp.application.model.dto.location.CountyDTO;
import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.dto.misc.IdentifierDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.service.donation_booking.DonationBookingServiceInterface;
import com.easyhelp.application.service.donation_request.DonationRequestServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
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

    @PostMapping("/add")
    private ResponseEntity<Response> addDonationCenter(@RequestBody LocationDTO location) {
        //TODO - create constructor to make this pretty

        DonationCenter donationCenter = new DonationCenter();
        donationCenter.setName(location.getName());
        donationCenter.setLatitude(location.getLatitude());
        donationCenter.setLongitude(location.getLongitude());
        donationCenter.setAddress(location.getAddress());
        donationCenter.setCounty(location.getCounty());
        donationCenterService.save(donationCenter);
        return ResponseBuilder.encode(HttpStatus.OK, donationCenter);
    }

    @RequestMapping("/getAll")
    private ResponseEntity<Response> getAllDonationCenters() {
        List<DonationCenter> donationCenters = donationCenterService.getAll();
        List<LocationDTO> dtoList = donationCenters.stream().map(LocationDTO::new).collect(Collectors.toList());
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
        List<LocationDTO> dtoList = donationCenters.stream().map(LocationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/getDCBookings")
    private ResponseEntity<Response> getDCBookings(@RequestBody IdentifierDTO identifierDTO) {
        List<DonationBooking> bookings = donationBookingService.getDCBookings(identifierDTO.getId());
        List<DonationBookingDTO> dtoList = bookings.stream().map(DonationBookingDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
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
}
