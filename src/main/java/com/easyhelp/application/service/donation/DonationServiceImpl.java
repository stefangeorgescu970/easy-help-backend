package com.easyhelp.application.service.donation;

import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationStatus;
import com.easyhelp.application.model.donations.DonationTestResult;
import com.easyhelp.application.model.dto.donation.DonationTestResultDTO;
import com.easyhelp.application.repository.DonationRepository;
import com.easyhelp.application.repository.DonationTestResultRepository;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DonationServiceImpl implements DonationServiceInterface {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationTestResultRepository donationTestResultRepository;

    @Override
    public void saveDonation(Donation donation) {
        donationRepository.save(donation);
    }

    @Override
    public void addTestResults(DonationTestResultDTO donationTestResultDTO) throws EntityNotFoundException {
        Optional<Donation> donation = donationRepository.findById(donationTestResultDTO.getDonationId());

        if(!donation.isPresent())
            throw new EntityNotFoundException("Donation with that id does not exist");

        Donation donationUnwrapped = donation.get();

        if (donationUnwrapped.getStatus() != DonationStatus.AWAITING_CONTROL_TESTS) {
            throw new EntityNotFoundException("Donation with that id already has test results");
        }

        DonationTestResult donationTestResult = new DonationTestResult(donationTestResultDTO);

        donationUnwrapped.setTestResults(donationTestResult);
        donationTestResult.setDonation(donationUnwrapped);

        if (donationTestResultDTO.getHasFailed()) {
            donationUnwrapped.setStatus(DonationStatus.FAILED);
        } else {
            donationUnwrapped.setStatus(DonationStatus.AWAITING_SPLIT_RESULTS);
        }

        donationTestResultRepository.save(donationTestResult);
        donationRepository.save(donationUnwrapped);
    }
}
