package com.easyhelp.application.service.donation;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.blood.SeparatedBloodType;
import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationStatus;
import com.easyhelp.application.model.donations.DonationTestResult;
import com.easyhelp.application.model.dto.donation.DonationSplitResultsDTO;
import com.easyhelp.application.model.dto.donation.DonationTestResultDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.requests.DonationRequest;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.DonationRepository;
import com.easyhelp.application.repository.DonationTestResultRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
import com.easyhelp.application.service.separated_bloodtype.SeparatedBloodTypeServiceInterface;
import com.easyhelp.application.service.stored_blood.StoredBloodServiceInterface;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DonationServiceImpl implements DonationServiceInterface {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationTestResultRepository donationTestResultRepository;

    @Autowired
    private StoredBloodServiceInterface storedBloodService;

    @Autowired
    private SeparatedBloodTypeServiceInterface separatedBloodTypeService;

    @Autowired
    private BloodTypeServiceInterface bloodTypeService;

    @Autowired
    private DonorServiceInterface donorService;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @Override
    public void saveDonation(Donation donation) {
        donationRepository.save(donation);
    }

    @Override
    public void addTestResults(DonationTestResultDTO donationTestResultDTO) throws EntityNotFoundException {
        Optional<Donation> donation = donationRepository.findById(donationTestResultDTO.getDonationId());

        if (!donation.isPresent())
            throw new EntityNotFoundException("Donation with that id does not exist");

        Donation donationUnwrapped = donation.get();

        if (donationUnwrapped.getStatus() != DonationStatus.AWAITING_CONTROL_TESTS) {
            throw new EntityNotFoundException("Donation with that id is not waiting for control tests");
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

    @Override
    public void separateBlood(DonationSplitResultsDTO donationSplitResultsDTO) throws EasyHelpException {
        Optional<Donation> donation = donationRepository.findById(donationSplitResultsDTO.getDonationId());

        if (!donation.isPresent())
            throw new EntityNotFoundException("Donation with that id does not exist");

        Donation donationUnwrapped = donation.get();

        if (donationUnwrapped.getStatus() != DonationStatus.AWAITING_SPLIT_RESULTS) {
            throw new EasyHelpException("Donation with that id is not awaiting split results");
        }

        Donor donor = donationUnwrapped.getDonor();
        DonationCenter donationCenter = donationUnwrapped.getDonationCenter();


        storeBlood(BloodComponent.RED_BLOOD_CELLS, donationSplitResultsDTO.getRedBloodCellsUnits(), donor, donationCenter);
        storeBlood(BloodComponent.PLASMA, donationSplitResultsDTO.getPlasmaUnits(), donor, donationCenter);
        storeBlood(BloodComponent.PLATELETS, donationSplitResultsDTO.getPlateletsUnits(), donor, donationCenter);

        donationUnwrapped.setStatus(DonationStatus.COMPLETED);
        donationRepository.save(donationUnwrapped);
    }

    @Override
    public List<Donation> getDonationsForDonor(Long donorId) {
        return donationRepository.findAll().stream().filter(donation -> donation.getDonor().getId().equals(donorId)).collect(Collectors.toList());
    }

    private void storeBlood(BloodComponent bloodComponent, Double quantity, Donor donor, DonationCenter donationCenter) {
        SeparatedBloodType separatedBloodType = separatedBloodTypeService.findSeparatedBloodTypeInDB(donor.getBloodType().getGroupLetter(), donor.getBloodType().getRh(), bloodComponent);
        if (separatedBloodType == null) {
            separatedBloodType = new SeparatedBloodType();
            separatedBloodType.setComponent(bloodComponent);
            BloodType bloodTypeInDb = bloodTypeService.findBloodTypeInDB(donor.getBloodType().getGroupLetter(), donor.getBloodType().getRh());
            separatedBloodType.setBloodType(bloodTypeInDb);
        }

        StoredBlood storedBlood = new StoredBlood();

        storedBlood.setDonor(donor);
        storedBlood.setDonationCenter(donationCenter);
        storedBlood.setSeparatedBloodType(separatedBloodType);
        storedBlood.setStoredDate(new Date());
        storedBlood.setAmount(quantity);

        donor.getStoredBloodSet().add(storedBlood);
        donationCenter.getStoredBloodSet().add(storedBlood);
        separatedBloodType.getStoredBloodSet().add(storedBlood);

        separatedBloodTypeService.save(separatedBloodType);
        donorService.save(donor);
        donationCenterService.save(donationCenter);

        storedBloodService.storeBlood(storedBlood);
    }
}
