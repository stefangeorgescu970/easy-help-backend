package com.easyhelp.application.service.donation_form;

import com.easyhelp.application.model.donations.DonationForm;

public interface DonationFormServiceInterface {

    void addDonationForm(DonationForm donationForm);

    DonationForm getDonationFormForDonor(Long donorId);

    void removeForm(DonationForm donationForm);
}
