package com.easyhelp.application.controller;


import com.easyhelp.application.model.dto.account.BloodGroupRhDTO;
import com.easyhelp.application.model.dto.account.CountySsnDTO;
import com.easyhelp.application.service.donor.DonorServiceInterface;
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
@RequestMapping("/donor")
public class DonorController {

    @Autowired
    DonorServiceInterface donorService;

    @PostMapping("/updateSsnCounty")
    public ResponseEntity<Response> setCountyAndSSN(@RequestBody CountySsnDTO countySsnDTO) {
        try {
            donorService.updateCountyOnDonor(countySsnDTO.getDonorId(), countySsnDTO.getCounty());
            donorService.updateSsnOnDonor(countySsnDTO.getDonorId(), countySsnDTO.getSsn());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }

    @PostMapping("/updateBloodGroup")
    public ResponseEntity<Response> setBloodGroup(@RequestBody BloodGroupRhDTO bloodGroupRhDTO) {
        try {
            donorService.updateBloodGroupOnDonor(bloodGroupRhDTO.getDonorId(),
                                                 bloodGroupRhDTO.getGroupLetter(),
                                                 bloodGroupRhDTO.getRh());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (EasyHelpException e) {
            return ResponseBuilder.encode(HttpStatus.OK, e.getMessage());
        }
    }
}
