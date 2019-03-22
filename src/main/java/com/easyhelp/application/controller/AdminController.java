package com.easyhelp.application.controller;


import com.easyhelp.application.model.dto.accountrequest.IdentifierDTO;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donationcenterpersonnel.DonationCenterPersonnelServiceInterface;
import com.easyhelp.application.utils.exceptions.AccountNotFoundException;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    DoctorServiceInterface doctorService;

    @Autowired
    DonationCenterPersonnelServiceInterface donationCenterPersonnelService;

    @RequestMapping("/doctorAccountRequests")
    private ResponseEntity<Response> getDoctorAccountRequests() {
        return ResponseBuilder.encode(HttpStatus.OK, doctorService.getAllPendingAccounts(), 1, 1, 1);
    }

    @RequestMapping("/dcpAccountRequests")
    private ResponseEntity<Response> getDonationCenterPersonnelAccountRequests() {
        return ResponseBuilder.encode(HttpStatus.OK, donationCenterPersonnelService.getAllPendingAccounts(), 1, 1, 1);
    }

    @PostMapping("/approveDoctorAccount")
    private ResponseEntity<Response> approveDoctorAccount(@RequestBody IdentifierDTO identifierDTO) {
        try {
            this.doctorService.reviewAccount(identifierDTO.getId(), true);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (AccountNotFoundException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    @PostMapping("/rejectDoctorAccount")
    private ResponseEntity<Response> rejectDoctorAccount(@RequestBody IdentifierDTO identifierDTO) {
        try {
            this.doctorService.reviewAccount(identifierDTO.getId(), false);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (AccountNotFoundException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }
}
