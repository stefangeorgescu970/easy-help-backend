package com.easyhelp.application.service.donation;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.dto.donation.DonationSplitResultsDTO;
import com.easyhelp.application.model.dto.donation.DonationTestResultDTO;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DonationServiceInterface {

    void saveDonation(Donation donation);

    void addTestResults(DonationTestResultDTO donationTestResultDTO) throws EntityNotFoundException;

    void separateBlood(DonationSplitResultsDTO donationSplitResultsDTO) throws EasyHelpException;

    List<Donation> getDonationsForDonor(Long donorId);
}
