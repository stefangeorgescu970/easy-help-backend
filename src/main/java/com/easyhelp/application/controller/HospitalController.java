package com.easyhelp.application.controller;

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


@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalServiceInterface hospitalService;


    @PostMapping("/add")
    private ResponseEntity<Response> addHospital(@RequestBody LocationDTO location) {
        Hospital hospital = new Hospital();
        hospital.setName(location.getName());
        hospital.setLatitude(location.getLatitude());
        hospital.setLongitude(location.getLongitude());
        hospitalService.save(hospital);
        return ResponseBuilder.encode(HttpStatus.OK);
    }

    @RequestMapping("/getAll")
    private ResponseEntity<Response> getAllHospitals() {
        return ResponseBuilder.encode(HttpStatus.OK, hospitalService.getAll(), 1, 1, 1);
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

    @RequestMapping("/getAvailableCounties")
    private ResponseEntity<Response> getCounties() {
        return ResponseBuilder.encode(HttpStatus.OK, hospitalService.getAll(), 1, 1, 1);
    }
}
