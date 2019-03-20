package com.easyhelp.application.controller;

import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
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
@RequestMapping("/donationCenter")
public class DonationCenterController {

    @Autowired
    DonationCenterServiceInterface donationCenterService;


    @PostMapping("/add")
    private ResponseEntity<Response> addDonationCenter(@RequestBody LocationDTO location) {
        DonationCenter donationCenter = new DonationCenter();
        donationCenter.setName(location.getName());
        donationCenter.setLatitude(location.getLatitude());
        donationCenter.setLongitude(location.getLongitude());
        donationCenterService.save(donationCenter);
        return ResponseBuilder.encode(HttpStatus.OK);
    }

    @RequestMapping("/getAll")
    private ResponseEntity<Response> getAllDonationCenters() {
        return ResponseBuilder.encode(HttpStatus.OK, donationCenterService.getAll(), 1, 1, 1);
    }

}
