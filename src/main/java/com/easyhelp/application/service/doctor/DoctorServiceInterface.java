package com.easyhelp.application.service.doctor;

import com.easyhelp.application.model.users.Doctor;

import java.util.List;

public interface DoctorServiceInterface {

    List<Doctor> getAllPendingAccounts();

    List<Doctor> getAllActiveAccounts();

    List<Doctor> getAllBannedAccounts();

}
