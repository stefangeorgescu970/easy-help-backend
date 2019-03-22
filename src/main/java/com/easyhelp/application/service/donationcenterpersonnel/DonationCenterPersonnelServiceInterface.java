package com.easyhelp.application.service.donationcenterpersonnel;

import com.easyhelp.application.model.dto.accountrequest.DonationCenterPersonnelAccountRequestDTO;
import com.easyhelp.application.model.users.DonationCenterPersonnel;

import java.util.List;

public interface DonationCenterPersonnelServiceInterface {

    List<DonationCenterPersonnelAccountRequestDTO> getAllPendingAccounts();

    List<DonationCenterPersonnel> getAllActiveAccounts();

    List<DonationCenterPersonnel> getAllBannedAccounts();
}
