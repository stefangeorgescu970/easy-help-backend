package com.easyhelp.application.service.donation_form;

import com.easyhelp.application.model.donations.DonationForm;
import com.easyhelp.application.repository.DonationFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonationFormServiceImpl implements DonationFormServiceInterface {

    @Autowired
    private DonationFormRepository donationFormRepository;

    @Override
    public void addDonationForm(DonationForm donationForm) {
        donationFormRepository.save(donationForm);
    }
}
