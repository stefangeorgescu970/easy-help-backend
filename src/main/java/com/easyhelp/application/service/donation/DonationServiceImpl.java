package com.easyhelp.application.service.donation;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonationServiceImpl implements DonationServiceInterface {

    @Autowired
    private DonationRepository donationRepository;

    @Override
    public void saveDonation(Donation donation) {
        donationRepository.save(donation);
    }
}
