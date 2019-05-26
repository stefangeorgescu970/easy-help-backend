package com.easyhelp.application.controller;


import com.easyhelp.application.model.dto.admin.incoming.AdminCreateDonationCenterDTO;
import com.easyhelp.application.model.dto.admin.incoming.AdminCreateHospitalDTO;
import com.easyhelp.application.model.dto.admin.outgoing.AdminDCPAccountDTO;
import com.easyhelp.application.model.dto.admin.outgoing.AdminDoctorAccountDTO;
import com.easyhelp.application.model.dto.misc.incoming.BooleanDTO;
import com.easyhelp.application.model.dto.misc.incoming.IdentifierDTO;
import com.easyhelp.application.model.dto.misc.incoming.StringDTO;
import com.easyhelp.application.model.dto.misc.outgoing.ExtendedOutgoingLocationDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.donationcenterpersonnel.DonationCenterPersonnelServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
import com.easyhelp.application.service.hospital.HospitalServiceInterface;
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

    @Autowired
    private HospitalServiceInterface hospitalService;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    //================================================================================
    // Managing Doctor Accounts
    //================================================================================

    @RequestMapping("/doctorAccountRequests")
    private ResponseEntity<Response> getDoctorAccountRequests() {
        List<Doctor> pendingAccounts = doctorService.getAllPendingAccounts();
        List<AdminDoctorAccountDTO> dtoList = pendingAccounts.stream().map(AdminDoctorAccountDTO::new).collect(Collectors.toList());
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

    @PostMapping("/deactivateDoctorAccount")
    private ResponseEntity<Response> deactivateDoctorAccount(@RequestBody IdentifierDTO identifierDTO) {
        try {
            this.doctorService.deactivateAccount(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    //================================================================================
    // Managing Donation Center Personnel Accounts
    //================================================================================

    @RequestMapping("/dcpAccountRequests")
    private ResponseEntity<Response> getDonationCenterPersonnelAccountRequests() {
        List<DonationCenterPersonnel> pendingAccounts = donationCenterPersonnelService.getAllPendingAccounts();
        List<AdminDCPAccountDTO> dtoList = pendingAccounts.stream().map(AdminDCPAccountDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
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

    @PostMapping("/deactivateDcpAccount")
    private ResponseEntity<Response> deactivateDcpAccount(@RequestBody IdentifierDTO identifierDTO) {
        try {
            this.donationCenterPersonnelService.deactivateAccount(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    //================================================================================
    // Managing Hospitals
    //================================================================================

    @PostMapping("/add")
    private ResponseEntity<Response> addHospital(@RequestBody AdminCreateHospitalDTO location) {
        Hospital hospital = new Hospital(location);
        hospitalService.save(hospital);
        return ResponseBuilder.encode(HttpStatus.OK, hospital);
    }

    @RequestMapping("/getAll")
    private ResponseEntity<Response> getAllHospitals() {
        List<Hospital> hospitals = hospitalService.getAll();
        List<ExtendedOutgoingLocationDTO> response = hospitals.stream().map(ExtendedOutgoingLocationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, response, 1, 1, 1);
    }

    @PostMapping("/remove")
    private ResponseEntity<Response> removeHospital(@RequestBody IdentifierDTO identifierDTO) {
        try {
            hospitalService.removeHospital(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    //================================================================================
    // Managing Donation Centers
    //================================================================================

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

    @PostMapping("/remove")
    private ResponseEntity<Response> removeDonationCenter(@RequestBody IdentifierDTO identifierDTO) {
        try {
            donationCenterService.removeDonationCenter(identifierDTO.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException exp) {
            return ResponseBuilder.encode(HttpStatus.OK, exp.getMessage());
        }
    }

    //================================================================================
    // Others
    //================================================================================

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
