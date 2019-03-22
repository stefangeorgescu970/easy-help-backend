package com.easyhelp.application.controller;


import com.easyhelp.application.model.dto.account.ActiveAccountDTO;
import com.easyhelp.application.model.dto.account.DoctorAccountDTO;
import com.easyhelp.application.model.dto.account.DonationCenterPersonnelAccountDTO;
import com.easyhelp.application.model.dto.misc.IdentifierDTO;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donationcenterpersonnel.DonationCenterPersonnelServiceInterface;
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

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DoctorServiceInterface doctorService;

    @Autowired
    private DonationCenterPersonnelServiceInterface donationCenterPersonnelService;

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
        } catch (EntityNotFoundException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    @PostMapping("/rejectDoctorAccount")
    private ResponseEntity<Response> rejectDoctorAccount(@RequestBody IdentifierDTO identifierDTO) {
        try {
            this.doctorService.reviewAccount(identifierDTO.getId(), false);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    @PostMapping("/approveDcpAccount")
    private ResponseEntity<Response> approveDonationCenterPersonnelAccount(@RequestBody IdentifierDTO identifierDTO) {
        try {
            this.donationCenterPersonnelService.reviewAccount(identifierDTO.getId(), true);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    @PostMapping("/rejectDcpAccount")
    private ResponseEntity<Response> rejectDonationCenterPersonnelAccount(@RequestBody IdentifierDTO identifierDTO) {
        try {
            this.donationCenterPersonnelService.reviewAccount(identifierDTO.getId(), false);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EntityNotFoundException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    @PostMapping("/doctorAccounts")
    private ResponseEntity<Response> getDoctorAccounts(@RequestBody ActiveAccountDTO activeAccountDTO) {
        List<DoctorAccountDTO> dtoList;

        if (activeAccountDTO.isActive()) {
            dtoList = doctorService.getAllActiveAccounts();
        } else {
            dtoList = doctorService.getAllBannedAccounts();
        }

        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/dcpAccounts")
    private ResponseEntity<Response> getDcpAccounts(@RequestBody ActiveAccountDTO activeAccountDTO) {
        List<DonationCenterPersonnelAccountDTO> dtoList;

        if (activeAccountDTO.isActive()) {
            dtoList = donationCenterPersonnelService.getAllActiveAccounts();
        } else {
            dtoList = donationCenterPersonnelService.getAllBannedAccounts();
        }

        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/deactivateDoctorAccount")
    private ResponseEntity<Response> deactivateDoctorAccount(@RequestBody IdentifierDTO identifierDTO) {
        try {
            this.doctorService.deactivateAccount(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    @PostMapping("/deactivateDcpAccount")
    private ResponseEntity<Response> deactivateDcpAccount(@RequestBody IdentifierDTO identifierDTO) {
        try {
            this.donationCenterPersonnelService.deactivateAccount(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }
}
