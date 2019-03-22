package com.easyhelp.application.service.doctor;

import com.easyhelp.application.model.dto.accountrequest.AccountRequestDTO;
import com.easyhelp.application.model.dto.accountrequest.DoctorAccountRequestDTO;
import com.easyhelp.application.model.users.Doctor;

import java.util.List;

public interface DoctorServiceInterface {

    List<DoctorAccountRequestDTO> getAllPendingAccounts();

    List<Doctor> getAllActiveAccounts();

    List<Doctor> getAllBannedAccounts();

}
