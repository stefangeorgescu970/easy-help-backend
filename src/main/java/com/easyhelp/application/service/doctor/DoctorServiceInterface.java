package com.easyhelp.application.service.doctor;

import com.easyhelp.application.model.dto.account.DoctorAccountDTO;
import com.easyhelp.application.utils.exceptions.AccountNotReviewedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DoctorServiceInterface {

    List<DoctorAccountDTO> getAllPendingAccounts();

    List<DoctorAccountDTO> getAllActiveAccounts();

    List<DoctorAccountDTO> getAllBannedAccounts();

    void reviewAccount(Long doctorId, boolean shouldValidate) throws EntityNotFoundException;

    void deactivateAccount(Long doctorId) throws AccountNotReviewedException, EntityNotFoundException;
}
