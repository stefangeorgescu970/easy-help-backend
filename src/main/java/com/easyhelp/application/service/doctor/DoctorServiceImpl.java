package com.easyhelp.application.service.doctor;

import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorServiceInterface {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public List<Doctor> getAllPendingAccounts() {
        return doctorRepository.findAll().stream().filter(doctor -> !doctor.getIsReviewed()).collect(Collectors.toList());
    }

    @Override
    public List<Doctor> getAllActiveAccounts() {
        return doctorRepository.findAll().stream().filter(doctor -> doctor.getIsReviewed() && doctor.getIsValid()).collect(Collectors.toList());
    }

    @Override
    public List<Doctor> getAllBannedAccounts() {
        return doctorRepository.findAll().stream().filter(doctor -> doctor.getIsReviewed() && !doctor.getIsValid()).collect(Collectors.toList());
    }
}
