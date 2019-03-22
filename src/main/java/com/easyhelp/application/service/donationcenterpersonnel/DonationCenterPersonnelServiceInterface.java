package com.easyhelp.application.service.donationcenterpersonnel;

import com.easyhelp.application.model.dto.account.DonationCenterPersonnelAccountRequestDTO;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DonationCenterPersonnelServiceInterface {

    List<DonationCenterPersonnelAccountRequestDTO> getAllPendingAccounts();

    List<DonationCenterPersonnel> getAllActiveAccounts();

    List<DonationCenterPersonnel> getAllBannedAccounts();

    void reviewAccount(Long dcpId, boolean shouldValidate) throws EntityNotFoundException;
}