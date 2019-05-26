package com.easyhelp.application.controller;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.dto.dcp.outgoing.DCPDonationDTO;
import com.easyhelp.application.model.dto.dcp.incoming.DonationSplitResultCreateDTO;
import com.easyhelp.application.model.dto.dcp.incoming.DonationTestResultCreateDTO;
import com.easyhelp.application.model.dto.misc.incoming.IdentifierDTO;
import com.easyhelp.application.service.donation.DonationServiceInterface;
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
@RequestMapping("/donation")
public class DonationController {

    @Autowired
    DonationServiceInterface donationService;

    @PostMapping("/addTestResult")
    private ResponseEntity<Response> addTestResults(@RequestBody DonationTestResultCreateDTO donationTestResultDTO) {
        try {
            donationService.addTestResults(donationTestResultDTO);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/addSplitResults")
    private ResponseEntity<Response> addSplitResults(@RequestBody DonationSplitResultCreateDTO donationSplitResultCreateDTO) {
        try {
            donationService.separateBlood(donationSplitResultCreateDTO);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/getWaitingForTestResults")
    private ResponseEntity<Response> getWaitingForTestResults(@RequestBody IdentifierDTO identifierDTO) {
        List<Donation> donations = donationService.getDonationsAwaitingTestResultForDonationCenter(identifierDTO.getId());
        List<DCPDonationDTO> donationDTOS = donations.stream().map(DCPDonationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, donationDTOS, 1, 1, 1);
    }

    @PostMapping("/getWaitingForSplitResults")
    private ResponseEntity<Response> getWaitingForSplitResults(@RequestBody IdentifierDTO identifierDTO) {
        List<Donation> donations = donationService.getDonationsAwaitingSplitResultForDonationCenter(identifierDTO.getId());
        List<DCPDonationDTO> donationDTOS = donations.stream().map(DCPDonationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, donationDTOS, 1, 1, 1);
    }
}
