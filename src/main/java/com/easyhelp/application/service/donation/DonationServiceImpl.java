package com.easyhelp.application.service.donation;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.BloodType;
import com.easyhelp.application.model.blood.SeparatedBloodType;
import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.donations.Donation;
import com.easyhelp.application.model.donations.DonationStatus;
import com.easyhelp.application.model.donations.DonationTestResult;
import com.easyhelp.application.model.dto.dcp.incoming.DonationTestResultCreateDTO;
import com.easyhelp.application.model.dto.dcp.incoming.DonationSplitResultCreateDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.repository.DonationRepository;
import com.easyhelp.application.repository.DonationTestResultRepository;
import com.easyhelp.application.service.bloodtype.BloodTypeServiceInterface;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.service.donor.DonorServiceInterface;
import com.easyhelp.application.service.separated_bloodtype.SeparatedBloodTypeServiceInterface;
import com.easyhelp.application.service.stored_blood.StoredBloodServiceInterface;
import com.easyhelp.application.utils.PushNotificationUtils;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import com.easyhelp.application.utils.exceptions.PushTokenUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    public Donation saveDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    @Override
    public void addTestResults(DonationTestResultCreateDTO donationTestResultDTO) throws EntityNotFoundException {
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

        if (donationTestResultDTO.isFailed()) {
            donationUnwrapped.setStatus(DonationStatus.FAILED);
        } else {
            donationUnwrapped.setStatus(DonationStatus.AWAITING_SPLIT_RESULTS);
        }

        try {
            PushNotificationUtils.sendPushNotification(donationUnwrapped.getDonor(), "Test Results available for your last donation.");
        } catch (PushTokenUnavailableException e) {
            e.printStackTrace();
        }

        donationTestResultRepository.save(donationTestResult);
        donationRepository.save(donationUnwrapped);
    }

    @Override
    public void separateBlood(DonationSplitResultCreateDTO donationSplitResultCreateDTO) throws EasyHelpException {
        Optional<Donation> donation = donationRepository.findById(donationSplitResultCreateDTO.getDonationId());

        if (!donation.isPresent())
            throw new EntityNotFoundException("Donation with that id does not exist");

        Donation donationUnwrapped = donation.get();

        if (donationUnwrapped.getStatus() != DonationStatus.AWAITING_SPLIT_RESULTS) {
            throw new EasyHelpException("Donation with that id is not awaiting split results");
        }

        Donor donor = donationUnwrapped.getDonor();
        DonationCenter donationCenter = donationUnwrapped.getDonationCenter();


        storeBlood(BloodComponent.RED_BLOOD_CELLS, donationSplitResultCreateDTO.getRedBloodCellsUnits(), donor, donationCenter);
        storeBlood(BloodComponent.PLASMA, donationSplitResultCreateDTO.getPlasmaUnits(), donor, donationCenter);
        storeBlood(BloodComponent.PLATELETS, donationSplitResultCreateDTO.getPlateletsUnits(), donor, donationCenter);

        donationUnwrapped.setStatus(DonationStatus.COMPLETED);
        donationRepository.save(donationUnwrapped);
    }

    @Override
    public List<Donation> getDonationsForDonor(Long donorId) {
        return donationRepository.findAll().stream().filter(donation -> donation.getDonor().getId().equals(donorId)).collect(Collectors.toList());
    }

    @Override
    public List<Donation> getDonationsAwaitingTestResultForDonationCenter(Long donationCenterId) {
        return donationRepository.findAll().stream().filter(donation -> donation.getDonationCenter().getId().equals(donationCenterId) && donation.getStatus().equals(DonationStatus.AWAITING_CONTROL_TESTS)).collect(Collectors.toList());
    }

    @Override
    public List<Donation> getDonationsAwaitingSplitResultForDonationCenter(Long donationCenterId) {
        return donationRepository.findAll().stream().filter(donation -> donation.getDonationCenter().getId().equals(donationCenterId) && donation.getStatus().equals(DonationStatus.AWAITING_SPLIT_RESULTS)).collect(Collectors.toList());
    }

    private void storeBlood(BloodComponent bloodComponent, Double quantity, Donor donor, DonationCenter donationCenter) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

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

        String codeSeparator = ".";
        storedBlood.setBagIdentifier(storedBlood.getSeparatedBloodType().getComponent().codeString() + codeSeparator +
                storedBlood.getSeparatedBloodType().getBloodType().getGroupLetter() + codeSeparator +
                (storedBlood.getSeparatedBloodType().getBloodType().getRh().equals(true) ? "+" : "-") + codeSeparator +
                storedBlood.getDonor().getId() + codeSeparator +
                storedBlood.getDonationCenter().getId() + codeSeparator +
                simpleDateFormat.format(storedBlood.getStoredDate()));

        donor.getStoredBloodSet().add(storedBlood);
        donationCenter.getStoredBloodSet().add(storedBlood);
        separatedBloodType.getStoredBloodSet().add(storedBlood);

        separatedBloodTypeService.save(separatedBloodType);
        donorService.save(donor);
        donationCenterService.save(donationCenter);
        storedBloodService.storeBlood(storedBlood);
    }
}
