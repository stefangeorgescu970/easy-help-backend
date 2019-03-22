package com.easyhelp.application.service.doctor;

import com.easyhelp.application.model.dto.accountrequest.AccountRequestDTO;
import com.easyhelp.application.model.dto.accountrequest.DoctorAccountRequestDTO;
import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.utils.exceptions.AccountNotFoundException;
import com.easyhelp.application.utils.exceptions.AccountNotReviewedException;

import java.util.List;

public interface DoctorServiceInterface {

    List<DoctorAccountRequestDTO> getAllPendingAccounts();

    List<Doctor> getAllActiveAccounts();

    List<Doctor> getAllBannedAccounts();

    void reviewAccount(Long doctorId, boolean shouldValidate) throws AccountNotFoundException;
}
