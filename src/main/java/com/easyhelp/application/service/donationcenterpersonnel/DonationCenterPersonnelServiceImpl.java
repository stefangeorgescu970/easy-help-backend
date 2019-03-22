package com.easyhelp.application.service.donationcenterpersonnel;

import com.easyhelp.application.model.dto.account.DonationCenterPersonnelAccountRequestDTO;
import com.easyhelp.application.model.users.DonationCenterPersonnel;
import com.easyhelp.application.repository.DonationCenterPersonnelRepository;
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
    public List<DonationCenterPersonnelAccountRequestDTO> getAllPendingAccounts() {
        return donationCenterPersonnelRepository
                .findAll()
                .stream()
                .filter(dcp -> !dcp.getIsReviewed())
                .map(DonationCenterPersonnelAccountRequestDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationCenterPersonnel> getAllActiveAccounts() {
        return donationCenterPersonnelRepository.findAll().stream().filter(dcp -> dcp.getIsReviewed() && dcp.getIsValid()).collect(Collectors.toList());
    }

    @Override
    public List<DonationCenterPersonnel> getAllBannedAccounts() {
        return donationCenterPersonnelRepository.findAll().stream().filter(dcp -> dcp.getIsReviewed() && !dcp.getIsValid()).collect(Collectors.toList());
    }

    @Override
    public void reviewAccount(Long dcpId, boolean shouldValidate) throws EntityNotFoundException {
        Optional<DonationCenterPersonnel> doctor = donationCenterPersonnelRepository.findById(dcpId);

        if (doctor.isPresent()) {
            DonationCenterPersonnel dcpUnwrapped = doctor.get();
            dcpUnwrapped.reviewAccount(shouldValidate);
            donationCenterPersonnelRepository.save(dcpUnwrapped);
        } else {
            throw new EntityNotFoundException("user not found");
        }
    }
}
