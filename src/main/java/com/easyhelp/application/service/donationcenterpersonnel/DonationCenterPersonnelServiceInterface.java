package com.easyhelp.application.service.donationcenterpersonnel;

import com.easyhelp.application.model.dto.accountrequest.DonationCenterPersonnelAccountRequestDTO;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.utils.exceptions.AccountNotFoundException;

import java.util.List;

public interface DonationCenterPersonnelServiceInterface {

    List<DonationCenterPersonnelAccountRequestDTO> getAllPendingAccounts();

    List<DonationCenterPersonnel> getAllActiveAccounts();

    List<DonationCenterPersonnel> getAllBannedAccounts();

    void reviewAccount(Long dcpId, boolean shouldValidate) throws AccountNotFoundException;
}