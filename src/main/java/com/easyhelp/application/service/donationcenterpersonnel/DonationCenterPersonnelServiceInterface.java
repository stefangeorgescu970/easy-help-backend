package com.easyhelp.application.service.donationcenterpersonnel;

import com.easyhelp.application.model.dto.account.DonationCenterPersonnelAccountDTO;
import com.easyhelp.application.utils.exceptions.AccountNotReviewedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DonationCenterPersonnelServiceInterface {

    List<DonationCenterPersonnelAccountDTO> getAllPendingAccounts();

    List<DonationCenterPersonnelAccountDTO> getAllActiveAccounts();

    List<DonationCenterPersonnelAccountDTO> getAllBannedAccounts();

    void reviewAccount(Long dcpId, boolean shouldValidate) throws EntityNotFoundException;

    void deactivateAccount(Long dcpId) throws AccountNotReviewedException, EntityNotFoundException;
}