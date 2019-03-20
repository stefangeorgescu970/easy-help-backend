package com.easyhelp.application.service.donationcenterpersonnel;

import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.repository.DonationCenterPersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationCenterPersonnelServiceImpl implements DonationCenterPersonnelServiceInterface {

    @Autowired
    private DonationCenterPersonnelRepository donationCenterPersonnelRepository;

    @Override
    public List<DonationCenterPersonnel> getAllPendingAccounts() {
        return donationCenterPersonnelRepository.findAll().stream().filter(dcp -> !dcp.getIsReviewed()).collect(Collectors.toList());
    }

    @Override
    public List<DonationCenterPersonnel> getAllActiveAccounts() {
        return donationCenterPersonnelRepository.findAll().stream().filter(dcp -> dcp.getIsReviewed() && dcp.getIsValid()).collect(Collectors.toList());
    }

    @Override
    public List<DonationCenterPersonnel> getAllBannedAccounts() {
        return donationCenterPersonnelRepository.findAll().stream().filter(dcp -> dcp.getIsReviewed() && !dcp.getIsValid()).collect(Collectors.toList());
    }
}
