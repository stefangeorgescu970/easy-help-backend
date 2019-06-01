package com.easyhelp.application.service.donationcenterpersonnel;

import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.repository.DonationCenterPersonnelRepository;
import com.easyhelp.application.utils.EmailUtils;
import com.easyhelp.application.utils.exceptions.AccountNotReviewedException;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DonationCenterPersonnelServiceImpl implements DonationCenterPersonnelServiceInterface {

    @Autowired
    private DonationCenterPersonnelRepository donationCenterPersonnelRepository;

    @Override
    public List<DonationCenterPersonnel> getAllPendingAccounts() {
        return donationCenterPersonnelRepository
                .findAll()
                .stream()
                .filter(dcp -> !dcp.getIsReviewed())
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationCenterPersonnel> getAllActiveAccounts() {
        return donationCenterPersonnelRepository
                .findAll().stream()
                .filter(dcp -> dcp.getIsReviewed() && dcp.getIsValid())
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationCenterPersonnel> getAllBannedAccounts() {
        return donationCenterPersonnelRepository
                .findAll()
                .stream()
                .filter(dcp -> dcp.getIsReviewed() && !dcp.getIsValid())
                .collect(Collectors.toList());
    }

    @Override
    public void reviewAccount(Long dcpId, boolean shouldValidate) throws EntityNotFoundException {
        Optional<DonationCenterPersonnel> donationCenterPersonnel = donationCenterPersonnelRepository.findById(dcpId);

        if (donationCenterPersonnel.isPresent()) {
            DonationCenterPersonnel dcpUnwrapped = donationCenterPersonnel.get();
            dcpUnwrapped.reviewAccount(shouldValidate);
            donationCenterPersonnelRepository.save(dcpUnwrapped);
            new Thread(() -> {
                try {
                    EmailUtils.sendAccountReviewedEmail(dcpUnwrapped, shouldValidate);
                } catch (EasyHelpException ignored) {}
            }).start();
        } else {
            throw new EntityNotFoundException("user not found");
        }
    }

    @Override
    public void deactivateAccount(Long dcpId) throws AccountNotReviewedException, EntityNotFoundException {
        Optional<DonationCenterPersonnel> donationCenterPersonnel = donationCenterPersonnelRepository.findById(dcpId);

        if (donationCenterPersonnel.isPresent()) {
            DonationCenterPersonnel dcpUnwrapped = donationCenterPersonnel.get();
            dcpUnwrapped.invalidateAccount();
            donationCenterPersonnelRepository.save(dcpUnwrapped);
        } else {
            throw new EntityNotFoundException("user not found");
        }
    }

    @Override
    public DonationCenterPersonnel findById(Long dcpId) throws EntityNotFoundException {
        Optional<DonationCenterPersonnel> donationCenterPersonnel = donationCenterPersonnelRepository.findById(dcpId);

        if (donationCenterPersonnel.isPresent()) {
            return donationCenterPersonnel.get();
        } else {
            throw new EntityNotFoundException("user not found");
        }
    }
}
