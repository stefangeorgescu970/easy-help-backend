package com.easyhelp.application.service.doctor;

import com.easyhelp.application.model.users.Doctor;
import com.easyhelp.application.utils.exceptions.AccountNotReviewedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DoctorServiceInterface {

    List<Doctor> getAllPendingAccounts();

    List<Doctor> getAllActiveAccounts();

    List<Doctor> getAllBannedAccounts();

    void reviewAccount(Long doctorId, boolean shouldValidate) throws EntityNotFoundException;

    void deactivateAccount(Long doctorId) throws AccountNotReviewedException, EntityNotFoundException;

    Doctor findById(Long doctorId) throws EntityNotFoundException;
}
