package com.easyhelp.application.controller;

import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.server.ServerResponse;
import com.easyhelp.application.service.DoctorServiceInterface;
import com.easyhelp.application.utils.response.Response;
import com.easyhelp.application.utils.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorServiceInterface doctorServiceInterface;

    @RequestMapping("/get")
    private ServerResponse<Doctor> getAllDoctors() {
        ServerResponse<Doctor> serverResponse = new ServerResponse<>();
        serverResponse.setId("Doctors");
        serverResponse.setAct("getAll");
        serverResponse.setModel(doctorServiceInterface.getAllDoctors());
        return serverResponse;
    }

}


