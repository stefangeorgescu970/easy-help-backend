package com.easyhelp.application.controller;


import com.easyhelp.application.model.dto.admin.outgoing.AdminDCPAccountDTO;
import com.easyhelp.application.model.dto.admin.outgoing.AdminDoctorAccountDTO;
import com.easyhelp.application.model.dto.misc.incoming.BooleanDTO;
import com.easyhelp.application.model.dto.misc.incoming.IdentifierDTO;
import com.easyhelp.application.model.dto.misc.incoming.StringDTO;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donationcenterpersonnel.DonationCenterPersonnelServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
import com.easyhelp.application.utils.PushNotificationUtils;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DoctorServiceInterface doctorService;

    @Autowired
    private DonationCenterPersonnelServiceInterface donationCenterPersonnelService;

    @Autowired
    private DonorServiceInterface donorService;

    @RequestMapping("/doctorAccountRequests")
    private ResponseEntity<Response> getDoctorAccountRequests() {
        List<Doctor> pendingAccounts = doctorService.getAllPendingAccounts();
        List<AdminDoctorAccountDTO> dtoList = pendingAccounts.stream().map(AdminDoctorAccountDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @RequestMapping("/dcpAccountRequests")
    private ResponseEntity<Response> getDonationCenterPersonnelAccountRequests() {
        List<DonationCenterPersonnel> pendingAccounts = donationCenterPersonnelService.getAllPendingAccounts();
        List<AdminDCPAccountDTO> dtoList = pendingAccounts.stream().map(AdminDCPAccountDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
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
    private ResponseEntity<Response> getDoctorAccounts(@RequestBody BooleanDTO booleanDTO) {
        List<AdminDoctorAccountDTO> dtoList;

        if (booleanDTO.getParam()) {
            dtoList = doctorService.getAllActiveAccounts().stream().map(AdminDoctorAccountDTO::new).collect(Collectors.toList());
        } else {
            dtoList = doctorService.getAllBannedAccounts().stream().map(AdminDoctorAccountDTO::new).collect(Collectors.toList());
        }

        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }

    @PostMapping("/dcpAccounts")
    private ResponseEntity<Response> getDcpAccounts(@RequestBody BooleanDTO booleanDTO) {
        List<AdminDCPAccountDTO> dtoList;

        if (booleanDTO.getParam()) {
            dtoList = donationCenterPersonnelService.getAllActiveAccounts().stream().map(AdminDCPAccountDTO::new).collect(Collectors.toList());
        } else {
            dtoList = donationCenterPersonnelService.getAllBannedAccounts().stream().map(AdminDCPAccountDTO::new).collect(Collectors.toList());
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

    @PostMapping("/sendTestNotification")
    private ResponseEntity<Response> sendTestNotification(@RequestBody StringDTO stringDTO) {
        try {
            Donor donor = donorService.findDonorByEmail(stringDTO.getParam());
            PushNotificationUtils.sendPushNotification(donor, "Test Notification sent by System Admin");
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }
}
