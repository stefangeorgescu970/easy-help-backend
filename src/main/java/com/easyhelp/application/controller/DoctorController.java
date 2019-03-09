package com.easyhelp.application.controller;

import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.server.ServerResponse;
import com.easyhelp.application.service.DoctorServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


