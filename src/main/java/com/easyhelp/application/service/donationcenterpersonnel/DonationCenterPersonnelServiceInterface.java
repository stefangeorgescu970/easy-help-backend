package com.easyhelp.application.service.donationcenterpersonnel;

import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.utils.exceptions.AccountNotReviewedException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DonationCenterPersonnelServiceInterface {

    List<DonationCenterPersonnel> getAllPendingAccounts();

    List<DonationCenterPersonnel> getAllActiveAccounts();

    List<DonationCenterPersonnel> getAllBannedAccounts();

    void reviewAccount(Long dcpId, boolean shouldValidate) throws EntityNotFoundException;

    void deactivateAccount(Long dcpId) throws AccountNotReviewedException, EntityNotFoundException;

    DonationCenterPersonnel findById(Long dcpId) throws EntityNotFoundException;
}