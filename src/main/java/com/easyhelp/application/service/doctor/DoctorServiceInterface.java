package com.easyhelp.application.service.doctor;

import com.easyhelp.application.model.dto.account.DoctorAccountRequestDTO;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DoctorServiceInterface {

    List<DoctorAccountRequestDTO> getAllPendingAccounts();

    List<Doctor> getAllActiveAccounts();

    List<Doctor> getAllBannedAccounts();

    void reviewAccount(Long doctorId, boolean shouldValidate) throws EntityNotFoundException;
}
