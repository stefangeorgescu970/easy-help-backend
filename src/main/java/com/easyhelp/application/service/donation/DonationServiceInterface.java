package com.easyhelp.application.service.donation;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.dto.dcp.incoming.DonationTestResultCreateDTO;
import com.easyhelp.application.model.dto.dcp.incoming.DonationSplitResultCreateDTO;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;

import java.util.List;

public interface DonationServiceInterface {

    Donation saveDonation(Donation donation);

    Donation findById(Long donationId) throws EntityNotFoundException;

    void addTestResults(DonationTestResultCreateDTO donationTestResultDTO) throws EntityNotFoundException;

    void separateBlood(DonationSplitResultCreateDTO donationSplitResultCreateDTO) throws EasyHelpException;

    List<Donation> getDonationsForDonor(Long donorId);

    List<Donation> getDonationsAwaitingTestResultForDonationCenter(Long donationCenterId);

    List<Donation> getDonationsAwaitingSplitResultForDonationCenter(Long donationCenterId);
}
