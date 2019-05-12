package com.easyhelp.application.controller;

import com.easyhelp.application.model.dto.donation.DonationSplitResultsDTO;
import com.easyhelp.application.model.dto.donation.DonationTestResultDTO;
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

@RestController
@RequestMapping("/donation")
public class DonationController {
    @Autowired
    DonationServiceInterface donationService;

    @PostMapping("/addTestResult")
    private ResponseEntity<Response> addTestResults(@RequestBody DonationTestResultDTO donationTestResultDTO) {
        try {
            donationService.addTestResults(donationTestResultDTO);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/addSplitResults")
    private ResponseEntity<Response> addSplitResults(@RequestBody DonationSplitResultsDTO donationSplitResultsDTO) {
        try {
            donationService.separateBlood(donationSplitResultsDTO);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }
}
