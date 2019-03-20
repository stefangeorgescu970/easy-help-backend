package com.easyhelp.application.controller;


import com.easyhelp.application.service.doctor.DoctorServiceInterface;
import com.easyhelp.application.service.donationcenterpersonnel.DonationCenterPersonnelServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    DoctorServiceInterface doctorService;

    @Autowired
    DonationCenterPersonnelServiceInterface donationCenterPersonnelService;


}
