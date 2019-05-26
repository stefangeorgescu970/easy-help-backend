package com.easyhelp.application.controller;

import com.easyhelp.application.model.dto.misc.incoming.CountyDTO;
import com.easyhelp.application.model.dto.misc.outgoing.BaseOutgoingLocationDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.hospital.HospitalServiceInterface;
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
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private HospitalServiceInterface hospitalService;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @PostMapping("/getHospitalsInCounty")
    private ResponseEntity<Response> getHosptialsInCounty(@RequestBody CountyDTO countyDTO) {
        List<Hospital> hospitals = hospitalService.getHospitalsInCounty(countyDTO.getCounty());
        List<BaseOutgoingLocationDTO> response = hospitals.stream().map(BaseOutgoingLocationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, response, 1, 1, 1);
    }

    @PostMapping("/getDonationCentersInCounty")
    private ResponseEntity<Response> getDonationCentersInCounty(@RequestBody CountyDTO countyDTO) {
        List<DonationCenter> donationCenters = donationCenterService.getDonationCentersInCounty(countyDTO.getCounty());
        List<BaseOutgoingLocationDTO> dtoList = donationCenters.stream().map(BaseOutgoingLocationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, dtoList, 1, 1, 1);
    }
}
