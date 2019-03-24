package com.easyhelp.application.controller;

import com.easyhelp.application.model.dto.location.CountyDTO;
import com.easyhelp.application.model.dto.location.LocationDTO;
import com.easyhelp.application.model.dto.misc.IdentifierDTO;
import com.easyhelp.application.model.locations.Hospital;
import com.easyhelp.application.service.hospital.HospitalServiceInterface;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
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
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalServiceInterface hospitalService;


    @PostMapping("/add")
    private ResponseEntity<Response> addHospital(@RequestBody LocationDTO location) {
        //TODO - create constructor to make this pretty

        Hospital hospital = new Hospital();
        hospital.setName(location.getName());
        hospital.setCounty(location.getCounty());
        hospital.setAddress(location.getAddress());
        hospital.setLatitude(location.getLatitude());
        hospital.setLongitude(location.getLongitude());
        hospitalService.save(hospital);
        return ResponseBuilder.encode(HttpStatus.OK, hospital);
    }

    @RequestMapping("/getAll")
    private ResponseEntity<Response> getAllHospitals() {
        List<Hospital> hospitals = hospitalService.getAll();
        List<LocationDTO> response = hospitals.stream().map(LocationDTO::new).collect(Collectors.toList());
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

    @PostMapping("getInCounty")
    private ResponseEntity<Response> getHosptialsInCounty(@RequestBody CountyDTO countyDTO) {
        List<Hospital> hospitals = hospitalService.getHospitalsInCounty(countyDTO.getCounty());
        List<LocationDTO> response = hospitals.stream().map(LocationDTO::new).collect(Collectors.toList());
        return ResponseBuilder.encode(HttpStatus.OK, response, 1, 1, 1);
    }
}
